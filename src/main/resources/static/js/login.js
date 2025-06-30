import { setupFormValidation } from "./formValidation.js";

setupFormValidation({
  formId: "loginForm",
  emailId: "formEmail",
  passwordId: "formPassword",
  submitButtonId: "loginButton",
  mode: "login",
});
