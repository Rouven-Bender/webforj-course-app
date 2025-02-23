package rbender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
    
    public static Optional<byte[]> getResourceFileAsByteArray(String filename) throws IOException{
        if (filename.equals("")){
            return Optional.empty();
        }
        return Optional.of(classLoader.getResourceAsStream(filename).readAllBytes());
    }

    public static Optional<String> getResourceAsString(String filename) throws IOException{
        if (filename.equals("")){
            return Optional.empty();
        }
        Optional<byte[]> data = getResourceFileAsByteArray(filename);
        if (data.isPresent()) {
            return Optional.of(new String(data.get(), StandardCharsets.UTF_8));
        } else {
            return Optional.empty();
        }
    }
}
