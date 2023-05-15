# Technical Test - what3words
Emergency Reporting service for what3words technical test.

  * Author: Gustavo Ayrton Bitocchi
  * Email: gustavoabitocchi@gmail.com
  * Mobile phone: +5491140653465

## Environment properties
  * Java v17
  * Spring boot v2.5.8
  * What3Words Java Wrapper v3.1.8
  * Swagger v2.9.2
  * JUnit v4.13.1

## Notes
  * In order to execute the different requests, you need an Public API Key. This can be created at: https://developer.what3words.com/public-api
  * Once you have your Public API Key, you have to put it in the what3words.apiKey field of the application.properties file in the src/main/resources folder.
  * I have provided a swagger documentation of the API endpoints and what parameters they require and what results they return. It can be accessed by clicking the following link: http://localhost:8080/swagger-ui.html with the server started.
