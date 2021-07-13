package com.dgraysh.bot.services;

import com.dgraysh.bot.entities.UserInfo;
import com.dgraysh.bot.repositories.TokenRepository;
import com.dgraysh.bot.repositories.UserTrackerRepository;
import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OidcService {
    private final OAuth20Service oAuthService;
    private final UserTrackerRepository userTrackers;
    private final TokenRepository accessTokens;

    public Optional<UserInfo> findUserInfo(Long userId) {
        return accessTokens.find(userId)
                .map(UserInfo::getUserInfoFromToken);
    }

    public String getAuthUrl(Long userId) {
        var state = UUID.randomUUID().toString();
        userTrackers.put(state, userId);
        return oAuthService.getAuthorizationUrl(state);
    }

    public Optional<UserInfo> completeAuth(String state, String code) {
        return userTrackers.find(state)
                .map(userId -> requestAndStoreToken(code, userId))
                .map(UserInfo::getUserInfoFromToken);
    }

    private OpenIdOAuth2AccessToken requestAndStoreToken(String code, Long userId) {
        var token = requestToken(code);
        accessTokens.put(userId, token);
        return token;
    }

    private OpenIdOAuth2AccessToken requestToken(String code) {
        try {
            return (OpenIdOAuth2AccessToken) oAuthService.getAccessToken(code);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("Cannot get access token", e);
        }
    }
}

