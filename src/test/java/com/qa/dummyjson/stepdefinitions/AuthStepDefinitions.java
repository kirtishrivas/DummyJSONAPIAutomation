package com.qa.dummyjson.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.qa.dummyjson.utils.ConfigReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AuthStepDefinitions {

    private RequestSpecification request;
    private Response response;
    private static String secureToken;

    @Given("the Authentication API endpoint environment is set up")
    public void the_authentication_api_endpoint_environment_is_set_up() {
        // ConfigReader se Base URL fetch karke RestAssured ko de rahe hain
        RestAssured.baseURI = ConfigReader.getProperty("baseURI");
        
        // Request specification initialize kar rahe hain JSON content-type ke saath
        request = RestAssured.given().contentType("application/json");
    }

    @When("a POST request is sent to the login endpoint with valid user credentials")
    public void a_post_request_is_sent_to_the_login_endpoint_with_valid_user_credentials() {
        // Strict JSON payload object for DummyJSON Auth
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", ConfigReader.getProperty("apiUsername"));
        credentials.put("password", ConfigReader.getProperty("apiPassword"));
        credentials.put("expiresInMins", 30); // Session expiry limits added

        // Live API hit
        response = request.body(credentials).post("/auth/login");
    }

    @Then("the system response status code should be {int}")
    public void the_system_response_status_code_should_be(Integer expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        
        // Agar fail ho, toh pehle console mein print ho ki backend kya bol raha hai
        if (actualStatusCode != expectedStatusCode) {
            System.out.println("\n❌ ====== SERVER REJECTION DETAILS ======");
            System.out.println("Status Code Received: " + actualStatusCode);
            System.out.println("Server Response Message: " + response.getBody().asString());
            System.out.println("=========================================\n");
        }
        
        assertEquals("Assertion Failed: Status code match nahi hua!", (int) expectedStatusCode, actualStatusCode);
    }

    @Then("the response payload must contain a secure {string}")
    public void the_response_payload_must_contain_a_secure(String tokenKey) {
        // Response body se JSONPath ke througth token extract kar rahe hain
        secureToken = response.jsonPath().getString(tokenKey);
        
        assertNotNull("Assertion Failed: Access Token null aaya hai!", secureToken);
        assertFalse("Assertion Failed: Access Token khali (empty) mila!", secureToken.trim().isEmpty());
    }

    @Then("the user profile details for firstName {string} should be validated successfully")
    public void the_user_profile_details_for_first_name_should_be_validated_successfully(String expectedFirstName) {
        // Backend se return huye user data ke real values ko pick kar rahe hain
        String actualFirstName = response.jsonPath().getString("firstName");
        String userEmail = response.jsonPath().getString("email");

        // Verification step
        assertEquals("Assertion Failed: Profile name match nahi hua!", expectedFirstName, actualFirstName);

        // Success logs in Console
        System.out.println("\n==================================================");
        System.out.println("🚀 LIVE DATA FETCHED FROM DUMMYJSON BACKEND!");
        System.out.println("==================================================");
        System.out.println("✅ Authenticated User: " + actualFirstName);
        System.out.println("📧 Backend Registered Email: " + userEmail);
        System.out.println("🔑 Generated Bearer Token: " + secureToken.substring(0, 20) + "...");
        System.out.println("==================================================\n");
    }

    @Given("a valid session access token is already generated")
    public void a_valid_session_access_token_is_already_generated() {
        // Verify kar rahe hain ki pichle scenario se token successfully mila ya nahi
        assertNotNull("Pre-requisite Failed: Access token available nahi hai!", secureToken);
        
        // Base URI set kar rahe hain aur Header mein Authorization Bearer Token add kar rahe hain
        request = RestAssured.given()
                             .header("Authorization", "Bearer " + secureToken)
                             .contentType("application/json");
    }

    @When("a GET request is sent to the current user profile endpoint")
    public void a_get_request_is_sent_to_the_current_user_profile_endpoint() {
        // DummyJSON ke authenticated user details nikalne wale route ko hit kar rahe hain
        response = request.get("/auth/me");
    }

    @Then("the response payload should contain email {string} and username {string}")
    public void the_response_payload_should_contain_email_and_username(String expectedEmail, String expectedUsername) {
        String actualEmail = response.jsonPath().getString("email");
        String actualUsername = response.jsonPath().getString("username");
        String actualPhone = response.jsonPath().getString("phone");

        // Assertions (Verifications)
        assertEquals("Profile Email match nahi hua!", expectedEmail, actualEmail);
        assertEquals("Profile Username match nahi hua!", expectedUsername, actualUsername);

        // Success Logs for Second Scenario
        System.out.println("\n==================================================");
        System.out.println("🛡️ SECURE USER PROFILE FETCHED SUCCESSFULLY!");
        System.out.println("==================================================");
        System.out.println("👤 Verified Username: " + actualUsername);
        System.out.println("📧 Verified Email: " + actualEmail);
        System.out.println("📞 Connected Phone: " + actualPhone);
        System.out.println("==================================================\n");
    }
    // Is static getter ka use hum aage dusri API calls (like Cart or Products) mein authorization ke liye karenge
    public static String getSecureToken() {
        return secureToken;
    }
    @And("the response payload data structure must strictly match the {string} contract")
    public void the_response_payload_data_structure_must_strictly_match_the_contract(String schemaFileName) {
        // Asserting the whole response payload structure against the JSON schema template file
        response.then().assertThat()
                .body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath("Schemas/" + schemaFileName));
        
        System.out.println("📄 Contract Validation Passed! JSON Response structure matches " + schemaFileName + " perfectly.");
    }
    @Given("an invalid or corrupted session access token is used")
    public void an_invalid_or_corrupted_session_access_token_is_used() {
        RestAssured.baseURI = com.qa.dummyjson.utils.ConfigReader.getProperty("baseURI");
        // Forcing an explicitly malformed token header configuration
        request = RestAssured.given()
                             .contentType("application/json")
                             .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.corruptedGarbageToken");
    }

    

    @And("the response error payload message should contain {string}")
    public void the_response_error_payload_message_should_contain(String expectedErrorMessage) {
        String actualMessage = response.jsonPath().getString("message");
        assertTrue("Assertion Failed: Got unexpected error text: " + actualMessage, 
                actualMessage.toLowerCase().contains(expectedErrorMessage.toLowerCase()));
        
        System.out.println("\n==================================================");
        System.out.println("🛑 SECURITY/BOUNDARY BLOCK REJECTION VALIDATED");
        System.out.println("==================================================");
        System.out.println("Returned Status Code: " + response.getStatusCode());
        System.out.println("Returned Server Message: " + actualMessage);
        System.out.println("==================================================\n");
    }
}