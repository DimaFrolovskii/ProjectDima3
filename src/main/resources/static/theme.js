// IIFE (Immediately Invoked Function Expression)
(function() {
    const themeKey = 'theme-preference';

    const getTheme = () => {
        return localStorage.getItem(themeKey) || 'light';
    };

    const applyTheme = (theme) => {
        // Устанавливаем класс на <html>
        document.documentElement.className = '';
        document.documentElement.classList.add(`${theme}-theme`);

        // Ищем кнопку КАЖДЫЙ РАЗ при вызове функции.
        // Это гарантирует, что мы найдем её, как только DOM загрузится.
        const btn = document.getElementById('theme-toggle');
        if (btn) {
            btn.innerHTML = theme === 'dark' ? '☀️' : '🌙';
        }
    };

    // Применяем тему сразу же (чтобы не было мигания страницы)
    applyTheme(getTheme());

    // Вешаем обработчик на кнопку, когда DOM будет полностью готов
    document.addEventListener('DOMContentLoaded', () => {
        // Вызываем функцию еще раз, чтобы обновить иконку,
        // так как при первом вызове в <head> кнопки еще не было
        applyTheme(getTheme());

        const toggleButton = document.getElementById('theme-toggle');
        if (toggleButton) {
            toggleButton.addEventListener('click', () => {
                const newTheme = getTheme() === 'light' ? 'dark' : 'light';
                localStorage.setItem(themeKey, newTheme);
                applyTheme(newTheme);
            });
        }
    });
})();