package rbender.views;

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

import rbender.components.Video;
import rbender.controllers.CourseDataProvider;

@Route(value = "/:chapter/:lesson", outlet = MainLayout.class)
public class LessonView extends Composite<FlexLayout> implements DidEnterObserver{
    private FlexLayout self = getBoundComponent();
    private CourseDataProvider courseDataProvider = CourseDataProvider.getInstance();

    private FlexLayout fourOfourContainer;
    private Div videocontainer;

    private Video vidPlayer;

    public LessonView(){
        Router.getCurrent().onNavigate(this::onNavigate);
    }

    private void createVideoplayer(String videolink){
        videocontainer = new Div();
        videocontainer.addClassName("container");
        vidPlayer = new Video(videolink);
        vidPlayer.addClassName("VideoPlayer");
        videocontainer.add(vidPlayer);
        self.add(videocontainer);
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
        self.add(fourOfourContainer);
    }

    @Override
    public void onDidEnter(DidEnterEvent event, ParametersBag params) {
        String chapter = params.get("chapter").orElse("");
        String lesson = params.get("lesson").orElse("");

        if (!chapter.equals("") && !lesson.equals("")){
            String link = "/" + chapter + "/" + lesson;
            if (courseDataProvider.isLessonLink(link)) {
                createVideoplayer(courseDataProvider.getVideoLink(link));
            } else { create404Message(); }
        } else { create404Message(); }
    }

    private void onNavigate(NavigateEvent ev) {
        String link = ev.getLocation().getFullURI();
        if (resolvesToThisView(link)){
            if (courseDataProvider.isLessonLink(link)){
                if(videocontainer != null){
                    if (fourOfourContainer != null){ fourOfourContainer.setVisible(false);}
                    vidPlayer.resetWithNewSource(courseDataProvider.getVideoLink(link));
                    videocontainer.setVisible(true);
                } else {
                    if (fourOfourContainer != null){ fourOfourContainer.setVisible(false);}
                    createVideoplayer(courseDataProvider.getVideoLink(link));
                }
            } else {
                if (fourOfourContainer != null){
                    if (videocontainer != null){ vidPlayer.pause(); videocontainer.setVisible(false); }
                    fourOfourContainer.setVisible(true);
                } else {
                    if (videocontainer != null){ vidPlayer.pause(); videocontainer.setVisible(false); }
                    create404Message();
                    fourOfourContainer.setVisible(true);
                }
            }
        }
    }

    private boolean resolvesToThisView(String link){
        return link.matches("^\\/.{1,}\\/.{1,}$");
    }
}
