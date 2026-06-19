@DummyJSON @Authentication
Feature: DummyJSON User Session Authentication
  As an API client automation service
  I want to log into the DummyJSON platform with valid user credentials
  So that I can receive a secure access token to perform user-restricted operations

  @Smoke @Positive
  Scenario: Successfully log in and generate an authorization access token
    Given the Authentication API endpoint environment is set up
    When a POST request is sent to the login endpoint with valid user credentials
    Then the system response status code should be 200
    And the response payload must contain a secure "accessToken"
    And the response payload data structure must strictly match the "auth-schema.json" contract
    And the user profile details for firstName "Emily" should be validated successfully

  @Smoke @Profile
  Scenario: Successfully fetch authenticated user profile details using bearer token
    Given a valid session access token is already generated
    When a GET request is sent to the current user profile endpoint
    Then the system response status code should be 200
    And the response payload should contain email "emily.johnson@x.dummyjson.com" and username "emilys"
    
    @Smoke @Negative @Security
  Scenario: Rejected profile retrieval when presenting a corrupted session token
    Given an invalid or corrupted session access token is used
    When a GET request is sent to the current user profile endpoint
    Then the system response status code should be 401
    And the response error payload message should contain "Invalid/Expired Token!"

  @Smoke @Negative @Boundary
  Scenario: Rejected product look up for non-existent entry ID
    When I send a GET request for an invalid product ID "/products/999999"
    Then the product response status code should be 404
    And the product response error payload message should contain "Product with id '999999' not found"