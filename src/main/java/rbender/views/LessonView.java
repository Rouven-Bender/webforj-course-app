package rbender.views;

import java.util.Optional;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.event.DidLeaveEvent;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.Location;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;
import com.webforj.router.observer.DidLeaveObserver;

import rbender.components.LessonContent;
import rbender.components.dontHaveCourse;
import rbender.components.notFound;
import rbender.controllers.AuthProvider;
import rbender.controllers.CourseDataProvider;
import rbender.types.Course;

@Route(value = ":course/:chapter/:lesson", outlet = MainLayout.class)
public class LessonView extends Composite<FlexLayout> implements DidEnterObserver, DidLeaveObserver{
    private FlexLayout self = getBoundComponent();
    private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();
    private AuthProvider authProvider = AuthProvider.getInstance().get();

    private ListenerRegistration<NavigateEvent> r;

    private notFound f404Element;
    private dontHaveCourse dhCourse;
    private LessonContent lContent;


    public LessonView(){
        checkLoginStatusOrSendToLogin();
        self.setStyle("height", "100%");
        r = Router.getCurrent().onNavigate(this::onNavigate);
    }

    private void checkLoginStatusOrSendToLogin(){
        if (!authProvider.isLogdin()){
            Router.getCurrent().navigate(new Location("/login"));
        }
    }

    private boolean checkUserOwningTheCourse(String courseUrl){
        Optional<String> username = authProvider.getLogdinUsername();
        if (username.isPresent()) {
            return authProvider.userHasCourse(username.get(), courseUrl);
        }
        return false;
    }

    @Override
    public void onDidEnter(DidEnterEvent event, ParametersBag params) {
        checkLoginStatusOrSendToLogin();
        Optional<Location> l = Router.getCurrent().getResolvedLocation();
        if (l.isPresent()) {
            decideHowToRender(l.get().getFullURI());
        }
    }

    @Override
    public void onDidLeave(DidLeaveEvent event, ParametersBag params){
        r.remove();
    }

    private void decideHowToRender(String url){
        Optional<String> cn = getCourseNameFromURL(url);
        if (cn.isPresent()) {
            Optional<Course> course = courseDataProvider.getCourseByURL(cn.get());
            if (course.isPresent()) {
                if (checkUserOwningTheCourse(course.get().url)){
                    if (courseDataProvider.isLessonLink(url)) {
                        //Course exists, User owns course and the lesson exists
                        lContent = new LessonContent(url);
                        self.add(lContent);
                    } else {
                        //Course exists, User owns course but lesson doesn't exist
                        f404Element = new notFound();
                        self.add(f404Element);
                    }
                } else {
                    //course exists but user doesn't own it
                    dhCourse = new dontHaveCourse();
                    self.add(dhCourse);
                }
            } else {
                // course doesn't exist
                f404Element = new notFound();
                self.add(f404Element);
            }
        } else {
            // course name could not be extracted from url
            f404Element = new notFound();
            self.add(f404Element);
        }
    }
    
    private void resetViewToDefault(){
        if (f404Element != null){
            self.remove(f404Element);
            f404Element = null;
        }
        if (dhCourse != null){
            self.remove(dhCourse);
            dhCourse = null;
        }
        if (lContent != null){
            self.remove(lContent);
            lContent = null;
        }
    }

    private void onNavigate(NavigateEvent ev) {
        String link = ev.getLocation().getFullURI();
        //unload the view
        resetViewToDefault();
        //check if we reenter the same view because that doesn't trigger an onDidEnter
        if (resolvesToThisView(link)){
            decideHowToRender(link);
        }
    }

    private boolean resolvesToThisView(String link) {
        return link.matches("^\\/.{1,}\\/.{1,}\\/.{1,}$");
    }

    private Optional<String> getCourseNameFromURL(String link) {
        String[] dirs = link.split("/");
        if (dirs.length < 2) {
            return Optional.empty();
        } else {
            return Optional.of(dirs[1]);
        }
    }
}
