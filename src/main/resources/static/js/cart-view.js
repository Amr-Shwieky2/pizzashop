import { useLocalStorage } from "/js/useLocalStorage.js";
import { updateCartCount } from "./updateCartCount.js";

const { readValue, writeValue } = useLocalStorage("cart", []);

const cartContainer = document.getElementById("cart-items-container");
const cartCountBadge = document.getElementById("cart-items-count");
const emptyState = document.querySelector(".blankslate");

function formatCartItem({ imageUrl, name, size, toppings = [], price }, index) {
  const toppingLabels = toppings.map((t) => t.label).join(", ");

  return `
      <div class="card rounded-3 mb-3" data-cart-index="${index}">
        <div class="card-body">
          <div class="row align-items-center g-3">
            <!-- 🗑️ Trash icon + image -->
            <div class="col-1 d-flex justify-content-center">
              <a href="#" class="text-danger delete-item-btn" data-index="${index}">
                <i class="fas fa-trash fa-lg"></i>
              </a>
            </div>
            <div class="col-3 col-md-2">
              <img src="${imageUrl}" class="img-fluid rounded-3" alt="${name}" style="max-height: 80px;" />
            </div>
            <div class="col-6 col-md-6">
              <div class="d-flex flex-column">
                <p class="fw-bold mb-1">${name}</p>
                <p class="mb-1 text-muted">Size: ${size.toLowerCase()}</p>
                ${
                  toppingLabels
                    ? `<p class="mb-0 text-muted">Toppings: ${toppingLabels}</p>`
                    : ""
                }
              </div>
            </div>
            <div class="col-2 d-flex justify-content-end">
              <h5 class="mb-0 text-nowrap">₪${price.toFixed(2)}</h5>
            </div>
          </div>
        </div>
      </div>
    `;
}

async function loadCartItems() {
  const cart = readValue();

  if (cart.length === 0) {
    emptyState?.classList.remove("d-none");
    cartContainer?.classList.add("d-none");
    return;
  }

  emptyState?.classList.add("d-none");
  cartContainer?.classList.remove("d-none");

  if (cartCountBadge)
    cartCountBadge.textContent = `${cart.length} ${
      cart.length > 1 ? "items" : "item"
    }`;

  const cartItemsContainer = document.getElementById("cart-items");
  const cartTotalPriceElement = document.getElementById("cart-total-price");

  cartItemsContainer.innerHTML = "";

  let totalPrice = 0;
  cart.forEach((item, index) => {
    totalPrice += item.price ?? 0;
    const html = formatCartItem(
      {
        imageUrl: item.imageUrl,
        name: item.name,
        size: item.size.label,
        toppings: item.toppings,
        price: item.price,
      },
      index
    );
    cartItemsContainer.insertAdjacentHTML("beforeend", html);
  });

  if (cartTotalPriceElement) {
    cartTotalPriceElement.textContent = `₪${totalPrice.toFixed(2)}`;
  }

  function removeItemFromCart(index) {
    const cart = readValue();
    cart.splice(index, 1);
    writeValue("cart", cart);
    loadCartItems();
    updateCartCount();
  }

  document.querySelectorAll(".delete-item-btn").forEach((btn) => {
    btn.addEventListener("click", function (e) {
      e.preventDefault();
      const index = parseInt(this.getAttribute("data-index"));
      removeItemFromCart(index);
    });
  });
}

document.addEventListener("DOMContentLoaded", loadCartItems);
