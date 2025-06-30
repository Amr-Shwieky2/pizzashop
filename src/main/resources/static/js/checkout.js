document.addEventListener("DOMContentLoaded", function () {
  const newAddressRadio = document.getElementById("new-address");
  const newAddressForm = document.getElementById("newAddressForm");

  if (newAddressRadio && newAddressForm) {
    newAddressRadio.addEventListener("change", function () {
      const collapse = new bootstrap.Collapse(newAddressForm, {
        toggle: false,
      });

      if (newAddressRadio.checked) {
        collapse.show();
      }
    });
  }

  // Optionally hide the form if other address is selected
  const addressRadios = document.querySelectorAll(
    "input[name='selectedAddress']"
  );
  addressRadios.forEach((radio) => {
    radio.addEventListener("change", () => {
      if (radio.value !== "new" && newAddressForm.classList.contains("show")) {
        bootstrap.Collapse.getInstance(newAddressForm)?.hide();
      }
    });
  });
});
