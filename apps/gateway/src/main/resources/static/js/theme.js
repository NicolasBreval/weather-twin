
document.addEventListener('DOMContentLoaded', () => {
  const storedTheme = localStorage.getItem('gateway-theme') || 'light';
  document.documentElement.setAttribute('data-theme', storedTheme === 'light' ? 'cupcake' : 'synthwave');

  const themeController = document.querySelector('#theme-controller');
  if (themeController) {
    themeController.checked = storedTheme === 'dark'
  }

  document.querySelector('#theme-controller')?.addEventListener('change', (e) => {
    const theme = e.target.checked ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', theme === 'light' ? 'cupcake' : 'synthwave');
    localStorage.setItem('gateway-theme', theme);
  })
});