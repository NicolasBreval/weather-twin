var FORM_MODE = "ADD";

async function addIntervalInput(value) {
  const form = document.querySelector(".sensor-form-dialog form");
  const intervalList = form.querySelector(".form-intervals");
  const id = intervalList.querySelectorAll("input").length + 1;

  const response = await fetch("/interval");

  if (!response.ok) {
    console.error("Unable to obtain rendered interval input");
    return;
  }

  const fragment = document.createRange().createContextualFragment(await response.text());
  const element = fragment.firstElementChild;
  element.classList.add(`add-interval-${id}`);
  element.querySelector("input").value = value;

  intervalList.insertAdjacentElement('afterbegin', element);

  const removeBtn = form.querySelector(`.add-interval-${id} button`);
  removeBtn.addEventListener("click", () => {
    form.querySelector(`.add-interval-${id}`).remove();
  });
}

function clearIntervalInputs() {
  document.querySelectorAll(".form-intervals .add-interval").forEach((i) => i.remove());
}

function showFormDialog(formMode) {
  const dialog = document.querySelector(".sensor-form-dialog");

  if (formMode === "ADD") {
    dialog.querySelector(".form-device").removeAttribute("disabled")
    dialog.querySelector(".form-sensor").removeAttribute("disabled");
  } else {
    dialog.querySelector(".form-device").setAttribute("disabled", "");
    dialog.querySelector(".form-sensor").setAttribute("disabled", "");
  }

  const titleEl = dialog.querySelector("form .form-title");
  const submitEl = dialog.querySelector('form button[type="submit"]');

  titleEl.textContent = titleEl.getAttribute(`data-${formMode.toLowerCase()}-title`);
  submitEl.textContent = submitEl.getAttribute(`data-${formMode.toLowerCase()}-submit`);

  FORM_MODE = formMode;

  dialog.showModal();
}

