import { useLocalStorage } from "/js/useLocalStorage.js";

const { readValue } = useLocalStorage("cart", []);

function renderOrderSummary() {
  const cart = readValue() || [];

  if (!cart || cart.length === 0) {
    window.location.href = "/cart";
  }

  const tbody = document.getElementById("order-summary-body");

  if (!tbody) return;

  tbody.innerHTML = "";

  function formatOrderItem({ imageUrl, name, size, toppings = [], price }) {
    const toppingLabels = toppings.map((t) => t.label).join(", ");

    return `
        <tr>
            <th scope="row"><img
                    src=${imageUrl}
                    alt="product-img"
                    title="product-img"
                    class="avatar-md rounded"></th>
            <td>
                <h5
                    class="font-size-16 text-truncate"><a
                        href="#"
                        class="text-dark">${name}</a></h5>
                
                <p class="mb-1 text-muted">Size: ${size.toLowerCase()}</p>
                ${
                  toppingLabels
                    ? `<p class="mb-0 text-muted">${toppingLabels}</p>`
                    : ""
                }
            </td>
            <td>₪${price.toFixed(2)}</td>
        </tr>
      `;
  }

  let subtotal = 0;
  cart.forEach((item) => {
    subtotal += item.price ?? 0;
    const html = formatOrderItem({
      imageUrl: item.imageUrl,
      name: item.name,
      size: item.size.label,
      toppings: item.toppings,
      price: item.price,
    });
    tbody.insertAdjacentHTML("beforeend", html);
  });

  const discount = 0;
  const delivery = 10;
  const tax = (subtotal - discount) * 0.17;
  const total = subtotal - discount + delivery + tax;

  const summary = `
        <tr><td colspan="2"><h6 class="font-size-14 m-0">Sub Total :</h6></td><td>₪${subtotal.toFixed(
          2
        )}</td></tr>
        <tr>
            <td colspan="2"><h6 class="font-size-14 m-0">Discount :</h6></td>
            <td><span class="text-bold">- </span>₪${discount.toFixed(2)}</td>
        </tr>
        <tr><td colspan="2"><h6 class="font-size-14 m-0">Delivery Charge :</h6></td><td>₪${delivery.toFixed(
          2
        )}</td></tr>
        <tr><td colspan="2"><h6 class="font-size-14 m-0">Estimated Tax :</h6></td><td>₪${tax.toFixed(
          2
        )}</td></tr>
        <tr class="bg-light"><td colspan="2"><h6 class="font-size-14 m-0">Total:</h6></td><td>₪${total.toFixed(
          2
        )}</td></tr>
      `;

  tbody.insertAdjacentHTML("beforeend", summary);

  //   // Optionally, update order ID with a dynamic value
  //   const orderIdEl = document.getElementById("order-id");
  //   if (orderIdEl) {
  //     orderIdEl.textContent = "#" + Math.floor(Math.random() * 900000 + 100000); // e.g., #123456
  //   }
}

document.addEventListener("DOMContentLoaded", renderOrderSummary);
