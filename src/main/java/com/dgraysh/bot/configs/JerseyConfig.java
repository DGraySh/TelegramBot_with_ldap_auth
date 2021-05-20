package com.dgraysh.bot.configs;

import com.dgraysh.bot.controllers.AuthController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(AuthController.class);
    }
}