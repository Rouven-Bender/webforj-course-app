package rbender;

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
}
