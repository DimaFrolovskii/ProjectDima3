// theme.js

const ThemeManager = (() => {
    const THEME_KEY = 'theme-preference';

    const getTheme = () => localStorage.getItem(THEME_KEY) || 'light';

    const applyTheme = (theme) => {
        document.documentElement.className = '';
        document.documentElement.classList.add(theme + '-theme');
        const btn = document.getElementById('theme-toggle');
        if (btn) btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
    };

    // Применяем тему сразу, чтобы не было мигания
    applyTheme(getTheme());

    const toggle = () => {
        const newTheme = getTheme() === 'light' ? 'dark' : 'light';
        localStorage.setItem(THEME_KEY, newTheme);
        applyTheme(newTheme);
    };

    // Вешаем обработчик после загрузки DOM
    document.addEventListener('DOMContentLoaded', () => {
        applyTheme(getTheme()); // обновляем иконку кнопки
        const btn = document.getElementById('theme-toggle');
        if (btn) btn.addEventListener('click', toggle);
    });

    return { toggle, applyTheme, getTheme };
})();

// Глобальная функция для вызова из onclick в HTML
function changeTheme() {
    ThemeManager.toggle();
}