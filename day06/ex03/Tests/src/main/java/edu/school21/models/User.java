package edu.school21.models;

import java.util.Objects;
public class User {
    private final Long identifier;
    private final String login;
    private final String password;
    private boolean authenticated;

    public User(Long identifier, String login, String password, boolean authenticated) {
        this.identifier = identifier;
        this.login = login;
        this.password = password;
        this.authenticated = authenticated;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
