
async function showAlert(type, message, timeout) {
  try {
    const response = await fetch(`/toast?type=${type}&message=${message}`);

    if (response.ok) {
      const fragment = document.createRange().createContextualFragment(await response.text());
      const element = fragment.firstElementChild;

      document.querySelector(".toast").prepend(element);

      setTimeout(() => element.remove(), timeout || 5000);
    } else {
      console.error("Unable to obtain rendered toast");
    }
  } catch (e) {
    console.error("Error during toast creation", e);
  }
}

window.showAlert = showAlert;