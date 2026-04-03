const API_URL = 'http://localhost:8080/api/assets';

// 1. ФУНКЦИЯ ЗАГРУЗКИ (ИСПРАВЛЕНА ПОД ПАГИНАЦИЮ)
async function loadAssets() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(API_URL, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 403 || response.status === 401) {
            logout();
            return;
        }

        const data = await response.json();

        // КЛЮЧЕВОЕ ИЗМЕНЕНИЕ: берём данные из поля content
        const assets = data.content || [];

        const tbody = document.getElementById('assetsBody');
        if (!tbody) return;

        tbody.innerHTML = '';

        assets.forEach(asset => {
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
                        <button onclick="deleteAsset(${asset.id})"
                                style="background:#dc3545; color:white; padding:5px; width:auto;">
                            Удалить
                        </button>
                    </td>
                </tr>
            `;
        });
    } catch (err) {
        console.error("Ошибка:", err);
        alert("Ошибка при загрузке данных");
    }
}

// 2. ФУНКЦИЯ СОХРАНЕНИЯ (СОЗДАНИЕ И ОБНОВЛЕНИЕ)
let editMode = false;
let currentEditId = null;

async function saveAsset() {
    const name = document.getElementById('assetName').value;
    const type = document.getElementById('assetType').value;
    const status = document.getElementById('assetStatus').value;
    const token = localStorage.getItem('token');

    // Если ты добавил facilityId в AssetDTO, можно добавить его и сюда
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
            alert("Ошибка при сохранении");
        }
    } catch (err) {
        alert("Проблема с соединением");
    }
}

// 3. ФУНКЦИЯ УДАЛЕНИЯ
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
        } else {
            alert("Ошибка при удалении (возможно, недостаточно прав)");
        }
    } catch (err) {
        alert("Ошибка сети");
    }
}

// 4. ВСПОМОГАТЕЛЬНЫЕ ФУНКЦИИ
function prepareEdit(id, name, type, status) {
    document.getElementById('assetName').value = name;
    document.getElementById('assetType').value = type;
    document.getElementById('assetStatus').value = status;

    editMode = true;
    currentEditId = id;

    const btn = document.querySelector('.container button');
    btn.textContent = "Сохранить изменения";
    btn.style.background = "#ffc107";
}

function resetForm() {
    editMode = false;
    currentEditId = null;
    document.getElementById('assetName').value = '';
    document.getElementById('assetType').value = '';
    document.getElementById('assetStatus').value = '';
    const btn = document.querySelector('.container button');
    btn.textContent = "Создать";
    btn.style.background = "#28a745";
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}