export function setupFormValidation(config) {
  document.addEventListener("DOMContentLoaded", () => {
    const {
      formId,
      emailId,
      passwordId,
      confirmPasswordId,
      nameId,
      submitButtonId,
      mode,
    } = config;

    const form = document.getElementById(formId);

    if (!form) {
      console.error(`Form with ID ${formId} not found.`);
      return;
    }

    const emailInput = document.getElementById(emailId);
    const passwordInput = document.getElementById(passwordId);
    const confirmPasswordInput = confirmPasswordId
      ? document.getElementById(confirmPasswordId)
      : null;
    const nameInput = nameId ? document.getElementById(nameId) : null;
    const submitButton = document.getElementById(submitButtonId);

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    form.addEventListener("submit", function (event) {
      event.preventDefault();
      if (validate()) {
        form.submit();
      }
    });

    [emailInput, passwordInput, confirmPasswordInput, nameInput].forEach(
      (el) => {
        if (el) {
          el.addEventListener("input", checkValidity);
          el.addEventListener("change", checkValidity);
        }
      }
    );

    checkValidity();

    function checkValidity() {
      const isEmailValid = emailRegex.test(emailInput.value.trim());
      const isPasswordValid = passwordInput.value.trim().length >= 3;
      const isNameValid = nameInput ? nameInput.value.trim().length > 0 : true;
      const isConfirmPasswordValid = confirmPasswordInput
        ? passwordInput.value === confirmPasswordInput.value
        : true;

      const isValid =
        isEmailValid &&
        isPasswordValid &&
        isNameValid &&
        isConfirmPasswordValid;

      submitButton.disabled = !isValid;
    }

    function validate() {
      let isValid = true;

      if (!emailRegex.test(emailInput.value.trim())) {
        emailInput.classList.add("is-invalid");
        isValid = false;
      } else {
        emailInput.classList.remove("is-invalid");
      }

      if (passwordInput.value.length < 3) {
        passwordInput.classList.add("is-invalid");
        isValid = false;
      } else {
        passwordInput.classList.remove("is-invalid");
      }

      if (confirmPasswordInput) {
        if (passwordInput.value !== confirmPasswordInput.value) {
          confirmPasswordInput.classList.add("is-invalid");
          isValid = false;
        } else {
          confirmPasswordInput.classList.remove("is-invalid");
        }
      }

      if (nameInput && nameInput.value.trim() === "") {
        nameInput.classList.add("is-invalid");
        isValid = false;
      } else if (nameInput) {
        nameInput.classList.remove("is-invalid");
      }

      return isValid;
    }
  });
}
