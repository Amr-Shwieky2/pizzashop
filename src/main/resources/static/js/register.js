import { setupFormValidation } from "./formValidation.js";

setupFormValidation({
  formId: "registerForm",
  nameId: "username",
  emailId: "formEmail",
  passwordId: "formPassword",
  confirmPasswordId: "confirmPassword",
  submitButtonId: "registerButton",
  mode: "register",
});
