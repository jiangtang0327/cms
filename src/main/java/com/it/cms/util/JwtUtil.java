package com.it.cms.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    //签名秘钥
    private static String signKey = "jiangtang";

    //过期时间(1天),一般30分钟
    private static Long expire = 60 * 60 * 24 * 1000L;

    /**
     * 生成JWT
     * @param claims JWT声明，包含payload信息
     * @return 生成的JWT字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                //自定义信息（有效载荷）
                .setClaims(claims)
                //签名算法（头部）
                .signWith(SignatureAlgorithm.HS256, signKey)
                // 设置JWT的过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();

        return jwt;
    }


    /**
     * 解析JWT Token并返回Claims对象
     * @param jwt JWT Token字符串
     * @return 解析出的Claims对象
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }


    /**
     * 获取用户ID
     * @param token 用户令牌
     * @return 用户ID
     */
    public static String getUserId(String token){
        Claims claims = parseJWT(token);
        String id = claims.get("userId").toString();
        return id;
    }

    /**
     * 获取token
     *
     * @return {@link String}
     */
    public static String getToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); // 获取当前请求的属性
        HttpServletRequest request = requestAttributes.getRequest(); // 获取当前请求
        String token = request.getHeader("Authorization");
        return token; // 返回token
    }

}
