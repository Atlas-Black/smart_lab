package com.yiguan.smart_lab.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

public class JwtUtils {
    private static final String SECRET = "smart-lab-smart-lab-smart-lab-smart-lab";
    private static final long EXPIRE_TIME = 1000L * 60 * 60 * 24;
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String createToken(Long userId){
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt((new Date()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Long parseToken(String token){
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }
}
