package rbender.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.Location;

@Route("/")
public class IndexView extends Composite<Div>{

    public IndexView() {
        Router.getCurrent().navigate(new Location("/webforj/chapter-0/what-is-webforj"));
    }
    
}
