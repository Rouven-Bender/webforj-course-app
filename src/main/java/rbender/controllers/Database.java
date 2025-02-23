package rbender.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import rbender.types.*;

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

    public Optional<ArrayList<String>> getCoursesUrlsOfUser(String username){
        try {
            PreparedStatement stmt = con.prepareStatement("select * from whichCoursesHasUser where username=?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> out = new ArrayList<String>();
            int length = 0;
            while(rs.next()){
               out.add(rs.getString("cname"));
               length++;
            }
            if (length != 0){
                return Optional.of(out);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e){
            //TODO: Logging
            return Optional.empty();
        }
    }
}
