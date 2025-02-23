package rbender.views;

import java.io.IOException;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexContentAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

import rbender.Application;
import rbender.components.NoteBox;
import rbender.components.Transcript;
import rbender.components.Video;
import rbender.controllers.CourseDataProvider;

@Route(value = "/:course/:chapter/:lesson", outlet = MainLayout.class)
public class LessonView extends Composite<FlexLayout> implements DidEnterObserver{
    private FlexLayout self = getBoundComponent();
    private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();
    private Div outerContainer = new Div();
    private Div videoTranscriptContainer = new Div();

    private FlexLayout fourOfourContainer;

    private Video vidPlayer;
    private Transcript transcript;

    public LessonView(){
        self.add(outerContainer);
        self.setStyle("height", "100%");
        outerContainer.add(videoTranscriptContainer);
        outerContainer.setStyle("display", "flex");
        outerContainer.setStyle("flex-direction", "row");
        outerContainer.addClassName("fill-available");
        outerContainer.setStyle("height", "100%");
        outerContainer.setStyle("gap", "5px");

        Router.getCurrent().onNavigate(this::onNavigate);
    }

    private void createVideoplayer(String lessonLink){
        String videolink = courseDataProvider.getVideoLink(lessonLink);
        videoTranscriptContainer.addClassName("container");
        vidPlayer = new Video(videolink);
        videoTranscriptContainer.add(vidPlayer);
        try {
            String markdown = Application.getResourceAsString(courseDataProvider.getTranscript(lessonLink)).orElse("");
            transcript = new Transcript(markdown);
        } catch (IOException e) {}
        videoTranscriptContainer.add(transcript);
        outerContainer.add(new NoteBox());
    }
    
    private void create404Message(){
        fourOfourContainer = new FlexLayout();
        fourOfourContainer.setAlignContent(FlexContentAlignment.CENTER);
        fourOfourContainer.setStyle("flex-direction", "column");
        fourOfourContainer.setStyle("flex-wrap", "nowrap"); // setFlow with column_nowrap became row_nowrap for some reason
        fourOfourContainer.setStyle("margin", "auto");
        H1 fourOfour = new H1("404");
        fourOfour.setStyle("margin", "auto");
        fourOfour.setStyle("color", "var(--dwc-color-primary-text)");
        fourOfour.setStyle("font-size", "80px");
        Paragraph text = new Paragraph("We don't have the Knowleadge you are looking for");
        text.setStyle("font-size", "18px");
        text.setStyle("margin", "auto");

        fourOfourContainer.add(fourOfour,text);
        outerContainer.add(fourOfourContainer);
    }

    @Override
    public void onDidEnter(DidEnterEvent event, ParametersBag params) {
        String course = params.get("course").orElse("");
        String chapter = params.get("chapter").orElse("");
        String lesson = params.get("lesson").orElse("");

        if (!course.equals("") && !chapter.equals("") && !lesson.equals("")){
            String link = 
                "/" + course 
              + "/" + chapter 
              + "/" + lesson;
            if (courseDataProvider.isLessonLink(link)) {
                createVideoplayer(link);
            } else { create404Message(); }
        } else { create404Message(); }
    }

    private void onNavigate(NavigateEvent ev) {
        String link = ev.getLocation().getFullURI();
        if (resolvesToThisView(link)){
            if (courseDataProvider.isLessonLink(link)){
                if(vidPlayer != null){
                    if (fourOfourContainer != null){ fourOfourContainer.setVisible(false);}
                    vidPlayer.resetWithNewSource(courseDataProvider.getVideoLink(link));
                    vidPlayer.setVisible(true);
                    try {
                        transcript.setText(Application.getResourceAsString(courseDataProvider.getTranscript(link)).orElse(""));
                        transcript.setVisible(true);
                    } catch (IOException e) { }
                } else {
                    if (fourOfourContainer != null){ fourOfourContainer.setVisible(false);}
                    createVideoplayer(link);
                }
            } else {
                if (fourOfourContainer != null){
                    if (vidPlayer != null){ vidPlayer.pause(); vidPlayer.setVisible(false); }
                    if (transcript != null) { transcript.setVisible(false); }
                    fourOfourContainer.setVisible(true);
                } else {
                    if (vidPlayer != null){ vidPlayer.pause(); vidPlayer.setVisible(false); }
                    if (transcript != null) { transcript.setVisible(false); }
                    create404Message();
                    fourOfourContainer.setVisible(true);
                }
            }
        }
    }

    private boolean resolvesToThisView(String link){
        return link.matches("^\\/.{1,}\\/.{1,}\\/.{1,}$");
    }
}
