package com.dgraysh.bot.entities;

import com.auth0.jwt.JWT;
import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;
import lombok.Value;
import org.json.JSONObject;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Value
public class UserInfo {
    String subject;
    String preferredUsername;
    Set<Role> roles;

    public static UserInfo getUserInfoFromToken(OpenIdOAuth2AccessToken token) {
        var openIdTokenJwt = JWT.decode(token.getOpenIdToken());
        var accessTokenJwt = JWT.decode(token.getAccessToken());
        var subject = openIdTokenJwt.getSubject();
        var preferredUsername = accessTokenJwt.getClaim("preferred_username").asString();
        var jsonObject = new JSONObject(accessTokenJwt.getClaim("resource_access").toString());
        var jsonArray = jsonObject.getJSONObject("tg_bot").getJSONArray("roles");
        var roles = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(x -> new Role("ROLE_" + x))
                .collect(Collectors.toSet());
        return new UserInfo(subject, preferredUsername, roles);
    }

    public boolean hasRole(String roleTitle) {
        return roles.contains(new Role(roleTitle));
    }

}

