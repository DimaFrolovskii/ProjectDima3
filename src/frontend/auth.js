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
            localStorage.setItem('token', data.token); // Сохраняем JWT
            window.location.href = 'assets.html'; // Переходим к списку
        } else {
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
            errorDiv.style.display = 'block';
        }
    } catch (err) {
        alert("Ошибка сервера при регистрации");
    }
}