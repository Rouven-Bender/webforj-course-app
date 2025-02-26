package rbender.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.login.Login;
import com.webforj.component.login.event.LoginSubmitEvent;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.Location;
import com.webforj.webstorage.LocalStorage;

import rbender.controllers.AuthProvider;

@Route("/login")
public class LoginView extends Composite<Div> {
    private Div self = getBoundComponent();
    private Login loginDialog;
    private AuthProvider authProvider;

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
   
    private void onSubmit(LoginSubmitEvent e){
        String username = e.getUsername();
        String pwd = e.getPassword();

        if (authProvider.validateLoginData(username, pwd)){
            LocalStorage.getCurrent().add("username", username).add("token", authProvider.createJWTToken(username));
            Router.getCurrent().navigate(new Location("/"));
            loginDialog.close();
        } else {
            loginDialog.setError(true);
            loginDialog.setEnabled(true);
        }
    }
}