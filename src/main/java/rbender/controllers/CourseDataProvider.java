package rbender.controllers;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import rbender.Application;
import rbender.types.Chapter;
import rbender.types.CourseData;
import rbender.types.Lesson;

public class CourseDataProvider {
    private static CourseDataProvider instance = null;
    private HashMap<String, String> videolinks = new HashMap<>();
    private CourseData courseData;

    private CourseDataProvider(){
        try {
            String content = Application.getResourceAsString("static/courseData.json");
            Gson gson = new Gson();
            courseData = gson.fromJson(content, CourseData.class);

            for (Chapter c : courseData.chapters) {
                for (Lesson l : c.lessons) {
                    String link = "/" + c.urlprefix.replace("/", "") +"/"+ l.url.replace("/", "");
                    videolinks.put(link, l.video);
                }
            }
        } catch (IOException e) {
            //TODO: Logging
        }
    }

    public Chapter[] getChapters(){
        return courseData.chapters.clone();
    }

    public boolean isLessonLink(String link){
        return videolinks.containsKey(link);
    }
    
    public String getVideoLink(String lessonLink){
        return videolinks.get(lessonLink);
    }

    public static synchronized CourseDataProvider getInstance(){
        if (instance == null) {
            instance = new CourseDataProvider();
        }
        return instance;
    }

}
