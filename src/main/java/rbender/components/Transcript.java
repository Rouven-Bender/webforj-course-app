package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.Div;

public class Transcript extends Composite<Div>{
    private Div self = getBoundComponent();

    public Transcript(String text){
        TextArea transcript = new TextArea();
        transcript.setText(text);
        transcript.setReadOnly(true);
        transcript.setVerticalScroll(true);
        transcript.addClassName("Transcript");
        transcript.addClassName("fill-available");

        self.setStyle("height", "100%");
        transcript.setStyle("height", "100%");

        self.add(transcript);
    }
}
