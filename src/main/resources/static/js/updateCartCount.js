export function updateCartCount() {
  const badge = document.getElementById("cart-count-badge");
  const cart = JSON.parse(localStorage.getItem("cart") || "[]");
  badge.textContent = cart.length;
}
