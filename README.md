# Maven-Init Project

This project demonstrates how to set up a Spring Boot application with JPA, Cucumber for BDD testing, and Bruno for API testing.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Git (optional)

### Running the Application

To run the application:

```bash
mvn spring-boot:run
```

### Running Tests

To run all tests including Cucumber tests:

```bash
mvn test
```

To run only Cucumber tests:

```bash
mvn test -Dtest=CucumberTestRunner
```

## Running Bruno API Tests

This project includes integration with Bruno API testing through a shell script. The script can be executed through Maven with custom parameters and environment variables.

### Running the Bruno Tests

To run only the Bruno shell script without executing other tests:

```bash
mvn exec:exec@run-bruno-tests -Dbruno.env=local -Dbruno.host=localHost -Dtest.port=8080
```

This command will:
- Execute the runBruno.sh script
- Pass 'arg1Value' as an argument to the script
- Set environment variables:
  - BRUNO_ENV=dev
  - TEST_URL=http://localhost:8080

### Customizing Bruno Parameters

You can customize the parameters passed to the Bruno script:

- `-Dbruno.arg1=[value]` - First argument to pass to the script
- `-Dbruno.arg2=[value]` - Second argument to pass to the script  
- `-Dbruno.env=[value]` - Sets the BRUNO_ENV environment variable
- `-Dtest.url=[value]` - Sets the TEST_URL environment variable

## Project Structure

- `src/main/java/com/example/demo`: Application source code
- `src/test/java/com/example/demo/cucumber`: Cucumber test code
- `src/test/resources/features`: Cucumber feature files
- `bruno/`: Bruno API test files
- `target/cucumber-reports.html`: Generated Cucumber test report
