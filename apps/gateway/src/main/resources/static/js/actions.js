

document.addEventListener('DOMContentLoaded', () => {

  document.querySelector("#add-sensor-btn").addEventListener("click", () => {
    document.querySelector("#form-dialog").showModal();
  });

  document.querySelector("#modal-close").addEventListener("click", () => {
    const dialog = document.querySelector("#form-dialog");
    dialog.close();

    dialog.querySelectorAll("input").forEach((input) => input.value = "");
    dialog.querySelectorAll("textarea").forEach((textarea) => textarea.value = "");
    dialog.querySelectorAll("select").forEach((select) => select.value = "");
    document.querySelectorAll('#form-intervals [id^="add-interval-"]').forEach((interval) => interval.remove());
    dialog.querySelector("form").reset();
  });

  document.querySelector("#add-interval-btn").addEventListener("click", async () => {
    const intervalList = document.querySelector("#form-intervals");
    const id = intervalList.querySelectorAll("input").length + 1;

    const response = await fetch(`/form/interval/add?id=${id}`);
    const fragment = await response.text();

    intervalList.insertAdjacentHTML('afterbegin', fragment);

    const removeBtn = document.querySelector(`#add-interval-${id} button`);
    removeBtn.addEventListener("click", () => {
      document.querySelector(`#add-interval-${id}`).remove();
    });
  });

  document.querySelector("#form-submit").addEventListener("click", () => {
    fetch('/registration', {
      method: 'POST',
      body: JSON.stringify({
        "device": "",
        "sensor": "",
        "aggregationExpression": "",
        "flushExpression": "",
        "defaultValue": "",
        "intervals": [],
        "sensorType": "",
        "magnitude": "",
        "description": ""
      })
    })
  });
})