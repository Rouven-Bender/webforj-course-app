package rbender.components;

import java.util.UUID;

import com.webforj.component.Composite;
import com.webforj.component.element.Element;
import com.webforj.component.html.elements.Div;

public class Video extends Composite<Div>{
    Div self = getBoundComponent();
    private Element vid = new Element("video");

    public Video(String... srcs) {
        createVid(srcs);
    }

    private void createVid(String ... srcs) {
        if (srcs == null) {
            throw new NullPointerException();
        }
        String videoName = UUID.randomUUID().toString();

        self.addClassName("VideoPlayer");

        vid = new Element("video");
        vid.setAttribute("controls", "");
        vid.setAttribute("name", videoName);
        vid.setMaxWidth("640px");
        vid.setMaxHeight("480px");

        for (String s : srcs) {
            Element source = new Element("source");
            source.setAttribute("src", s);
            vid.add(source);
        }
        self.add(vid);

    }

    public Video resetWithNewSource(String... srcs){
        self.remove(vid);
        createVid(srcs);
        return this;
    }

    public Video play(){
        String js = "document.getElementsByName(\"" + vid.getAttribute("name") + "\")[0].play();";
        vid.executeJs(js);
        return this;
    }

    public Video pause(){
        String js = "document.getElementsByName(\"" + vid.getAttribute("name") + "\")[0].pause();";
        vid.executeJs(js);
        return this;
    }

    public Video addClassName(String... classNames){
        self.addClassName(classNames);
        return this;
    }

    public void setVisible(boolean visible){
        self.setVisible(visible);
    }
}
