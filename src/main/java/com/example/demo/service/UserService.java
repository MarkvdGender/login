package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.persistence.UserDao;
import com.example.demo.persistence.mysql.UserMysqlDaoImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

public class UserService {

    private static UserService instance;
    private static UserDao dao;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final byte[] secret = Base64.getDecoder().decode("bGtcnvDdBDzgVNV3zL9CJK+IiDcV8IwbIVmFXEgnUlw=");

    private UserService() {
        dao = UserMysqlDaoImpl.getInstance();

    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public String createToken(User u) {
        Instant now = Instant.now();
        String jwt = Jwts.builder()
                .setSubject(u.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(10, ChronoUnit.SECONDS)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
        return jwt;
    }

    public String authenticate(String jwt) {
        Jws<Claims> result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret))
                .parseClaimsJws(jwt);

        return result.getBody().getSubject();
    }

    private String generateHash(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(salt);
            byte[] hash = digest.digest(password.getBytes());
            return (bytesToString(hash));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String bytesToString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] createSalt() {
        byte[] salt = new byte[5];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public void register(User u) {
        byte[] salt = createSalt();
        String password = u.getPassword();
        u.setPassword(generateHash(password, salt));
        u.setSalt(salt);
        dao.save(u);
    }

    public boolean login(User u) {
        String username = u.getUsername();
        byte[] salt = dao.findSalt(username);
        if (salt == null) {
            return false;
        }
        String password = generateHash(u.getPassword(), salt);
        u.setPassword(password);
        return dao.login(u);
    }

    public List<User> leak(){
        return dao.leak();
    }

}
