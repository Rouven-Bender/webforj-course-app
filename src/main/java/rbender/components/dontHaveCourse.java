package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexContentAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class dontHaveCourse extends Composite<FlexLayout>{
    private FlexLayout self = getBoundComponent();

    public dontHaveCourse(){
        self.setAlignContent(FlexContentAlignment.CENTER);
        self.setStyle("flex-direction", "column");
        self.setStyle("flex-wrap", "nowrap"); // setFlow with column_nowrap became row_nowrap for some reason
        self.setStyle("margin", "auto");
        H1 fourOthree = new H1("403");
        fourOthree.setStyle("margin", "auto");
        fourOthree.setStyle("color", "var(--dwc-color-primary-text)");
        fourOthree.setStyle("font-size", "80px");
        Paragraph text = new Paragraph("You don't have this Course");
        text.setStyle("font-size", "18px");
        text.setStyle("margin", "auto");

        self.add(fourOthree,text);

        hide();
    }

    public void show(){
        self.setVisible(true);
    }
    public void hide(){
        self.setVisible(false);
    }
    public void toggle(){
        self.setVisible(!self.isVisible());
    }
}
