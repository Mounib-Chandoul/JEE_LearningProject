package com.example.jee_project;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
// Adding this annotation wakes up WildFly's internal security provider mechanisms
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(loginPage = "/login.html")
)
public class HelloApplication extends Application {

}