package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.Div;

public class Transcript extends Composite<Div>{
    private Div self = getBoundComponent();
    private TextArea transcript;

    public Transcript(String text){
        transcript = new TextArea();
        transcript.setText(text);
        transcript.setReadOnly(true);
        transcript.setVerticalScroll(true);
        transcript.addClassName("fill-available");

        self.setStyle("height", "100%");
        transcript.setStyle("height", "100%");

        self.add(transcript);
    }

    public Transcript setVisible(boolean visible){
        self.setVisible(visible);
        return this;
    }
    
    public Transcript setText(String Text){
        transcript.setText(Text);
        return this;
    }
}
