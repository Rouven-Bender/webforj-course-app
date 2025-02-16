package rbender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.AppTitle;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "rbender.views")
@StyleSheet("ws://app.css")
@AppTitle("Course Plattform")
@AppTheme("system")
@AppProfile(name = "Course Plattform", shortName = "Course Plattform")
public class Application extends App {
    private static ClassLoader classLoader = Application.class.getClassLoader();
    
    public static byte[] getResourceFileAsByteArray(String filename) throws IOException{
        if (filename.equals("")){
            return new byte[]{};
        }
        return classLoader.getResourceAsStream(filename).readAllBytes();
    }

    public static String getResourceAsString(String filename) throws IOException{
        if (filename.equals("")){
            return "";
        }
        byte[] data = getResourceFileAsByteArray(filename);
        return new String(data, StandardCharsets.UTF_8);
    }
}
