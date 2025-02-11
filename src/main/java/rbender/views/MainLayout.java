package rbender.views;

import java.util.Set;

import rbender.components.ChapterDropdown;
import rbender.components.ChapterDropdownItem;
import rbender.components.DrawerHeader;
import rbender.components.Video;

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
import com.webforj.router.event.NavigateEvent;

@Route("/:chapter/:lesson")
public class MainLayout extends Composite<AppLayout> {
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
    chapters.add(
      new ChapterDropdown("Chapter 0: Introduction").add(
        new ChapterDropdownItem("What is WebforJ", "chapter-0/lesson-1"),
        new ChapterDropdownItem("What is a Frontend", "chapter-0/lesson-2")
      ),
      new ChapterDropdown("Chapter 1: Setup")
    );
    self.addToDrawer(chapters);
    //self.addToDrawer(appNav);
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
}
