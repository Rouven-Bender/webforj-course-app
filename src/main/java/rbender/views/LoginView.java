package rbender.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.login.Login;
import com.webforj.component.login.event.LoginSubmitEvent;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.Location;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;
import com.webforj.webstorage.LocalStorage;

import rbender.controllers.AuthProvider;

@Route("/login/:next*")
public class LoginView extends Composite<Div> implements DidEnterObserver{
    private Div self = getBoundComponent();
    private Login loginDialog;
    private AuthProvider authProvider;
    private Destination dest;

    public LoginView(){
        AuthProvider.getInstance().ifPresentOrElse(
            (e) -> {
                authProvider = e;

                loginDialog = new Login();
                self.add(loginDialog);
                loginDialog.open();
                loginDialog.onSubmit(this::onSubmit);
            },
            () -> {
                self.add(new Paragraph("Something went wrong and the login is unavailable"));
            }
        );
    }

    @Override
    public void onDidEnter(DidEnterEvent event, ParametersBag params){
        String next = params.get("next").orElse("");
        String[] blocks = next.split("\\/");
        if (blocks.length == 3){
            dest = new Destination();
            dest.kind = SendTo.LESSON;
            dest.which = next;
        } else {
            dest = new Destination();
            dest.kind = SendTo.INDEX;
            dest.which = "";
        }
    }
    
    private void onSubmit(LoginSubmitEvent e){
        String username = e.getUsername();
        String pwd = e.getPassword();

        if (authProvider.validateLoginData(username, pwd)){
            LocalStorage.getCurrent().add("username", username).add("token", authProvider.createJWTToken(username));
            if (dest != null) {
                if (dest.kind == SendTo.INDEX) {
                    Router.getCurrent().navigate(new Location("/"));
                }
                Router.getCurrent().navigate(new Location(dest.which));
            } else {
                Router.getCurrent().navigate(new Location("/"));
            }
        } else {
            loginDialog.setError(true);
            loginDialog.setEnabled(true);
        }
    }
}

class Destination {
    public SendTo kind;
    public String which;
}
enum SendTo {
    INDEX,
    COURSE, // maybe we are adding an index welcome page for courses later
    LESSON,
}