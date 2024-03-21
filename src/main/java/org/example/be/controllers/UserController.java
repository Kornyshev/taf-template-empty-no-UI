package org.example.be.controllers;

import io.restassured.http.Method;
import io.restassured.response.Response;
import org.example.be.models.User;
import org.example.core.ApiClient;

public class UserController {

    /**
     * URI: <a href="https://api.github.com/user">https://api.github.com/user</a>
     * Host: <a href="https://api.github.com">https://api.github.com</a>
     * Endpoint (resource): /user
     */

    private static final String GET_USER_DATA = "user";

    public User executeGetUserData() {
        return new ApiClient().build()
                // Метод с параметрами для отправки запроса (sendRequest)
                .sendRequest(Method.GET, 200, GET_USER_DATA)
                // Мапинг/десериализация боди от респонса (ответ сервера) на DTO. В исходном примере: класс User
                .as(User.class);
    }

    /**
     * Этот метод строго для того, чтобы получить какие-то логи в консоль
     * и понять как выглядит боди ответа и прочие детали...
     */
    public void executeGetUserDataExperiment() {
        Response response = new ApiClient().build()
                // Метод с параметрами для отправки запроса (sendRequest)
                .sendRequest(Method.GET, 200, GET_USER_DATA);
        // Далее можно дебажить объект response
    }

}
