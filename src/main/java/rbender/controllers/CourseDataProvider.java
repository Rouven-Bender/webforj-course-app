package rbender.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

import rbender.Application;
import rbender.types.*;

public class CourseDataProvider {
    private static CourseDataProvider instance = null;
    private Map<String, Lesson> LessonLookupByLink = new HashMap<String, Lesson>();
    private Map<String, Course> courseLookupByLink = new HashMap<String, Course>();

    private CourseDataProvider(){
        try {
        String content = Application.getResourceAsString("courseData/courses.json").orElse("");
        Gson gson = new Gson();
        Course[] courses = gson.fromJson(content, StorageFile.class).courses;

        for (Course c : courses) {
            courseLookupByLink.put(c.url, c);
            for (Chapter ch : c.chapters) {
                for (Lesson l : ch.lessons) {
                    String link = 
                        "/" + c.url.replace("/", "") +
                        "/" + ch.url.replace("/", "") +
                        "/"+ l.url.replace("/", "");
                    LessonLookupByLink.put(link, l);
                }
            }
        }
        } catch (IOException e) {
            //TODO: Logging
        }
    }

    public Optional<Chapter[]> getChapters(String course){
        Optional<Course> c = Optional.ofNullable(courseLookupByLink.get(course));
        if (c.isPresent()){
            return Optional.of(c.get().chapters);
        } else {
            return Optional.empty();
        }
    }

    public boolean isLessonLink(String link){
        return LessonLookupByLink.containsKey(link);
    }
    
    /**
     * returns the link to the video of the lesson
     * @param lessonLink
     * @return
     */
    public String getVideoLink(String lessonLink){
        Lesson value = LessonLookupByLink.get(lessonLink);
        if (value != null){
            return value.video;
        } else {
            return "";
        }
    }
    
    /**
     * returns the path of the transcript file
     * @param lessonLink
     * @return
     */
    public String getTranscript(String lessonLink){
        Lesson value = LessonLookupByLink.get(lessonLink);
        if (value != null){
            return value.transcript;
        } else {
            return "";
        }
    }

    public static synchronized CourseDataProvider getInstance(){
        if (instance == null) {
            instance = new CourseDataProvider();
        }
        return instance;
    }
}

class StorageFile{
    public Course[] courses;
}