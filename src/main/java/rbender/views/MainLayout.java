package rbender.views;

import java.util.ArrayList;
import java.util.Optional;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.drawer.Drawer;
import com.webforj.component.drawer.Drawer.Placement;
import com.webforj.component.html.elements.ListEntry;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.UnorderedList;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optiondialog.InputDialog;
import com.webforj.component.toast.Toast;
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
  private Nav courseNav;
  private Drawer accDrawer;
  private InputDialog redeemDialog;

  public MainLayout() {
    db = Database.getInstance().get(); //TODO: make a 500 Screen for server errors
    setupDialogs();
    setHeader();
    setDrawer();
    setAccountDrawer();
    Router.getCurrent().onNavigate(this::checkLoginStatus);
  }

  private void setAccountDrawer(){
    accDrawer = new Drawer();
    accDrawer.setPlacement(Placement.RIGHT);
    accDrawer.setLabel("Account");

    ListEntry redeem = new ListEntry("Redeem Code");
    redeem.setStyle("cursor", "pointer");
    redeem.onClick((e) -> {
      String code = redeemDialog.open(); 
      if (authProvider.checkRedeemAndRegister(code)) {
        Toast.show("course was successfully activated", Theme.SUCCESS);
      } else {
        Toast.show("the course code was either invalid or an error accrued", Theme.DANGER);
      }
      setDrawer();
    });
    accDrawer.add(new UnorderedList(
      redeem
    ));
    self.add(accDrawer);
  }

  private void checkLoginStatus(NavigateEvent event) {
    if (!event.getLocation().getFullURI().matches("^/login")){
      if (!authProvider.isLogdin()) {
        Router.getCurrent().navigate(new Location("/login"));
      }
    }
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
    acc.onClick((e) -> { 
      if (accDrawer.isOpened()) {
        accDrawer.close();
      } else {
        accDrawer.open();
      }
    });

    toolbar.add(toggle, acc);

    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    if (courseNav != null) {
      self.remove(courseNav);
    }
    courseNav = new Nav();

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
          courseNav.add(cd);
        });
      } else {
        courseNav.add(new Paragraph("Your User has no Courses"));
      }
    }

    self.addToDrawer(courseNav);
  }
  
  private void setupDialogs() {
    redeemDialog = new InputDialog(
      "Redeem your Course using a license key",
      "Redeem Course with Code",
      InputDialog.InputType.TEXT
    );
    redeemDialog.setBlurred(true);
    redeemDialog.setButtonText(InputDialog.Button.FIRST, "Redeem");
    redeemDialog.setFirstButtonTheme(ButtonTheme.PRIMARY);
    redeemDialog.setButtonText(InputDialog.Button.SECOND, "Cancel");
  }
}
