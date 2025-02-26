package rbender.views;

import java.util.Optional;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Nav;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.Location;

import rbender.components.ChapterDropdownItem;
import rbender.controllers.AuthProvider;

@Route("/account")
public class AccountLayout extends Composite<AppLayout> {
    private AppLayout self = getBoundComponent();
    private AuthProvider auth = null;

    public AccountLayout(){
        Optional<AuthProvider> a = AuthProvider.getInstance();
        if (a.isPresent()) {
            auth = a.get();
        } else {
            Router.getCurrent().navigate(new Location("/login")); //TODO: change to the 500 page later
        }
        Optional<String> username = auth.getLogdinUsername();
        if (username.isEmpty()) {
            Router.getCurrent().navigate(new Location("/login"));
        }
        setHeader();
        setDrawer();
    }
    
    private void setHeader(){
        self.addToHeader(new Div(
            new AppDrawerToggle()
            ).setStyle("padding", "8px")
        );
    }

    private void setDrawer(){
        self.setDrawerHeaderVisible(true);
        self.addToDrawerTitle(FeatherIcon.USER.create().setHeight("70px"));

        Nav nav = new Nav();
        nav.setStyle("padding-top", "20px");

        nav.add(
            new ChapterDropdownItem("Redeem Course", "/account/redeem")
        );

        self.addToDrawer(nav);
    }
}
