package com.br.luggycar.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtils {

    private static final String SECRET_KEY = "lgpd-eh-legal";

    public static String generateToken(String data) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withClaim("data", data)
                .sign(algorithm);
    }

    public static String decodeToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("data").asString();
    }
}
