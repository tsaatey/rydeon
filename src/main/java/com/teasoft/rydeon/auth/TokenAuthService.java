package com.teasoft.rydeon.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teasoft.rydeon.model.Users;
import com.teasoft.rydeon.model.Views;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class TokenAuthService {

    // 100 days
    private static final long VALIDITY_TIME_MS = 100 * 24 * 60 * 60 * 1000;
    private static final long TEMP_VALIDITY_TIME_MS = 1000;
    private static final String AUTH_HEADER_NAME = "x-auth-token";

    @Value("${token.secret}")
    private String secret;

    public String addAuthentication(HttpServletResponse response, TokenUser tokenUser) {
        String token = createTokenForUser(tokenUser.getUser());
        response.addHeader(AUTH_HEADER_NAME, token);
        response.addHeader("Access-Control-Expose-Headers", "x-auth-token");
        return token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && !token.isEmpty()) {
            final TokenUser user = parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        final String token2 = request.getParameter("token");
        if (token2 != null && !token2.isEmpty()) {
            final TokenUser user = parseUserFromToken(token2);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }

        return null;

    }

    public TokenUser parseUserFromToken(String token) {
        try {
            String userJSON = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return new TokenUser(fromJSON(userJSON));
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public String createTokenForUser(Users user) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String createTemporalToken(Users user) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + TEMP_VALIDITY_TIME_MS))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Users fromJSON(final String userJSON) {
        try {
            return new ObjectMapper().readValue(userJSON, Users.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toJSON(Users user) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            return mapper.writerWithView(Views.TokenUser.class).writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

}
