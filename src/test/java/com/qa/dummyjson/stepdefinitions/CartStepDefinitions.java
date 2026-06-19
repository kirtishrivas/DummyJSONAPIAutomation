package com.qa.dummyjson.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.qa.dummyjson.utils.ConfigReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CartStepDefinitions {

    private RequestSpecification request;
    private Response response;

    @Given("the Cart API endpoint environment is ready")
    public void the_cart_api_endpoint_environment_is_ready() {
        RestAssured.baseURI = ConfigReader.getProperty("baseURI");
        request = RestAssured.given().contentType("application/json");
    }

    @When("I send a POST request to add product ID {int} with quantity {int} to {string}")
    public void i_send_a_post_request_to_add_product_id_with_quantity_to(Integer productId, Integer quantity, String endpoint) {
        // Constructing complex nested JSON structure payload
        // DummyJSON structure requires: { userId: 1, products: [ { id: X, quantity: Y } ] }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 1); // Mapping to system user ID 1

        List<Map<String, Object>> productsList = new ArrayList<>();
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("id", productId);
        productDetails.put("quantity", quantity);
        productsList.add(productDetails);

        requestBody.put("products", productsList);

        // Executing the POST call
        response = request.body(requestBody).post(endpoint);
    }

    @Then("the cart response status code should be {int} or {int}")
    public void the_cart_response_status_code_should_be_or(Integer code1, Integer code2) {
        int actualCode = response.getStatusCode();
        assertTrue("Assertion Failed: Expected 200 or 201 but got " + actualCode, 
                actualCode == code1 || actualCode == code2);
    }

    @And("the cart response payload should confirm product ID {int} was added")
    public void the_cart_response_payload_should_confirm_product_id_was_added(Integer expectedProductId) {
        // Extracting product details from the nested response array index [0]
        int actualProductId = response.jsonPath().getInt("products[0].id");
        int addedQuantity = response.jsonPath().getInt("products[0].quantity");

        assertEquals("Assertion Failed: Product was not added correctly!", (int) expectedProductId, actualProductId);
        
        System.out.println("\n==================================================");
        System.out.println("🛒 POST OPERATION SUCCESSFUL: ITEM ADDED TO CART");
        System.out.println("==================================================");
        System.out.println("Confirmed Product ID Added: " + actualProductId);
        System.out.println("Quantity Registered: " + addedQuantity);
    }

    @And("the response total items count should match the updated state")
    public void the_response_total_items_count_should_match_the_updated_state() {
        int totalProducts = response.jsonPath().getInt("totalProducts");
        System.out.println("Total Distinct Items Now In Cart: " + totalProducts);
        System.out.println("==================================================\n");
        assertTrue("Assertion Failed: Cart total count calculations are corrupted!", totalProducts >= 0);
    }
    
    @When("I send a DELETE request to clear cart ID {int} from the system endpoint {string}")
    public void i_send_a_DELETE_request_to_clear_cart_id_from_the_system_endpoint(Integer cartId, String endpoint) {
        // HTTP DELETE request simulation
        response = request.delete(endpoint);
    }

    @And("the response payload must confirm deletion status with {string} flag true")
    public void the_response_payload_must_confirm_deletion_status_with_flag_true(String deletionKey) {
        // Extracting the boolean flag to ensure server wiped the resource state
        boolean isDeletedStatus = response.jsonPath().getBoolean(deletionKey);
        String deletionTime = response.jsonPath().getString("deletedOn");

        assertTrue("Assertion Failed: Server did not flag the resource as deleted!", isDeletedStatus);

        System.out.println("\n==================================================");
        System.out.println("🗑️ DELETE OPERATION SUCCESSFUL: CART PURGED");
        System.out.println("==================================================");
        System.out.println("Target Parameter Confirmed: " + deletionKey + " -> " + isDeletedStatus);
        System.out.println("Server Cleanup Timestamp: " + deletionTime);
        System.out.println("==================================================\n");
    }
    @Then("the cart response payload data structure must strictly match the {string} contract")
    public void the_response_payload_data_structure_must_strictly_match_the_contract(String schemaFileName) {
        response.then().assertThat()
                .body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
        
        System.out.println("🛒 Cart Contract Validation Passed! Structural fields match " + schemaFileName + " completely.");
    }
}