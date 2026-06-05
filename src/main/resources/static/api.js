const API_URL = '/api';

// --- Auth & General ---
if (typeof logout !== 'function') {
    function logout() {
        localStorage.clear();
        window.location.href = 'index.html';
    }
}

// --- Dashboard ---
async function loadStats() {
    // ... (код без изменений)
}

// --- Users ---
async function loadUsers() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/users`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!response.ok) throw new Error('Не удалось загрузить пользователей');

        const users = await response.json();
        const tbody = document.getElementById('usersBody');
        tbody.innerHTML = '';

        users.forEach(user => {
            tbody.innerHTML += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.role}</td>
                    <td>${user.companyName || 'Не назначено'}</td>
                    <td>${user.departmentName || 'Не назначено'}</td>
                    <td style="text-align: right;">
                        <button onclick='openUserModal(${JSON.stringify(user)})'>Подробнее</button>
                    </td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Ошибка загрузки пользователей:", err);
    }
}

async function openUserModal(user) {
    document.getElementById('userIdToAssign').value = user.id;
    document.getElementById('modalUserName').textContent = user.username;
    document.getElementById('modalUserId').textContent = user.id;
    document.getElementById('modalUserRole').textContent = user.role;

    const userModal = document.getElementById('userModal');
    userModal.style.display = 'block';

    const companySelect = document.getElementById('companySelect');
    companySelect.onchange = () => loadDepartmentsForModal(companySelect.value);

    await loadCompaniesForModal(user.companyId);
    await loadDepartmentsForModal(user.companyId, user.departmentId);
}

async function assignUser() {
    const userId = document.getElementById('userIdToAssign').value;
    const companyId = document.getElementById('companySelect').value;
    const departmentId = document.getElementById('departmentSelect').value;
    const token = localStorage.getItem('token');

    if (!companyId || !departmentId) {
        alert('Выберите компанию и отдел');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/users/${userId}/assign`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ companyId, departmentId })
        });

        if (response.ok) {
            document.getElementById('userModal').style.display = 'none';
            loadUsers();
        } else {
            const error = await response.json();
            alert('Ошибка назначения: ' + (error.message || 'Проверьте данные'));
        }
    } catch (err) {
        console.error('Ошибка:', err);
    }
}

// --- Companies & Departments Management ---

async function createCompany() {
    const name = document.getElementById('companyName').value;
    const address = document.getElementById('companyAddress').value;
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_URL}/companies`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify({ name, address })
        });

        if (response.ok) {
            document.getElementById('createCompanyForm').reset();
            loadCompanyList();
        } else {
            alert('Ошибка создания компании');
        }
    } catch (err) {
        console.error('Ошибка:', err);
    }
}

async function createDepartment() {
    const companyId = document.getElementById('departmentCompanySelect').value;
    const name = document.getElementById('departmentName').value;
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_URL}/departments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
            body: JSON.stringify({ name, companyId })
        });

        if (response.ok) {
            document.getElementById('createDepartmentForm').reset();
            loadDepartmentList();
        } else {
            alert('Ошибка создания отдела');
        }
    } catch (err) {
        console.error('Ошибка:', err);
    }
}

async function loadCompanyList() {
    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/companies`, { headers: { 'Authorization': `Bearer ${token}` } });
    const companies = await response.json();

    const list = document.getElementById('companyList');
    const select = document.getElementById('departmentCompanySelect');

    list.innerHTML = '';
    select.innerHTML = '<option value="">Выберите компанию</option>';

    companies.forEach(c => {
        list.innerHTML += `<li>${c.name} (ID: ${c.id})</li>`;
        select.innerHTML += `<option value="${c.id}">${c.name}</option>`;
    });
}

