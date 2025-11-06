package com.uned.clientedatamujer.service.util;

import com.uned.clientedatamujer.dto.ApiError;
import com.uned.clientedatamujer.dto.token.TokenResponse;
import com.uned.clientedatamujer.service.AuthService;

import java.time.Duration;
import java.time.LocalDateTime;

public class TokenRefresher {
    private static final Duration REFRESH_BEFORE = Duration.ofMinutes(5);
    private static final Object lock = new Object();
    private static boolean refreshing = false;

    public static void refreshIfNeeded() {
        if (AuthSession.noSession()) return;

        LocalDateTime expiration = AuthSession.getExpiration();
        LocalDateTime now = LocalDateTime.now();

        if (expiration.minus(REFRESH_BEFORE).isAfter(now)) return;

        synchronized (lock) {
            if (refreshing) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
            refreshing = true;
        }

        try {
            AuthService service = new AuthService();
            var response = service.refresh();
            if (response instanceof ApiError error) {
                AuthSession.clear();
            }else if(response instanceof TokenResponse tokens){
                AuthSession.setTokens(tokens);
            }
        } finally {
            synchronized (lock) {
                refreshing = false;
                lock.notifyAll();
            }
        }
    }
}
