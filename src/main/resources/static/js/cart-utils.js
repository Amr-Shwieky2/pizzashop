import { useShowToast } from "./useShowToast.js";
import { useLocalStorage } from "./useLocalStorage.js";
import { updateCartCount } from "./updateCartCount.js";

const { readValue, writeValue } = useLocalStorage("cart", []);
const { showToast } = useShowToast();

let activeProduct = null;

document.addEventListener("DOMContentLoaded", function () {
  const sizeOptions = document.getElementById("size-options");
  const toppingOptions = document.getElementById("topping-options");

  document.querySelectorAll('[data-bs-toggle="offcanvas"]').forEach((btn) => {
    btn.addEventListener("click", function () {
      const name = btn.getAttribute("data-name");
      const image = btn.getAttribute("data-image");
      const defaultSelectedSize = btn.getAttribute("data-default-size");
      const sizes = JSON.parse(btn.getAttribute("data-sizes") || "[]");
      const toppings = JSON.parse(btn.getAttribute("data-toppings") || "[]");

      activeProduct = {
        id: btn.getAttribute("data-id"),
        name: name,
        imageUrl: image,
      };

      document.getElementById(
        "offcanvasRightLabel"
      ).textContent = `Customize Your ${name} Pizza`;

      const sizeOrder = ["small", "medium", "large"];

      const sortedSizes = [...sizes].sort(
        (a, b) =>
          sizeOrder.indexOf(a.label.toLowerCase()) -
          sizeOrder.indexOf(b.label.toLowerCase())
      );

      // Render size radio buttons
      sizeOptions.innerHTML = sortedSizes
        .map(
          (s, i) => `
            <div class="col-4 d-flex justify-content-center">
                <label class="d-flex flex-column align-items-center w-100 p-2 text-center" style="cursor: pointer;">
                <input class="form-check-input d-none" type="radio" name="size" data-option-id="${
                  s.id
                }" id="size-${i}" value="${s.label}"
                ${
                  s.label.toLowerCase() === defaultSelectedSize?.toLowerCase()
                    ? "checked"
                    : ""
                }>
              

                    <div class="d-flex align-items-center justify-content-center pizza-img-wrapper" style="height: 130px;">
                        <img src="${image}" alt="${s.label}" 
                            style="width: ${getImageSize(
                              s.label
                            )}; transition: 0.3s;" 
                            class="pizza-size-img" />
                    </div>

                    <div class="fw-bold text-capitalize mt-2">${s.label.toLowerCase()}</div>
                    <div class="text-muted">₪${s.price.toFixed(2)}</div>
                </label>
            </div>
        `
        )
        .join("");

      // Helper to decide image size
      function getImageSize(sizeLabel) {
        switch (sizeLabel.toLowerCase()) {
          case "small":
            return "60px";
          case "medium":
            return "90px";
          case "large":
            return "120px";
          default:
            return "90px";
        }
      }

      function updateTotalPrice() {
        const selectedSize = document.querySelector(
          'input[name="size"]:checked'
        );
        const selectedToppings = document.querySelectorAll(
          "#topping-options input:checked"
        );

        let total = 0;

        if (selectedSize) {
          const priceText =
            selectedSize.closest("label").querySelector(".text-muted")
              ?.textContent || "";
          const match = priceText.match(/₪([\d.]+)/);
          if (match) total += parseFloat(match[1]);
        }

        selectedToppings.forEach((t) => {
          const price = parseFloat(t.dataset.price);
          if (!isNaN(price)) total += price;
        });

        document.getElementById("total-price").textContent = `₪${total.toFixed(
          2
        )}`;
      }

      sizeOptions.querySelectorAll('input[name="size"]').forEach((radio) => {
        radio.addEventListener("change", updateTotalPrice);
      });

      // Render topping checkboxes
      toppingOptions.innerHTML = toppings
        .map(
          (t) => `
            <div class="form-check d-flex align-items-center gap-2 mb-2">
                <input class="form-check-input" type="checkbox" id="${t.id}" value="${t.label}" data-price="${t.price}">
                <label class="form-check-label d-flex align-items-center" for="${t.id}">
                <img src="${t.imageUrl}" alt="${t.label}" style="width: 30px; height: 30px; object-fit: cover; margin-right: 8px;" />
                ${t.label} - ₪${t.price}
                </label>
            </div>
        `
        )
        .join("");

      // Bind change events to topping checkboxes
      toppingOptions
        .querySelectorAll('input[type="checkbox"]')
        .forEach((cb) => {
          cb.addEventListener("change", updateTotalPrice);
        });

      // Initial total calculation
      updateTotalPrice();
    });
  });

  document
    .getElementById("confirm-add-to-cart")
    .addEventListener("click", function () {
      const selectedSizeInput = document.querySelector(
        'input[name="size"]:checked'
      );
      const selectedToppingsInputs = document.querySelectorAll(
        "#topping-options input:checked"
      );

      const productId = activeProduct.id;
      const productOptionId = selectedSizeInput.getAttribute("data-option-id");

      const selectedSize = {
        id: productOptionId,
        label: selectedSizeInput.value,
      };

      const selectedToppings = Array.from(selectedToppingsInputs).map((cb) => ({
        id: cb.id,
        label: cb.value,
      }));

      const totalPrice = parseFloat(
        document.getElementById("total-price").textContent.replace("₪", "")
      );

      const cartItem = {
        productId,
        productOptionId,
        size: selectedSize,
        toppings: selectedToppings,
        name: activeProduct.name,
        imageUrl: activeProduct.imageUrl,
        price: totalPrice,
        addedAt: Date.now(),
      };

      const cart = readValue();
      cart.push(cartItem);
      writeValue("cart", cart);

      updateCartCount();

      bootstrap.Offcanvas.getInstance(
        document.getElementById("offcanvasRight")
      )?.hide();

      showToast("Pizza added to cart!", "success");
    });
});
