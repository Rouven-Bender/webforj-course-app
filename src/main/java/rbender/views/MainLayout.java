package rbender.views;

import java.util.ArrayList;
import java.util.Optional;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.Location;

import rbender.components.ChapterDropdown;
import rbender.components.ChapterDropdownItem;
import rbender.components.CourseDropdown;
import rbender.components.DrawerHeader;
import rbender.controllers.AuthProvider;
import rbender.controllers.CourseDataProvider;
import rbender.controllers.Database;
import rbender.types.Chapter;
import rbender.types.Lesson;

@Route("/")
public class MainLayout extends Composite<AppLayout> {
  private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();
  private AuthProvider authProvider = AuthProvider.getInstance().get();
  private Database db;
  private AppLayout self = getBoundComponent();

  public MainLayout() {
    db = Database.getInstance().get(); //TODO: make a 500 Screen for server errors
    setHeader();
    setDrawer();
    Router.getCurrent().onNavigate(this::checkLoginStatus);
  }

  private void checkLoginStatus(NavigateEvent event) {
    if (!event.getLocation().getFullURI().matches("^/login")){
      if (!authProvider.isLogdin()) {
        Router.getCurrent().navigate(new Location("/login"));
      }
    }
  }

  private void sendToAccountsPage(ElementClickEvent<Icon> event) {
    Router.getCurrent().navigate(new Location("/account"));
  }

  private void setHeader() {
    self.setDrawerHeaderVisible(true);

    self.addToDrawerTitle(new DrawerHeader());

    FlexLayout toolbar = new FlexLayout();
    toolbar.setStyle("padding", "8px");


    AppDrawerToggle toggle = new AppDrawerToggle();
    IconButton acc = new IconButton(FeatherIcon.USER.create());
    acc.setStyle("color", "var(--dwc-color-body-text)");
    acc.setStyle("margin-left", "auto");
    acc.onClick(this::sendToAccountsPage);

    toolbar.add(toggle, acc);

    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    Nav nav = new Nav();

    Optional<String> username = authProvider.getLogdinUsername();
    if (username.isPresent()){
      Optional<ArrayList<String>> courses = db.getCoursesUrlsOfUser(username.get());
      if (courses.isPresent()) {
        courses.get().forEach((course) -> {
          CourseDropdown cd = new CourseDropdown(courseDataProvider.getCourseByURL(course).get());
          for (Chapter c : courseDataProvider.getChapters(course).get()) {
            ChapterDropdown d = new ChapterDropdown(c);
            for (Lesson l : c.lessons) {
              String link = 
                  "/" + course +
                  "/" + c.url.replace("/", "") +
                  "/"+ l.url.replace("/", "");
              d.add(new ChapterDropdownItem(l.name, link));
            }
            cd.add(d);
          }
          nav.add(cd);
        });
      } else {
        nav.add(new Paragraph("Your User has no Courses"));
      }
    } else {
    }
    self.addToDrawer(nav);
  }
}
