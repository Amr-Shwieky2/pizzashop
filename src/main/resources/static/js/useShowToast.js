export const useShowToast = () => {
  function showToast(message, type = "success") {
    const container = document.getElementById("toast-container");

    const toastEl = document.createElement("div");
    toastEl.className = `toast show align-items-center text-white bg-${type} border-0 shadow rounded-3 mb-3 px-3 py-2`;
    toastEl.setAttribute("role", "alert");
    toastEl.setAttribute("aria-live", "assertive");
    toastEl.setAttribute("aria-atomic", "true");

    toastEl.innerHTML = `
        <div class="d-flex align-items-center">
            <div class="toast-body fw-semibold fs-6">
                ${message}
            </div>
            <button
                type="button"
                class="btn-close btn-close-white ms-auto"
                data-bs-dismiss="toast"
                aria-label="Close">
            </button>
        </div>
  
        `;

    container.appendChild(toastEl);

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();

    toastEl.addEventListener("hidden.bs.toast", () => toastEl.remove());
  }

  return { showToast };
};
