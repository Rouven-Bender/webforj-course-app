package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class DrawerHeader extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public DrawerHeader() {
    self.setDirection(FlexDirection.COLUMN);
    self.setSpacing("0px");

    H1 title = new H1("Course Plattform");
    title.setStyle("margin-bottom", "0");
    self.add(title);

    Paragraph subtitle = new Paragraph("#builtwithwebforj");
    subtitle.setStyle("color", "#86888f");
    subtitle.setStyle("font-size", ".7em");
    self.add(subtitle);
  }
}
