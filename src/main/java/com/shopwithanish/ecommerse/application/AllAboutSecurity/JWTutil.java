package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.Model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTutil {

    @Value("${spring.app.jwtExpiratintimeinMS}")
    private int jwtExpiratintimeinMS;

    @Value("${spring.app.JWTSECERT}")
    private String JWTSECERT;

    @Value("${spring.app.cookieName}")
    private String cookieName;

    //get jwt-token from headers

    public String getJwtTokenFromHeader(HttpServletRequest httpServletRequest){

        String Bearertoken=httpServletRequest.getHeader("Authorization");
        log.debug("Authorization header: {}", Bearertoken);
        if(Bearertoken!=null && Bearertoken.startsWith("Bearer ")){
            return Bearertoken.substring(7);
        }
        return null;
    }
    //genrating token from username
    public String generateTokenFromUsernameFn(String username) //this will take userdetails obj
    {
        return Jwts.builder()
                .setSubject(username)                 // 👈 username stored here
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(SECRETKEYfn())
                .compact();

    }
    //generating username from token
    public String getUsernameFromTokenFn(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRETKEYfn())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 👈 username
    }
    //generate signing-key

    public Key SECRETKEYfn(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWTSECERT));
    }

    //validate token
    // ✅ validate token (signature + expiry + structure)
    public  boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRETKEYfn())   // ✅ verification happens here
                    .build()
                    .parseClaimsJws(token);     // ✅ throws exception if invalid
            return true;
        } catch (ExpiredJwtException e) {
            // token expired
            log.error(" token expired : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // token malformed
            log.error("token malformed : {}", e.getMessage());
        } catch (Exception e) {
            // unsupported or illegal argument
            log.error("unsupported or illegal argument : {}", e.getMessage());
        }
        return false;
    }

    public ResponseCookie crateCookieFn( Users users){
        //inside cookie jwt token is present so we need to gerate token also
        String token= generateTokenFromUsernameFn(users.getUsername());

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .path("/")
                .httpOnly(true)
                .secure(false)          // 🔴 true only in production (HTTPS)
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        return cookie;
    }

    public String getJwtTokenInsideCookie(HttpServletRequest request ){
        //  Use ResponseCookie
        // When generating cookies in a Spring REST API
        //When sending JWT tokens
        //When using ResponseEntity
        //cookie httpservlet requet madhe pass asnar so pass argument as httpservlet request

        // Use Cookie
        //When reading cookies from HttpServletRequest


        Cookie cookie= WebUtils.getCookie(request , cookieName);
          if(cookie!=null){

              log.info("JWT FROM COOKIE: {}", cookie.getValue());
              return cookie.getValue(); //i.e. out token
          }
          else{
              return null; //to token inside it
          }
    }

    public ResponseCookie getCleanCookie(){

        //we don't pass token this time so user will not get token
        // and a new cookie will overwrite/replace old cookie and user will not able to access endpint without token now
        // in this way he will be sign out
        ResponseCookie cookie= ResponseCookie.from(cookieName , null )
                .path("/api")
                .build();

        return cookie;
    }
}
