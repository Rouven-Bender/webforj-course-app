package rbender.views;

import rbender.components.ChapterDropdown;
import rbender.components.ChapterDropdownItem;
import rbender.components.DrawerHeader;
import rbender.controllers.CourseDataProvider;
import rbender.types.Chapter;
import rbender.types.Lesson;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.annotation.Route;

@Route("/")
public class MainLayout extends Composite<AppLayout> {
  private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();

  public MainLayout() {
    setHeader();
    setDrawer();
  }

  private void setHeader() {
    self.setDrawerHeaderVisible(true);

    self.addToDrawerTitle(new DrawerHeader());

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(title);
    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    Nav chapters = new Nav();
    for (Chapter c : courseDataProvider.getChapters()) {
      ChapterDropdown d = new ChapterDropdown(c);
      for (Lesson l : c.lessons) {
        String link = "/" + c.urlprefix.replace("/", "") + "/" + l.url.replace("/", "");
        d.add(new ChapterDropdownItem(l.name, link));
      }
      chapters.add(d);
    }
    self.addToDrawer(chapters);
  }
}
