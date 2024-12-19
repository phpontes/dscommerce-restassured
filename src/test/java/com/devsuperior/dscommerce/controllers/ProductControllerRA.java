package com.devsuperior.dscommerce.controllers;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductControllerRA {

	private Long existingProductId, nonExistingProductId;
	private String productName;
	
	@BeforeEach
	private void setUp() {
		baseURI = "http://localhost:8080";
		
		productName = "Macbook";
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() {
		existingProductId = 2L;
		
		given()
			.get("/products/{id}", existingProductId)
			.then()
				.statusCode(200)
				.body("id", is(2))
				.body("name", equalTo("Smart TV"))
				.body("imgUrl", equalTo("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg"))
				.body("price", is(2190.0F))
				.body("categories.id", hasItems(2,3))
				.body("categories.name", hasItems("Eletrônicos", "Computadores"));
	}
	
	@Test
	public void findAllShouldReturnPageProductsWhenProductNameIsEmpty() {
		
		given()
			.get("/products?page=0")
			.then()
				.statusCode(200)
				.body("content.name", hasItems("Macbook Pro", "PC Gamer Tera"));
	}
	
	@Test
	public void findAllShouldReturnPageProductsWhenProductNameIsNotEmpty() {
		
		given()
			.get("/products?name={productName}", productName)
			.then()
				.statusCode(200)
				.body("content.id[0]", is(3))
				.body("content.name[0]", equalTo("Macbook Pro"))
				.body("content.price[0]", is(1250.0F))
				.body("content.imgUrl[0]", equalTo("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
	}
	
	@Test
	public void findAllShouldReturnPagedProductsWithPriceGreaterThan2000() {
		
		given()
			.get("/products?size=25")
			.then()
				.statusCode(200)
				.body("content.findAll { it.price > 2000 }.name", hasItems("Smart TV", "PC Gamer Weed"));
	}
}