async function loadDepartmentList() {
    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/departments`, { headers: { 'Authorization': `Bearer ${token}` } });
    const departments = await response.json();

    const list = document.getElementById('departmentList');
    list.innerHTML = '';
    departments.forEach(d => {
        list.innerHTML += `<li>${d.name} (ID: ${d.id})</li>`;
    });
}

// --- Functions for Modal Selects ---

async function loadCompaniesForModal(selectedId) {
    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/companies`, { headers: { 'Authorization': `Bearer ${token}` } });
    const companies = await response.json();
    const select = document.getElementById('companySelect');
    select.innerHTML = '<option value="">Выберите компанию</option>';
    companies.forEach(c => {
        const selected = c.id === selectedId ? 'selected' : '';
        select.innerHTML += `<option value="${c.id}" ${selected}>${c.name}</option>`;
    });
}

async function loadDepartmentsForModal(companyId, selectedId) {
    const select = document.getElementById('departmentSelect');
    select.innerHTML = '<option value="">Выберите отдел</option>';
    if (!companyId) return;

    const token = localStorage.getItem('token');
    const response = await fetch(`${API_URL}/departments/company/${companyId}`, { headers: { 'Authorization': `Bearer ${token}` } });
    const departments = await response.json();
    departments.forEach(d => {
        const selected = d.id === selectedId ? 'selected' : '';
        select.innerHTML += `<option value="${d.id}" ${selected}>${d.name}</option>`;
    });
}

// --- Assets ---

async function loadAssets() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/assets`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!response.ok) throw new Error('Не удалось загрузить активы');

        const page = await response.json(); // Получаем объект страницы
        const assets = page.content; // Извлекаем из него список активов

        const tbody = document.getElementById('assetsBody');
        tbody.innerHTML = '';

        assets.forEach(asset => {
            tbody.innerHTML += `
                <tr>
                    <td>${asset.id}</td>
                    <td>${asset.name}</td>
                    <td>${asset.type}</td>
                    <td>${asset.status}</td>
                    <td style="text-align: right;">
                        <button onclick='openAssetModal(${JSON.stringify(asset)})'>Подробнее</button>
                    </td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Ошибка загрузки активов:", err);
    }
}

function openAssetModal(asset) {
    const assetModal = document.getElementById('assetModal');
    assetModal.style.display = 'block';

    if (asset) { // Редактирование
        document.getElementById('assetId').value = asset.id;
        document.getElementById('assetName').value = asset.name;
        document.getElementById('assetType').value = asset.type;
        document.getElementById('assetStatus').value = asset.status;
        document.getElementById('assetModalTitle').textContent = 'Редактировать актив';
        document.getElementById('deleteAssetBtn').style.display = 'inline-block';
    } else { // Создание
        document.getElementById('assetForm').reset();
        document.getElementById('assetId').value = '';
        document.getElementById('assetModalTitle').textContent = 'Создать актив';
        document.getElementById('deleteAssetBtn').style.display = 'none';
    }
}

async function saveAsset() {
    const assetId = document.getElementById('assetId').value;
    const name = document.getElementById('assetName').value;
    const type = document.getElementById('assetType').value;
    const status = document.getElementById('assetStatus').value;
    const token = localStorage.getItem('token');

    if (!name || !type || !status) {
        alert("Заполните все поля");
        return;
    }

    const assetData = { name, type, status };
    let url = assetId ? `${API_URL}/assets/${assetId}` : `${API_URL}/assets`;
    let method = assetId ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(assetData)
        });

        if (response.ok) {
            document.getElementById('assetModal').style.display = 'none';
            loadAssets();
        } else {
            const errData = await response.text();
            alert("Ошибка сохранения: " + (response.status === 403 ? "Недостаточно прав" : errData));
        }
    } catch (err) {
        alert("Ошибка сети");
    }
}

async function deleteAsset() {
    const assetId = document.getElementById('assetId').value;
    const token = localStorage.getItem('token');
    if (!confirm("Удалить этот актив?")) return;

    try {
        const response = await fetch(`${API_URL}/assets/${assetId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            document.getElementById('assetModal').style.display = 'none';
            loadAssets();
        } else if (response.status === 403) {
            alert("Ошибка: Только администратор может удалять активы.");
        } else {
            alert("Ошибка при удалении");
        }
    } catch (err) {
        alert("Ошибка сети");
    }
}
