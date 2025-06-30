import { useLocalStorage } from "./useLocalStorage.js";

const { readValue } = useLocalStorage("cart", []);

document.addEventListener("DOMContentLoaded", () => {
  const cart = readValue() || [];

  const proceedLink = document.getElementById("proceedLink");
  const nameInput = document.getElementById("billing-name");
  const phoneInput = document.getElementById("billing-phone");
  const cartJsonInput = document.getElementById("cartJsonInput");

  const nameError = document.getElementById("name-error");
  const phoneError = document.getElementById("phone-error");

  const inputSelectors = [
    "#billing-name",
    "#billing-phone",
    "input[name='selectedAddress']:checked",
    "input[name='pay-method']:checked",
  ];

  function isFilled(selector) {
    if (selector.includes(":checked")) {
      return document.querySelector(selector) !== null;
    }
    const el = document.querySelector(selector);
    return el && el.value.trim() !== "";
  }

  function validateForm() {
    const nameVal = nameInput.value.trim();
    const phoneVal = phoneInput.value.trim();

    const nameValid = /^[A-Za-z\s]+$/.test(nameVal);
    const phoneValid = /^05\d{8}$/.test(phoneVal);
    const allFilled = inputSelectors.every(isFilled);
    const allValid = allFilled && nameValid && phoneValid;

    // Show or hide error messages
    nameError.classList.toggle("d-none", nameValid || nameVal === "");
    phoneError.classList.toggle("d-none", phoneValid || phoneVal === "");

    // Enable/disable Proceed button
    if (proceedLink) {
      proceedLink.classList.toggle("disabled", !allValid);
      proceedLink.setAttribute("aria-disabled", !allValid);
    }
  }

  inputSelectors.forEach((selector) => {
    const elements = document.querySelectorAll(
      selector.replace(":checked", "")
    );
    elements.forEach((el) => {
      el.addEventListener("input", validateForm);
      el.addEventListener("change", validateForm);
    });
  });

  if (proceedLink) {
    proceedLink.addEventListener("click", function (e) {
      if (proceedLink.classList.contains("disabled")) {
        e.preventDefault();
      } else {
        const minimalCart = cart.map((item) => ({
          productId: item.productId,
          size: item.size.label,
          toppingIds: item.toppings.map((t) => t.id),
        }));
        cartJsonInput.value = JSON.stringify(minimalCart);
        document.getElementById("checkoutForm").submit();
      }
    });
  }

  validateForm();
});
