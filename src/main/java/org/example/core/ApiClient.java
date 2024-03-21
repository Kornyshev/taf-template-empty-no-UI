package org.example.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.core.properties.Properties;

import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

/**
 * ApiClient для работы строго с одним URL.
 * Если потребуется расширять - нужно будет либо модифицировать этот класс,
 *      либо создавать рядом такой же класс только для другого сервиса.
 * Если потребуется расширять проект для микросервисов, нужно подумать над параметризацией методов:
 *      либо делать переменным хост и порт, как параметр метода и поле в классе,
 *      либо делать переменным только порт, если сервисы на одной машине на одном хосту.
 * <p>
 * Этот класс реализован с помощью паттерна Builder.
 */
public class ApiClient {

    /**
     * Токен из файла с пропертями.
     * Он загружается в BeforeAll методе в переменные окружения.
     */
    private static final String TOKEN = System.getProperty(Properties.API_TOKEN);

    private final RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    private RequestSpecification requestSpecification;

    public Response sendRequest(Method method, int expectedStatusCode, String url, Object... params) {
        Response response = given()
                .spec(requestSpecification)
                .request(method, url, params).prettyPeek()
                .then().log().status()
                .extract().response();
        if (expectedStatusCode != -1) {
            response = response.then().statusCode(expectedStatusCode).extract().response();
        }
        return response;
    }

    public ApiClient build() {
        requestSpecification = requestSpecBuilder
                .setConfig(newConfig().encoderConfig(encoderConfig().defaultContentCharset(StandardCharsets.UTF_8)))
                // Это URL сервиса, на который мы хотим пустачаться. Например: 54.98.65.32:8956
                // В случае модификации/параметризации именно сюда будет приходить динамический параметр.
                .setBaseUri(System.getProperty(Properties.BASE_API_URL))
                .log(LogDetail.ALL)
                // Это заголовки запросов - они зависят от реализации сервисов.
                // Они должны быть соответственно модифицированы.
                .addHeader("Authorization", "Bearer " + TOKEN)
                // GitHub-specific header - при адаптации проекта к свои сервисам нужно удалить.
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
        return this;
    }

    public ApiClient addQueryParam(String name, String value) {
        requestSpecBuilder.addQueryParam(name, value);
        return this;
    }

    public ApiClient addBody(String body) {
        requestSpecBuilder.setBody(body);
        return this;
    }

    public ApiClient addBody(Object body) {
        requestSpecBuilder.setBody(body);
        return this;
    }

    public ApiClient addJsonContentType() {
        requestSpecBuilder.addHeader("Content-Type", "application/json");
        return this;
    }

}
