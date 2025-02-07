package rbender.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.ListEntry;

public class ChapterDropdownItem extends Composite<ListEntry>{
    ListEntry self = getBoundComponent();

    public ChapterDropdownItem(String itemtext) {
        self.setStyle("padding-left", "30px");
        self.setStyle("list-style-position", "inside");
        self.setStyle("margin-top", "5px");
        self.setText(itemtext);
    }

    public void toggleVisibility() {
        self.setVisible(!self.isVisible());
    }
}
