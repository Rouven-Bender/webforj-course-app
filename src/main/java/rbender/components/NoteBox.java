package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.field.TextArea;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.webstorage.LocalStorage;

public class NoteBox extends Composite<FlexLayout>{
    private FlexLayout self = getBoundComponent();
    private LocalStorage localstorage = LocalStorage.getCurrent();    
    private TextArea notebox;

    public NoteBox(){
        notebox = new TextArea();
        notebox.setPlaceholder("Notes");

        self.addClassName("fill-available");
        self.setDirection(FlexDirection.COLUMN);
        self.setStyle("height", "100%");
        self.setStyle("gap", "5px");
        notebox.addClassName("fill-available");
        notebox.setStyle("height", "100%");

        if (hasNotesInLocalStorage()){ loadFromLocalStorage(); }

        self.add(notebox);

        Button save = new Button("Save");
        save.onClick(this::saveToLocalStorage);
        self.add(save);
    }

    public NoteBox saveToLocalStorage(ButtonClickEvent e){
        localstorage.setItem("LessonNotes", notebox.getText());
        return this;
    }
    public NoteBox loadFromLocalStorage(){
        notebox.setText(localstorage.get("LessonNotes"));
        return this;
    }

    public boolean hasNotesInLocalStorage(){
        return !localstorage.getItem("LessonNotes").equals("");
    }
}
