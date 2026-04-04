// --- АВТОРИЗАЦИЯ И РЕГИСТРАЦИЯ ---

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

            // Сохраняем токен и роль для использования в api.js
            localStorage.setItem('token', data.token);
            localStorage.setItem('userRole', data.role);

            window.location.href = 'assets.html';
        } else {
            errorDiv.innerText = "Неверный логин или пароль";
            errorDiv.style.display = 'block';
        }
    } catch (err) {
        console.error("Ошибка сети", err);
        alert("Бэкенд не отвечает!");
    }
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

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    window.location.href = 'index.html';
}