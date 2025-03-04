package rbender.views;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.login.Login;
import com.webforj.component.login.LoginI18n;
import com.webforj.component.login.event.LoginSubmitEvent;
import com.webforj.component.toast.Toast;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.Location;

import rbender.controllers.AuthProvider;

@Route("register")
public class RegisterView extends Composite<Div> {
    private Div self = getBoundComponent();
    private Login registerDialog;
    private AuthProvider auth;
    
    public RegisterView(){
        AuthProvider.getInstance().ifPresentOrElse(
            (e) -> {
                this.auth = e;
                registerDialog = new Login();
                registerDialog.addClassName("registrationDialog");
                LoginI18n registrationRebrand = new LoginI18n();
                registrationRebrand.setTitle("Registration");
                registrationRebrand.setSubmit("Register");
                registerDialog.setI18n(registrationRebrand);

                registerDialog.setRememberme(false);
                registerDialog.setName("");

                registerDialog.onSubmit(this::registerClicked);

                self.add(registerDialog);
                registerDialog.open();
            },
            () -> {

            }
        );
    }

    private void registerClicked(LoginSubmitEvent event) {
        if (auth.registerNewUser(event.getUsername(), event.getPassword())) {
            registerDialog.close();
            Toast.show("Registration successful", Theme.SUCCESS);
            Router.getCurrent().navigate(new Location("/login"));
        }
    }
}
