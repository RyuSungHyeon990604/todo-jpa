package com.example.todojpa.security;

public class MySecurityContextHolder {
    private static final ThreadLocal<UserAuthentication> context = new ThreadLocal<>();

    public static void setAuthenticated(UserAuthentication user) {
        context.set(user);
    }

    public static UserAuthentication getAuthenticated() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
