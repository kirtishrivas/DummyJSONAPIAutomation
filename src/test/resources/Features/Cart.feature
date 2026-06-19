@DummyJSON @Cart
Feature: DummyJSON Shopping Cart Operations
  As an authenticated customer
  I want to manage my shopping cart items
  So that I can prepare an order for checkout

  @Smoke @Positive
  Scenario: Successfully add a product to user cart
    Given the Cart API endpoint environment is ready
    When I send a POST request to add product ID 15 with quantity 2 to "/carts/add"
    Then the cart response status code should be 201 or 200
    And the cart response payload data structure must strictly match the "cart-schema.json" contract
    And the cart response payload should confirm product ID 15 was added
    And the response total items count should match the updated state
    
    @Smoke @Negative @Cleanup
  Scenario: Successfully delete an existing shopping cart resource
    Given the Cart API endpoint environment is ready
    When I send a DELETE request to clear cart ID 1 from the system endpoint "/carts/1"
    Then the cart response status code should be 201 or 200
    And the response payload must confirm deletion status with "isDeleted" flag true