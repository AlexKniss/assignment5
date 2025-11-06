package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {

	private Database database;
	private ShoppingCartAdaptor cart;
	private Amazon amazon;

	@BeforeEach
	void setUp() {
		database = new Database();
		database.resetDatabase();
		cart = new ShoppingCartAdaptor(database);
	}

	@AfterEach
	void tearDown() {
		if (database != null)
            database.close();
	}

	@Test
	@DisplayName("specification-based")
	void calculateIntegration() {
		// empty rules
		Amazon emptyRules = new Amazon(cart, Collections.emptyList());
		assertEquals(0.0, emptyRules.calculate(), 0.001);

		// non-empty rules using real RegularCost
		Item item = new Item(ItemType.OTHER, "Book", 1, 9.99);
		Amazon amazonWithRegular = new Amazon(cart, Arrays.asList(new RegularCost()));

		amazonWithRegular.addToCart(item);

		assertEquals(9.99, amazonWithRegular.calculate(), 0.01);

		List<Item> items = cart.getItems();
		assertEquals(1, items.size());
		Item persisted = items.getFirst();
		assertEquals(item.getName(), persisted.getName());
		assertEquals(item.getType(), persisted.getType());
		assertEquals(item.getQuantity(), persisted.getQuantity());
		assertEquals(item.getPricePerUnit(), persisted.getPricePerUnit(), 0.001);
	}

	@Test
	@DisplayName("structural-based")
	void addToCartStoresItemStructural() {
		// use RegularCost to compute
		amazon = new Amazon(cart, Arrays.asList(new RegularCost()));
		Item item = new Item(ItemType.ELECTRONIC, "Electronic", 2, 10.0);
		amazon.addToCart(item);

		List<Item> items = cart.getItems();
		assertEquals(1, items.size());
		assertEquals(20.0, amazon.calculate(), 0.01);
	}
}