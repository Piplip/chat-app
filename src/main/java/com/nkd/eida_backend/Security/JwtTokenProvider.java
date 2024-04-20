package com.nkd.eida_backend.Security;

import com.nkd.eida_backend.Domain.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "itsnhdhquwus812jjsusiqiwijqmwkwmasnbahwqhwiqiuwiansnshhqow";
    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(UserDetails userDetails){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        Claims claims = Jwts.claims()
                .subject(userDetails.getUser().getId().toString())
                .add("username", userDetails.getUser().getEmail())
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                .compact();
    }

    public String getUserFromJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                    .build().parseSignedClaims(token).getPayload();

            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Boolean validateToken(String authToken){
        try{
            Jwts.parser()
                    .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }catch (MalformedJwtException ex) {
            log.error(ex.getMessage());
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
