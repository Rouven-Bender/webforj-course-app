package rbender.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.annotation.Route;

@Route(value = "/account/redeem", outlet = AccountLayout.class, priority = 9)
public class RedeemCourseView extends Composite<Div>{
    private Div self = getBoundComponent();

    public RedeemCourseView(){} 
}
