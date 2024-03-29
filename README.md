# Emergency Reporting service - what3words
Emergency Reporting service using what3words API.

## Project properties
  * Java v17
  * Spring boot v2.5.8
  * What3Words Java Wrapper v3.1.8
  * Swagger v2.9.2
  * JUnit v4.13.1

## Notes
  * In order to execute the different requests, you need a Public API Key. This can be created at: https://developer.what3words.com/public-api
  * Once you have your Public API Key, you have to put it in the what3words.apiKey field of the application.properties file in the src/main/resources folder.
  * I have provided swagger documentation of the API endpoints, what parameters they require, and what results they return. It can be accessed by clicking the link: http://localhost:8080/swagger-ui.html with the server started.
