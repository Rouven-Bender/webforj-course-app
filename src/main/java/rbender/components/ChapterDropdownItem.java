package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.ListEntry;
import com.webforj.router.Router;
import com.webforj.router.history.Location;

public class ChapterDropdownItem extends Composite<ListEntry>{
    private ListEntry self = getBoundComponent();
    private String link;

    public ChapterDropdownItem(String itemtext, String link) {
        this.link = link;
        self.setStyle("padding-left", "30px");
        self.setStyle("list-style-position", "inside");
        self.setStyle("margin-top", "5px");
        self.setStyle("cursor", "pointer");
        self.setText(itemtext);
        self.onClick(this::chapterSelected);
    }

    public void toggleVisibility() {
        self.setVisible(!self.isVisible());
    }

    public void chapterSelected(ElementClickEvent<ListEntry> event) {
        event.getComponent();
        Router.getCurrent().navigate(new Location(link));
    }
}
