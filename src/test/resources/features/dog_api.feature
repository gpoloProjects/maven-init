Feature: Dog API Management
  As an API user
  I want to manage dogs through the API
  So that I can perform CRUD operations on dog resources

  Background:
    Given the API is running

  Scenario: Get all dogs
    When the client requests all dogs
    Then the response status code should be 200
    And the response should contain a list of dogs

  Scenario: Get a specific dog by ID
    Given a dog exists with ID 1
    When the client requests the dog with ID 1
    Then the response status code should be 200
    And the response should contain details of the dog with ID 1

  Scenario: Get a non-existent dog
    When the client requests the dog with ID 999
    Then the response status code should be 404

  Scenario: Create a new dog
    Given a new dog with name "Rex" and breed "German Shepherd"
    When the client creates a new dog
    Then the response status code should be 201
    And the response should contain the created dog with name "Rex" and breed "German Shepherd"

  Scenario: Delete a dog
    Given a dog exists with ID 1
    When the client deletes the dog with ID 1
    Then the response status code should be 204
    And the dog with ID 1 should no longer exist

  Scenario: Update a dog
    Given a dog exists with ID 1
    And an updated dog with name "Max" and breed "Golden Retriever"
    When the client updates the dog with ID 1
    Then the response status code should be 200
    And the response should contain the updated dog with name "Max" and breed "Golden Retriever"
