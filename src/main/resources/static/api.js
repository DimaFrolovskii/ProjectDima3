const API_URL = 'http://localhost:8080/api/assets';

async function loadAssets() {
    const token = localStorage.getItem('token');
    // Получаем роль и приводим к верхнему регистру на всякий случай
    const userRole = localStorage.getItem('userRole') ? localStorage.getItem('userRole').toUpperCase() : '';

    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(API_URL, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 403 || response.status === 401) {
            alert("Сессия истекла или недостаточно прав");
            logout();
            return;
        }

        const data = await response.json();
        const assets = data.content || [];

        const tbody = document.getElementById('assetsBody');
        if (!tbody) return;
        tbody.innerHTML = '';

        assets.forEach(asset => {
            // Проверяем роль. В базе может быть 'ADMIN' или 'ROLE_ADMIN'
            let deleteBtn = '';
            if (userRole === 'ADMIN' || userRole === 'ROLE_ADMIN') {
                deleteBtn = `
                    <button onclick="deleteAsset(${asset.id})"
                            style="background:#dc3545; color:white; padding:5px; width:auto;">
                        Удалить
                    </button>
                `;
            }

            tbody.innerHTML += `
                <tr>
                    <td>${asset.id}</td>
                    <td>${asset.name}</td>
                    <td>${asset.type}</td>
                    <td>${asset.status}</td>
                    <td>
                        <button onclick="prepareEdit(${asset.id}, '${asset.name}', '${asset.type}', '${asset.status}')"
                                style="background:#ffc107; color:black; padding:5px; width:auto; margin-right:5px;">
                            Править
                        </button>
                        ${deleteBtn}
                    </td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Ошибка загрузки:", err);
    }
}

async function saveAsset() {
    const name = document.getElementById('assetName').value;
    const type = document.getElementById('assetType').value;
    const status = document.getElementById('assetStatus').value;
    const token = localStorage.getItem('token');

    if (!name || !type || !status) {
        alert("Заполните все поля");
        return;
    }

    const assetData = { name, type, status };
    let url = API_URL;
    let method = 'POST';

    if (editMode) {
        url = `${API_URL}/${currentEditId}`;
        method = 'PUT';
    }

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
            resetForm();
            loadAssets();
        } else {
            const errData = await response.text();
            alert("Ошибка сохранения: " + (response.status === 403 ? "Недостаточно прав" : errData));
        }
    } catch (err) {
        alert("Ошибка сети");
    }
}

async function deleteAsset(id) {
    const token = localStorage.getItem('token');
    if (!confirm("Удалить этот актив?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
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

let editMode = false;
let currentEditId = null;

function prepareEdit(id, name, type, status) {
    document.getElementById('assetName').value = name;
    document.getElementById('assetType').value = type;
    document.getElementById('assetStatus').value = status;
    editMode = true;
    currentEditId = id;
    const btn = document.querySelector('.container button');
    if (btn) {
        btn.textContent = "Сохранить изменения";
        btn.style.background = "#ffc107";
    }
}

function resetForm() {
    editMode = false;
    currentEditId = null;
    document.getElementById('assetName').value = '';
    document.getElementById('assetType').value = '';
    document.getElementById('assetStatus').value = '';
    const btn = document.querySelector('.container button');
    if (btn) {
        btn.textContent = "Создать";
        btn.style.background = "#28a745";
    }
}

// Если функция logout не определена в другом месте
if (typeof logout !== 'function') {
    function logout() {
        localStorage.clear();
        window.location.href = 'index.html';
    }
}