package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.UnorderedList;
import com.webforj.component.icons.FeatherIcon;

import rbender.types.Chapter;

public class ChapterDropdown extends Composite<UnorderedList>{
    private UnorderedList self = getBoundComponent();
    private Button fold;
    private boolean folded = false;

    public ChapterDropdown(Chapter c){
        self.setStyle("padding-left", "20px");
        self.setStyle("margin-top", "7px");
        self.setStyle("margin-bottom", "7px");

        Div header = new Div();
        header.setStyle("display", "flex");

        Paragraph text = new Paragraph(c.name);

        fold = new Button();
        fold.setIcon(FeatherIcon.CHEVRON_DOWN.create());
        fold.setStyle("margin-left", "auto");
        fold.setStyle("padding-right", "15px");
        fold.addClassName("chapter-fold-btn");
        fold.addClickListener(this::foldChapter);
        fold.setVisible(false);

        header.add(text, fold);

        self.add(header);
    }

    public void foldChapter(ButtonClickEvent event){
        self.getComponents().forEach(e -> {
            try {
                ChapterDropdownItem item = (ChapterDropdownItem) e;
                item.toggleVisibility();
            } catch (ClassCastException ex) {} // because the first item is a div we don't care about that
        });
        if (folded) {
            fold.setIcon(FeatherIcon.CHEVRON_DOWN.create());
            folded = !folded;
        } else {
            fold.setIcon(FeatherIcon.CHEVRON_RIGHT.create());
            folded = !folded;
        }
    }

    public ChapterDropdown add(ChapterDropdownItem... items) {
        self.add(items);
        if (!fold.isVisible()) { fold.setVisible(true); }
        return this;
    }
}
