package com.example.pizzaapp.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.pizzaapp.enums.ProductSize;
import com.example.pizzaapp.models.Product;
import com.example.pizzaapp.models.ProductCategory;
import com.example.pizzaapp.models.ProductOption;
import com.example.pizzaapp.models.Topping;
import com.example.pizzaapp.repositories.ProductCategoryRepository;
import com.example.pizzaapp.repositories.ProductOptionRepository;
import com.example.pizzaapp.repositories.ProductRepository;
import com.example.pizzaapp.repositories.ToppingRepository;

@Component
public class ProductDataLoader implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(ProductDataLoader.class);

        private final ProductRepository productRepository;
        private final ProductCategoryRepository categoryRepository;
        private final ProductOptionRepository optionRepository;
        private final ToppingRepository toppingRepository;

        public ProductDataLoader(ProductRepository productRepository,
                        ProductCategoryRepository categoryRepository,
                        ProductOptionRepository optionRepository,
                        ToppingRepository toppingRepository) {
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
                this.optionRepository = optionRepository;
                this.toppingRepository = toppingRepository;
        }

        @Override
        public void run(String... args) {
                if (productRepository.count() > 0 || toppingRepository.count() > 0) {
                        logger.info("Product and topping data already exists. Skipping initialization.");
                        return;
                }

                try {
                        ProductCategory pizzaCategory = new ProductCategory("Pizza");
                        categoryRepository.save(pizzaCategory);

                        List<Topping> toppings = createAndSaveToppings(List.of(
                                        new Topping("Extra Cheese", 8.0, "/assets/images/toppings/cheese.jpg"),
                                        new Topping("Olives", 6.5, "/assets/images/toppings/olives.jpg"),
                                        new Topping("Mushrooms", 7.0, "/assets/images/toppings/mushrooms.jpg"),
                                        new Topping("Pepperoni", 9.0, "/assets/images/toppings/pepperoni.jpg"),
                                        new Topping("Sausage", 9.0, "/assets/images/toppings/sausage.jpg"),
                                        new Topping("Onions", 5.0, "/assets/images/toppings/onion.webp"),
                                        new Topping("Green Peppers", 5.5, "/assets/images/toppings/greenPeppers.jpg"),
                                        new Topping("Pineapple", 7.0, "/assets/images/toppings/pineapple.jpg"),
                                        new Topping("Jalapeños", 6.0, "/assets/images/toppings/jalapeno.jpg"),
                                        new Topping("Bacon", 9.5, "/assets/images/toppings/bacon.jpg"),
                                        new Topping("Spinach", 6.5, "/assets/images/toppings/spinach.jpg"),
                                        new Topping("Tomato Slices", 6.0, "/assets/images/toppings/tomatoes.jpg"),
                                        new Topping("Garlic", 5.0, "/assets/images/toppings/garlic.jpg"),
                                        new Topping("Basil", 5.0, "/assets/images/toppings/basil.jpg"),
                                        new Topping("Anchovies", 9.0, "/assets/images/toppings/anchovies.jpg"),
                                        new Topping("Chicken", 9.5, "/assets/images/toppings/chicken.webp"),
                                        new Topping("Ham", 9.0, "/assets/images/toppings/ham.avif"),
                                        new Topping("Corn", 5.5, "/assets/images/toppings/corn.jpg"),
                                        new Topping("Artichoke Hearts", 8.0, "/assets/images/toppings/artichokes.jpg"),
                                        new Topping("Feta Cheese", 8.5, "/assets/images/toppings/feta.avif")));

                        Map<String, Map<ProductSize, Double>> priceMap = Map.of(
                                        "Margherita",
                                        Map.of(ProductSize.SMALL, 35.0, ProductSize.MEDIUM, 50.0, ProductSize.LARGE,
                                                        66.0),
                                        "Pepperoni",
                                        Map.of(ProductSize.SMALL, 38.0, ProductSize.MEDIUM, 55.0, ProductSize.LARGE,
                                                        62.0),
                                        "Hawaiian",
                                        Map.of(ProductSize.SMALL, 39.0, ProductSize.MEDIUM, 56.0, ProductSize.LARGE,
                                                        83.0),
                                        "Veggie Supreme",
                                        Map.of(ProductSize.SMALL, 37.0, ProductSize.MEDIUM, 53.0, ProductSize.LARGE,
                                                        60.0),
                                        "BBQ Chicken",
                                        Map.of(ProductSize.SMALL, 30.0, ProductSize.MEDIUM, 67.0, ProductSize.LARGE,
                                                        95.0),
                                        "Four Cheese",
                                        Map.of(ProductSize.SMALL, 29.0, ProductSize.MEDIUM, 46.0, ProductSize.LARGE,
                                                        74.0),
                                        "Meat Lovers",
                                        Map.of(ProductSize.SMALL, 32.0, ProductSize.MEDIUM, 50.0, ProductSize.LARGE,
                                                        88.0),
                                        "Mediterranean Delight",
                                        Map.of(ProductSize.SMALL, 41.0, ProductSize.MEDIUM, 57.0, ProductSize.LARGE,
                                                        64.0),
                                        "Spicy Jalapeño Chicken",
                                        Map.of(ProductSize.SMALL, 31.0, ProductSize.MEDIUM, 48.0, ProductSize.LARGE,
                                                        66.0),
                                        "White Pizza", Map.of(ProductSize.SMALL, 28.0, ProductSize.MEDIUM, 38.0,
                                                        ProductSize.LARGE, 50.0));

                        List<Product> products = List.of(
                                        new Product("Margherita", "Classic cheese pizza", pizzaCategory,
                                                        "/assets/images/pizzas/margherita.png",
                                                        Set.of(findTopping(toppings, "Extra Cheese"),
                                                                        findTopping(toppings, "Basil"))),
                                        new Product("Pepperoni", "Loaded with spicy pepperoni and melted cheese",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/pepperoni.jpg",
                                                        Set.of(findTopping(toppings, "Extra Cheese"),
                                                                        findTopping(toppings, "Pepperoni"))),
                                        new Product("Hawaiian", "A sweet and savory combination of pineapple and ham",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/hawaiian.webp",
                                                        Set.of(findTopping(toppings, "Pineapple"),
                                                                        findTopping(toppings, "Ham"),
                                                                        findTopping(toppings, "Extra Cheese"))),
                                        new Product("Veggie Supreme", "A medley of fresh vegetables with melted cheese",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/veggie-supreme.jpeg",
                                                        Set.of(findTopping(toppings, "Olives"),
                                                                        findTopping(toppings, "Onions"),
                                                                        findTopping(toppings, "Mushrooms"),
                                                                        findTopping(toppings, "Green Peppers"),
                                                                        findTopping(toppings, "Tomato Slices"),
                                                                        findTopping(toppings, "Extra Cheese"))),
                                        new Product("BBQ Chicken", "Smoky barbecue sauce base with chicken and onions",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/chicken-mushroom-pizza.jpg",
                                                        Set.of(findTopping(toppings, "Chicken"),
                                                                        findTopping(toppings, "Onions"),
                                                                        findTopping(toppings, "Garlic"))),
                                        new Product("Four Cheese", "A rich blend of cheeses for pure indulgence",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/four-cheese-pizza.avif",
                                                        Set.of(findTopping(toppings, "Extra Cheese"),
                                                                        findTopping(toppings, "Feta Cheese"))),
                                        new Product("Meat Lovers", "Hearty mix of meats for serious carnivores",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/meat-lovers.png",
                                                        Set.of(findTopping(toppings, "Pepperoni"),
                                                                        findTopping(toppings, "Bacon"),
                                                                        findTopping(toppings, "Ham"),
                                                                        findTopping(toppings, "Sausage"),
                                                                        findTopping(toppings, "Anchovies"))),
                                        new Product("Mediterranean Delight",
                                                        "Zesty pizza with feta, olives, and artichokes", pizzaCategory,
                                                        "/assets/images/pizzas/mediterranean-delight.jpg",
                                                        Set.of(findTopping(toppings, "Feta Cheese"),
                                                                        findTopping(toppings, "Olives"),
                                                                        findTopping(toppings, "Artichoke Hearts"),
                                                                        findTopping(toppings, "Garlic"),
                                                                        findTopping(toppings, "Corn"))),
                                        new Product("Spicy Jalapeño Chicken",
                                                        "Heat meets meat – fiery jalapeños on grilled chicken",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/chicken-jalapeno-pizza.avif",
                                                        Set.of(findTopping(toppings, "Chicken"),
                                                                        findTopping(toppings, "Jalapeños"),
                                                                        findTopping(toppings, "Onions"),
                                                                        findTopping(toppings, "Corn"))),
                                        new Product("White Pizza",
                                                        "No tomato sauce – just garlic, cheese, and rich creaminess",
                                                        pizzaCategory,
                                                        "/assets/images/pizzas/white-pizza.jpg",
                                                        Set.of(findTopping(toppings, "Extra Cheese"),
                                                                        findTopping(toppings, "Spinach"),
                                                                        findTopping(toppings, "Garlic"))));

                        createAndSaveProducts(products, priceMap);

                        logger.info("Sample product and options initialized successfully.");
                } catch (Exception e) {
                        logger.error("Error initializing sample product data", e);
                }
        }

        private List<Topping> createAndSaveToppings(List<Topping> toppings) {
                return toppings.stream().map(toppingRepository::save).collect(Collectors.toList());
        }

        private void createAndSaveProducts(List<Product> products, Map<String, Map<ProductSize, Double>> priceMap) {
                for (Product product : products) {
                        productRepository.save(product);
                        Map<ProductSize, Double> sizePrices = priceMap.get(product.getName());
                        if (sizePrices != null) {
                                createAndSaveProductOptions(product, sizePrices);
                        } else {
                                logger.warn("Missing pricing for product: {}", product.getName());
                        }
                }
        }

        private void createAndSaveProductOptions(Product product, Map<ProductSize, Double> sizePriceMap) {
                sizePriceMap.forEach((size, price) -> optionRepository.save(new ProductOption(product, size, price)));
        }

        private Topping findTopping(List<Topping> toppings, String name) {
                return toppings.stream()
                                .filter(t -> t.getName().equals(name))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Topping not found: " + name));
        }
}
