@DummyJSON @Products
Feature: DummyJSON Product Catalog Operations
  As an API consumer
  I want to query the product database
  So that I can search, filter, and view product information dynamically

  @Smoke @Positive
  Scenario: Retrieve all products successfully
    When I send a GET request to the fetch all products endpoint "/products"
    Then the product response status code should be 200
    And the response should contain a list of products with a total count greater than 0

  @Positive
  Scenario: Search for a specific product using query parameters
    Given I want to search for products containing "Laptop"
    When I send a GET request to the product search endpoint
    Then the product response status code should be 200
    And the first product title should contain "Laptop"
    
    @Smoke @Positive @Update
  Scenario: Successfully update specific attributes of an existing product using PATCH
    Given I have configured a PATCH request for product ID 1
    When I send the partial update request to "/products/1" with a modified title "Premium Apple MacBook"
    Then the product response status code should be 200
    And the product field "title" should explicitly reflect the updated value "Premium Apple MacBook"