document.addEventListener('DOMContentLoaded', () => {

  const dialog = document.querySelector(".sensor-form-dialog");

  //#region ADD SENSOR BTN

  document.querySelector(".add-sensor-btn").addEventListener("click", () => {
    clearIntervalInputs();
    showFormDialog("ADD");
  });

  //#endregion

  //#region EDIT SENSOR BTNS

  function handleEdit(btn) {
    const position = Array.from(btn.classList).slice(-1)[0].split("-").slice(-1)[0];
    const sensorEl = document.querySelector(`.sensor-${position}`);

    if (sensorEl) {
      const device = sensorEl.getAttribute("data-device");
      const sensor = sensorEl.getAttribute("data-sensor");
      const type = sensorEl.getAttribute("data-sensortype");
      const defaultValue = sensorEl.getAttribute("data-defaultvalue");
      const magnitude = sensorEl.getAttribute("data-magnitude");
      const description = sensorEl.getAttribute("data-description");
      const aggExpression = sensorEl.getAttribute("data-agg-expression");
      const flushExpression = sensorEl.getAttribute("data-flush-expression");
      const intervals = JSON.parse(sensorEl.getAttribute("data-intervals"));

      dialog.querySelector(".form-device").value = device;
      dialog.querySelector(".form-sensor").value = sensor;
      dialog.querySelector(".form-type").value = type;
      dialog.querySelector(".form-defaultvalue").value = defaultValue;
      dialog.querySelector(".form-magnitude").value = magnitude;
      dialog.querySelector(".form-description").value = description;
      dialog.querySelector(".form-agg-expression").value = aggExpression;
      dialog.querySelector(".form-flush-expression").value = flushExpression;

      clearIntervalInputs();
      Promise.all(intervals.map(interval => addIntervalInput(interval)))
        .then(() => showFormDialog("EDIT"));
    } else {
      console.error(`Error obtaining data for sensor at position ${position}`);
    }
  }

  document.querySelectorAll('button[class*="edit-sensor-"]').forEach((btn) => btn.addEventListener("click", () => handleEdit(btn)));

  //#endregion

  //#region DELETE SENSOR BTNS

  async function handleDelete(btn) {
    const position = Array.from(btn.classList).slice(-1)[0].split("-").slice(-1)[0];
    const sensorEl = document.querySelector(`.sensor-${position}`);

    if (sensorEl) {
      const device = sensorEl.getAttribute("data-device");
      const sensor = sensorEl.getAttribute("data-sensor");

      const response = await fetch(`registration/${device}/${sensor}`, { method: "DELETE" });

      if (response.ok) {
        document.querySelector(`div.sensor-${position}`).closest("li").remove();
        showAlert("success", "site.body.alert.sensor.deletion.ok")
      } else if (response.status === 404) {
        showAlert("error", "site.body.alert.sensor.deletion.not-exists")
      } else {
        showAlert("error", "site.body.alert.sensor.deletion.error")
      }
    } else {
      console.error(`Error obtaining data for sensor at position ${position}`);
    }
  }

  document.querySelectorAll('button[class*="delete-sensor-"]').forEach((btn) => btn.addEventListener("click", async () => handleDelete(btn)));

  //#endregion

  //#region MODAL CLOSE

  document.querySelector(".modal-close").addEventListener("click", (e) => {
    // Close dialog
    dialog.close();

    // Clear inputs, textareas and selects
    dialog.querySelectorAll("input").forEach((input) => input.value = "");
    dialog.querySelectorAll("textarea").forEach((textarea) => textarea.value = "");
    dialog.querySelectorAll("select").forEach((select) => select.value = "");

    // Remove added interval inputs
    document.querySelectorAll('div[class^="add-interval-"]').forEach((interval) => interval.remove());
  })

  //#endregion

  //#region FORM ADD INTERVAL

  dialog.querySelector(".add-interval-btn").addEventListener("click", () => addIntervalInput());

  //#endregion

  //#region FORM SUBMIT

  dialog.querySelector("form").addEventListener("submit", async (e) => {
    e.preventDefault();

    const isEditing = FORM_MODE !== "ADD";

    const device = dialog.querySelector(".form-device").value;
    const sensor = dialog.querySelector(".form-sensor").value;
    const sensorType = dialog.querySelector(".form-type").value;
    const defaultValue = dialog.querySelector(".form-defaultvalue").value;
    const magnitude = dialog.querySelector(".form-magnitude").value;
    const description = dialog.querySelector(".form-description").value;
    const aggExpression = dialog.querySelector(".form-agg-expression").value;
    const flushExpression = dialog.querySelector(".form-flush-expression").value;
    const intervals = Array.from(dialog.querySelectorAll('.add-interval input')).map(node => node.value);

    if (intervals.length === 0) {
      window.showAlert("error", "site.body.alert.sensor.registration.intervals-empty");
      return;
    }

    const response = await fetch('/registration', {
      method: isEditing ? 'POST' : 'PUT',
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        device: device,
        sensor: sensor,
        aggregationExpression: aggExpression,
        flushExpression: flushExpression,
        defaultValue: defaultValue,
        intervals,
        sensorType: sensorType,
        magnitude: magnitude,
        description: description
      })
    });


    // TODO: Put toasts for edit operation too
    if (!response.ok) {
      if (response.status === 400) {
        const responseText = await response.text();

        if (responseText.startsWith("Aggregation expression not valid")) {
          window.showAlert("error", isEditing ? "site.body.alert.sensor.edition.bad-agg-expression" : "site.body.alert.sensor.registration.bad-agg-expression");
        } else if (responseText.startsWith("Flush expression not valid")) {
          window.showAlert("error", isEditing ? "site.body.alert.sensor.edition.bad-flush-expression" : "site.body.alert.sensor.registration.bad-flush-expression");
        }
      } else if (!isEditing && response.status === 409) {
        window.showAlert("error", "site.body.alert.sensor.registration.already-exists");
      } else if (isEditing && response.status === 404) {
        window.showAlert("error", "site.body.alert.sensor.edition.not-exists");
      } else {
        window.showAlert("error", isEditing ? "site.body.alert.sensor.edition.error" : "site.body.alert.sensor.registration.error");
      }
    } else {
      window.showAlert("success", isEditing ? "site.body.alert.sensor.edition.ok" : "site.body.alert.sensor.registration.ok");
      const response = await fetch(`/sensor/items?device=${device}&sensor=${sensor}`)

      if (response.ok) {
        const sensorList = document.querySelector(".sensor-list");
        const renderedSensor = await response.text();

        if (!isEditing) {
          const fragment = document.createRange().createContextualFragment(renderedSensor);
          const editBtn = fragment.firstElementChild.querySelector('button[class*="edit-sensor-"]');
          const deleteBtn = fragment.firstElementChild.querySelector('button[class*="delete-sensor-"]');
          editBtn.addEventListener("click", () => handleEdit(editBtn));
          deleteBtn.addEventListener("click", () => handleEdit(deleteBtn));
          sensorList.append(fragment.firstElementChild);
        } else {
          document.querySelectorAll('.sensor-list li div[class*="sensor-"]').forEach((s) => {
            const attrDevice = s.getAttribute("data-device");
            const attrSensor = s.getAttribute("data-sensor");

            if (attrDevice === device && attrSensor === sensor) {
              const fragment = document.createRange().createContextualFragment(renderedSensor);
              const editBtn = fragment.firstElementChild.querySelector('button[class*="edit-sensor-"]');
              const deleteBtn = fragment.firstElementChild.querySelector('button[class*="delete-sensor-"]');
              editBtn.addEventListener("click", () => handleEdit(editBtn));
              deleteBtn.addEventListener("click", () => handleEdit(deleteBtn));
              s.parentNode.replaceWith(fragment);
            }
          });
        }
      } else {
        console.error(`Error obtaining rendered item for '${device}.${sensor}'`);
      }
    }
  });

  //#endregion

})