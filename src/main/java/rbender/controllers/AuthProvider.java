package rbender.controllers;

import java.util.Optional;

import com.password4j.Password;

import rbender.types.User;

public class AuthProvider {
    private static AuthProvider instance = null;
    private Database database;

    private AuthProvider() throws Exception{
        if (Database.getInstance().isPresent()){
            database = Database.getInstance().get();
        } else {
            throw new Exception();
        }
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
