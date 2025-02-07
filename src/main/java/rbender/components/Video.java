package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.element.Element;
import com.webforj.component.html.elements.Div;

public class Video extends Composite<Div>{
    Div self = getBoundComponent();

    public Video(String... srcs) {
        if (srcs == null) {
            throw new NullPointerException();
        }

        Element vid = new Element("video");
        vid.setAttribute("controls", "");

        for (String s : srcs) {
            Element source = new Element("source");
            source.setAttribute("src", s);
            vid.add(source);
        }
        self.add(
            vid
        );

    }

    public Video addClassName(String... classNames){
        self.addClassName(classNames);
        return this;
    }
}
