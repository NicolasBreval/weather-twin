
async function checkStatus() {
  const response = await fetch('/actuator/health/liveness');

  const success = document.querySelector('.tooltip-status-success');
  const error = document.querySelector('.tooltip-status-error');

  if (!response.ok) {
    success.classList.add('hidden');
    error.classList.remove('hidden');
  } else {
    content = await response.json();
    if (content.status === "UP") {
      error.classList.add('hidden');
      success.classList.remove('hidden');
    } else {
      success.classList.add('hidden');
      error.classList.remove('hidden');
    }
  }

  setTimeout(() => checkStatus(), 5000);
}

document.addEventListener('DOMContentLoaded', () => {
  checkStatus();
})