const API_URL = 'http://localhost:8080/api/assets';

// Функция получения всех активов
async function loadAssets() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'index.html'; // Если нет токена — на вход
        return;
    }

    try {
        const response = await fetch(API_URL, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 403 || response.status === 401) {
            logout(); // Токен протух или неверный
            return;
        }

        const assets = await response.json();
        const tbody = document.getElementById('assetsBody');
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
        alert("Ошибка при загрузке данных");
    }
}

// Функция добавления актива
async function addAsset() {
    const name = document.getElementById('assetName').value;
    const type = document.getElementById('assetType').value;
    const status = document.getElementById('assetStatus').value;
    const token = localStorage.getItem('token');

    const assetData = { name, type, status };

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(assetData)
    });

    if (response.ok) {
        loadAssets(); // Обновляем таблицу
    } else {
        alert("Ошибка при сохранении");
    }
}

// Функция удаления
async function deleteAsset(id) {
    const token = localStorage.getItem('token');
    if (!confirm("Удалить этот актив?")) return;

    await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });
    loadAssets();
}

let editMode = false;
let currentEditId = null;

// Функция для заполнения формы данными из таблицы
function prepareEdit(id, name, type, status) {
    document.getElementById('assetName').value = name;
    document.getElementById('assetType').value = type;
    document.getElementById('assetStatus').value = status;
    
    editMode = true;
    currentEditId = id;
    
    const btn = document.querySelector('.container button');
    btn.textContent = "Сохранить изменения";
    btn.style.background = "#ffc107"; // Желтый цвет для режима правки
}

// Изменяем функцию addAsset, чтобы она понимала, создаем мы или редактируем
async function saveAsset() {
    const name = document.getElementById('assetName').value;
    const type = document.getElementById('assetType').value;
    const status = document.getElementById('assetStatus').value;
    const token = localStorage.getItem('token');

    const assetData = { name, type, status };
    
    let url = API_URL;
    let method = 'POST';

    if (editMode) {
        url = `${API_URL}/${currentEditId}`;
        method = 'PUT'; // В бэкенде должен быть реализован @PutMapping
    }

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