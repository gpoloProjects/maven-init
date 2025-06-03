package com.example.demo.cucumber;

import com.example.demo.models.Dog;
import com.example.demo.models.DogRequestDto;
import com.example.demo.repositories.DogRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DogApiStepDefinitions {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    private DogRepository dogRepository;

    private RequestSpecification request;
    private Response response;
    private DogRequestDto dogRequestDto;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = contextPath;
        request = given();
    }

    @After
    public void cleanUp() {
        // Clean up the test data
        dogRepository.deleteAll();
    }

    @Given("the API is running")
    public void theApiIsRunning() {
        // No action needed, Spring Boot test ensures the API is running
    }

    @Given("a dog exists with ID {long}")
    public void aDogExistsWithId(Long id) {
        // First delete any existing dog with this ID to avoid conflicts
        try {
            dogRepository.deleteById(id);
            dogRepository.flush();
        } catch (Exception e) {
            // Ignore if the entity doesn't exist
        }

        // Create a new dog with the specific ID
        Dog dog = new Dog();
        dog.setId(id);
        dog.setName("Test Dog");
        dog.setBreed("Test Breed");

        // Save the dog using JPA repository save method
        Dog savedDog = dogRepository.save(dog);
        dogRepository.flush();

        // Verify the dog was saved with the correct ID
        Optional<Dog> retrievedDog = dogRepository.findById(id);
        if (retrievedDog.isEmpty()) {
            throw new RuntimeException("Failed to create test dog with ID: " + id);
        }
    }

    @Given("a new dog with name {string} and breed {string}")
    public void aNewDogWithNameAndBreed(String name, String breed) {
        dogRequestDto = new DogRequestDto();
        dogRequestDto.setName(name);
        dogRequestDto.setBreed(breed);
    }

    @Given("an updated dog with name {string} and breed {string}")
    public void anUpdatedDogWithNameAndBreed(String name, String breed) {
        dogRequestDto = new DogRequestDto();
        dogRequestDto.setName(name);
        dogRequestDto.setBreed(breed);
    }

    @When("the client requests all dogs")
    public void theClientRequestsAllDogs() {
        response = request.get("/v1/examples/dogs");
    }

    @When("the client requests the dog with ID {long}")
    public void theClientRequestsTheDogWithId(Long id) {
        response = request.get("/v1/examples/dogs/" + id);
    }

    @When("the client creates a new dog")
    public void theClientCreatesANewDog() {
        response = request
                .contentType(ContentType.JSON)
                .body(dogRequestDto)
                .post("/v1/examples/dogs");
    }

    @When("the client deletes the dog with ID {long}")
    public void theClientDeletesTheDogWithId(Long id) {
        response = request.delete("/v1/examples/dogs/" + id);
    }

    @When("the client updates the dog with ID {long}")
    public void theClientUpdatesTheDogWithId(Long id) {
        response = request
                .contentType(ContentType.JSON)
                .body(dogRequestDto)
                .put("/v1/examples/dogs/" + id);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("the response should contain a list of dogs")
    public void theResponseShouldContainAListOfDogs() {
        response.then().body("$", instanceOf(List.class));
    }

    @Then("the response should contain details of the dog with ID {long}")
    public void theResponseShouldContainDetailsOfTheDogWithId(Long id) {
        response.then().body("id", equalTo(id.intValue()));
    }

    @Then("the response should contain the created dog with name {string} and breed {string}")
    public void theResponseShouldContainTheCreatedDogWithNameAndBreed(String name, String breed) {
        response.then()
                .body("name", equalTo(name))
                .body("breed", equalTo(breed));
    }

    @Then("the dog with ID {long} should no longer exist")
    public void theDogWithIdShouldNoLongerExist(Long id) {
        Optional<Dog> dog = dogRepository.findById(id);
        assertFalse(dog.isPresent(), "Dog with ID " + id + " should not exist in the database");
    }

    @Then("the response should contain the updated dog with name {string} and breed {string}")
    public void theResponseShouldContainTheUpdatedDogWithNameAndBreed(String name, String breed) {
        response.then()
                .body("name", equalTo(name))
                .body("breed", equalTo(breed));
    }
}
