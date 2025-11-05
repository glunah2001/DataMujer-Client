package com.uned.clientedatamujer.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.uned.clientedatamujer.dto.token.TokenResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AuthSession {
    private static TokenResponse tokens;

    public static void setTokens(TokenResponse tokens) {AuthSession.tokens = tokens;}

    public static void clear(){tokens = null;}

    public static boolean noSession(){return tokens == null;}

    public static String getAccessToken(){
        return noSession() ? "" : tokens.accessToken();
    }

    public static String getRefreshToken(){return noSession() ? "" : tokens.refreshToken();}

    public static String getSubject(){return decode().getSubject();}

    public static String getRole(){return getClaim("role");}

    public static String getPersonType(){return getClaim("personType");}

    public static LocalDateTime getExpiration(){
        var expEpoch = decode().getClaim("exp").asLong();
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(expEpoch),
                ZoneId.systemDefault()
        );
    }

    private static DecodedJWT decode(){
        return JWT.decode(AuthSession.tokens.accessToken());
    }

    private static String getClaim(String claim){
        return decode().getClaim(claim).asString();
    }
}
