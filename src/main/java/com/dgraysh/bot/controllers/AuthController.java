package com.dgraysh.bot.controllers;

import com.dgraysh.bot.Bot;
import com.dgraysh.bot.configs.BotProperties;
import com.dgraysh.bot.services.OidcService;
import com.dgraysh.bot.services.UserTrackerService;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Path("/auth")
public class AuthController {
    private final URI botUri;
    private final OidcService oidcService;
    private final Bot bot;
    private final UserTrackerService userTrackerService;

    public AuthController(BotProperties botProperties, OidcService oidcService, Bot bot, UserTrackerService userTrackerService) throws URISyntaxException {
        botUri = new URI("tg://resolve?domain=" + botProperties.getName());
        this.oidcService = oidcService;
        this.bot = bot;
        this.userTrackerService = userTrackerService;
    }

    @GET()
    @Produces("text/plain; charset=UTF-8")
    public Response auth(@QueryParam("state") String state, @QueryParam("code") String code) {
//        userTrackerService.find(state).ifPresent(bot::greet);//todo
        return oidcService.completeAuth(state, code)
                .map(userInfo -> Response.temporaryRedirect(botUri).build())
                .orElseGet(() -> Response.serverError().entity("Cannot complete authentication").build());
    }
}
