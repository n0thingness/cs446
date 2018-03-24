package io.chatr.chatr.data.model;

/**
 * Created by Daniel on 2018-03-22.
 */

public class LoginRequest {
    final String email;
    final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
