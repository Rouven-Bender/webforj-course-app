package rbender.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.password4j.Password;
import com.webforj.webstorage.LocalStorage;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import rbender.Application;
import rbender.types.Course;
import rbender.types.User;

public class AuthProvider {
    private static AuthProvider instance = null;
    private ConfigProvider config = ConfigProvider.getInstance();
    private static SecretKey JWTSigningSecret;
    private Database database;

    private AuthProvider() throws Exception{
        JWTSigningSecret = Keys.hmacShaKeyFor(Application.getResourceFileAsByteArray("jwt-signing-secret").get());
        if (Database.getInstance().isPresent()){
            database = Database.getInstance().get();
        } else {
            //TODO: Log
            throw new Exception();
        }
    }

    public boolean isLogdin(){
        LocalStorage ls = LocalStorage.getCurrent();
        String username = ls.get("username");
        String token = ls.get("token");
        return validateJWTToken(token, username);
    }

    public Optional<String> getLogdinUsername(){
        LocalStorage ls = LocalStorage.getCurrent();
        String username = ls.get("username");
        String token = ls.get("token");
        if (validateJWTToken(token, username)){
            return Optional.of(username);
        } else {
            return Optional.empty();
        }
    }

    public void logoutUser(){
        LocalStorage ls = LocalStorage.getCurrent();
        ls.remove("username", "token");
    }

    private boolean validateJWTToken(String token, String username) {
        try {
            Jwts.parser()
            .requireIssuer(config.getConfig("issuer"))
            .requireSubject(username)
            .verifyWith(JWTSigningSecret)
            .build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkRedeemAndRegister(String code) {
        Optional<String> coursename = database.getCourseForCode(code);
        Optional<String> username = getLogdinUsername();
        if (coursename.isPresent() && username.isPresent()) {
            return (database.registerCourseForUser(coursename.get(), username.get()) && database.removeUsedActivationCode(code));
        } else {
            return false;
        }
    }

    public boolean userHasCourse(String username, String courseURL){
        Optional<Course> cs = CourseDataProvider.getInstance().getCourseByURL(courseURL);
        Optional<ArrayList<String>> cu = database.getCoursesUrlsOfUser(username);
        if (cs.isPresent() && cu.isPresent()){
            for (String c : cu.get()){
                if (cs.get().url.equals(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean registerNewUser(String username, String password) {
        if(database.isUsernameFree(username)){
            String pwdHash = Password.hash(password).withArgon2().getResult();
            return database.addNewUser(username, pwdHash);
        }
        return false;
    }

    public String createJWTToken(String username){
        return Jwts.builder()
            .subject(username)
            .issuer(config.getConfig("issuer"))
            .expiration(Date.from(Instant.now().plusSeconds(86400))) // expires in a day
            .signWith(JWTSigningSecret)
            .compact();
    }

    public static synchronized Optional<AuthProvider> getInstance(){
        if (instance == null){
            try {
                instance = new AuthProvider();
            } catch (Exception e){
                instance = null;
                return Optional.empty();
            }
        }
        return Optional.ofNullable(instance);
    }

    public boolean validateLoginData(String username, String pwd){
        Optional<User> user = database.getUser(username);
        if (user.isPresent()){
            return Password.check(pwd, user.get().pwdhash).withArgon2();
        } else {
            return false;
        }
    }
}
