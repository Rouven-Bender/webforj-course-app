package rbender.views;

import java.util.Set;

import rbender.components.ChapterDropdown;
import rbender.components.ChapterDropdownItem;
import rbender.components.DrawerHeader;
import rbender.components.Video;
import rbender.controllers.CourseDataProvider;
import rbender.types.Chapter;
import rbender.types.Lesson;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

@Route("/:chapter/:lesson")
public class MainLayout extends Composite<AppLayout> implements DidEnterObserver {
  private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();

  public MainLayout() {
    setHeader();
    setDrawer();
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  private void setHeader() {
    self.setDrawerHeaderVisible(true);

    self.addToDrawerTitle(new DrawerHeader());

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(title);

    self.addToHeader(toolbar);
    Div container = new Div();
    container.addClassName("container");
    Video vidPlayer = new Video("");
    vidPlayer.addClassName("VideoPlayer");
    container.add(vidPlayer);
    self.addToContent(container);
  }

  private void setDrawer() {
    Nav chapters = new Nav();
    for (Chapter c : courseDataProvider.getChapters()) {
      ChapterDropdown d = new ChapterDropdown(c);
      for (Lesson l : c.lessons) {
        String link = c.urlprefix.replace("/", "") + "/" + l.url.replace("/", "");
        d.add(new ChapterDropdownItem(l.name, link));
      }
      chapters.add(d);
    }
    self.addToDrawer(chapters);
  }

  private void onNavigate(NavigateEvent ev) {
    Set<Component> components = ev.getContext().getAllComponents();
    Component view = components.stream().filter(c -> c.getClass().getSimpleName().endsWith("View")).findFirst()
        .orElse(null);

    if (view != null) {
      FrameTitle frameTitle = view.getClass().getAnnotation(FrameTitle.class);
      title.setText(frameTitle != null ? frameTitle.value() : "");
    }
  }

  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag params) {
    String chapter = params.getAlpha("chapter").orElse("");
    String lesson = params.getAlpha("lesson").orElse("");
  }
}
