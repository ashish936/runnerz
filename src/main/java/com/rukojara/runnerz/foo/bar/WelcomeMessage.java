package com.rukojara.runnerz.foo.bar;

import org.springframework.stereotype.Component;

@Component
public class WelcomeMessage {

    public String getWelcomeMessage() {
        return "Welcome to the spring boot application";
    }
}
