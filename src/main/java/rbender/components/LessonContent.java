package rbender.components;

import java.io.IOException;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

import rbender.Application;
import rbender.controllers.CourseDataProvider;

public class LessonContent extends Composite<FlexLayout>{
    private FlexLayout self = getBoundComponent();
    private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();

    private String url;

    private Div videoTranscriptContainer = new Div();

    private Video vidPlayer;
    private Transcript transcript;
    private NoteBox notebox = new NoteBox();

    public LessonContent(String url){
        this.url = url;
        
        self.setDirection(FlexDirection.ROW);
        self.addClassName("fill-available");
        self.setHeight("100%");
        self.setStyle("gap", "5px");

        createLeftPlane();
        createRightPlane();
    }

    private void createLeftPlane(){
        String videolink = courseDataProvider.getVideoLink(url);
        videoTranscriptContainer.addClassName("container");
        vidPlayer = new Video(videolink);
        videoTranscriptContainer.add(vidPlayer);
        try {
            String markdown = Application.getResourceAsString(courseDataProvider.getTranscript(url)).orElse("");
            transcript = new Transcript(markdown);
        } catch (IOException e) {}
        videoTranscriptContainer.add(transcript);

        self.add(videoTranscriptContainer);
    }
    
    private void createRightPlane() {
        self.add(notebox);
    }
}
