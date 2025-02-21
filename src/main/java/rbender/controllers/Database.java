package rbender.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


import rbender.types.User;

public class Database {
    private static Database instance = null;
    private Connection con;

    private Database() throws Exception { //TODO: Create custom exception later
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        } catch (SQLException sqlE){
            throw new Exception();
        } catch (ClassNotFoundException classE){
            throw new Exception();
        }

    }

    public static synchronized Optional<Database> getInstance(){
        try {
            if (instance == null){
                instance = new Database();
            }
            return Optional.of(instance);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<User> getUser(String username){
        try {
            PreparedStatement stmt = con.prepareStatement("Select * from users where username=?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                User user = new User();
                user.username = rs.getString("username");
                user.pwdhash = rs.getString("pwdhash");
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e){
            return Optional.empty();
        }
    }
}
