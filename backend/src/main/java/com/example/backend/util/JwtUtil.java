package com.example.backend.util;

import com.example.backend.services.UserDetail.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${refreshToken.expiration}")
    private Long refreshTokenExpiration;

    @Value("${accessToken.expiration}")
    private Long accessTokenExpiration;

    private static Key key;


    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(customUserDetails.getUsername())
                .subject(customUserDetails.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000L))
                .and()
                .signWith(getKey())
                .compact()
                ;
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(customUserDetails.getUsername())
                .subject(customUserDetails.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000L))
                .and()
                .signWith(getKey())
                .compact()
                ;
    }

    public Date getExpirationDate(String token) {
        return extractExpiry(token);
    }

    private Date extractExpiry(String token) {
        return extractClaim(token, Claims::getExpiration);

    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String tokenType = extractTokenType(token);
        return (username.equals(userDetails.getUsername())) && TokenNotExpired(token) && "access".equals(tokenType);
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final String tokenType = extractTokenType(token);
        return (username.equals(userDetails.getUsername())) && TokenNotExpired(token) && "refresh".equals(tokenType);
    }

    private boolean TokenNotExpired(String token) {
        return !extractExpiry(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }
}
