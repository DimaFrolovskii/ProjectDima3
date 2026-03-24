async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('error');

    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            // ВАЖНО: сохраняем токен, который прислал сервер
            localStorage.setItem('token', data.token);
            window.location.href = 'assets.html';
        } else {
            // Если 401 или 403 при логине - значит неверный пароль
            errorDiv.innerText = "Неверный логин или пароль";
            errorDiv.style.display = 'block';
        }
    } catch (err) {
        console.error("Ошибка сети", err);
        alert("Бэкенд не отвечает!");
    }
}

function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

async function register() {
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;
    const errorDiv = document.getElementById('regError');

    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        if (response.ok) {
            alert("Регистрация успешна! Теперь войдите.");
            window.location.href = 'index.html';
        } else {
            errorDiv.innerText = "Ошибка регистрации (возможно, логин занят)";
            errorDiv.style.display = 'block';
        }
    } catch (err) {
        alert("Ошибка сервера при регистрации");
    }
}

// Эту функцию вызывай на странице assets.html при загрузке (onload)
async function fetchAssets() {
    const token = localStorage.getItem('token');

    const response = await fetch('http://localhost:8080/api/assets', { // Твой защищенный API
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token, // ПЕРЕДАЕМ ТОКЕН ТУТ
            'Content-Type': 'application/json'
        }
    });

    if (response.status === 403) {
        alert("Доступ запрещен. Войдите заново.");
        window.location.href = 'index.html';
    } else {
        const data = await response.json();
        console.log("Список активов:", data);
        // Тут код отрисовки данных на странице
    }
}