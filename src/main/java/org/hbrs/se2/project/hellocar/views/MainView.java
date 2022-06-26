package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.hbrs.se2.project.hellocar.control.LoginControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseUserException;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 */
@Route(value = "")
@RouteAlias(value = "login")
public class MainView extends VerticalLayout {

    @Autowired
    private LoginControl loginControl;

    public MainView() {
        setSizeFull();

        LoginForm loginForm =  new LoginForm();
        loginForm.setI18n(createLoginFormGerman());

        loginForm.addLoginListener(e -> {

            boolean isAuthenticated = false;
            try {
                isAuthenticated = loginControl.authentificate( e.getUsername() , e.getPassword() );

            } catch (DatabaseUserException databaseException) {
                Dialog dialog = new Dialog();
                dialog.add( new Text( databaseException.getReason()) );
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                ex.printStackTrace();
            }
            if (isAuthenticated) {
                grabAndSetUserIntoSession();
                navigateToMainPage();

            } else {
                // Kann noch optimiert werden
                loginForm.setError(true);
            }
        });

        Button registration = new Button("Registrieren");
        registration.addClickListener( event -> navigateToRegistrationPage());
        add(loginForm, registration);
        this.setAlignItems( Alignment.CENTER );
    }

    private LoginI18n createLoginFormGerman(){
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());

        LoginI18n.Header i18nHeader = i18n.getHeader();
        i18nHeader.setTitle("Coll@H-BRS");
        i18nHeader.setDescription("Ihre Plattform für regionales Networking");

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Anmeldung");
        i18nForm.setUsername("Emailadresse");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Passwort vergessen?");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Überprüfen Sie ihre Emailadresse oder ihr Passwort!");
        i18nErrorMessage.setMessage("Versuchen Sie Ihre Anmeldedaten nochmals einzugeben.");
        i18n.setErrorMessage(i18nErrorMessage);
        i18n.setAdditionalInformation("Sie haben noch keinen Account? Hier geht's zur Registrierung.");

        return i18n;
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute( Globals.CURRENT_USER, userDTO );
    }


    private void navigateToMainPage() {
        // Navigation zur Startseite.
        UserDTO userDTO = loginControl.getCurrentUser();
        if(userDTO.getRole().equals(Globals.Roles.STUDENT))
            UI.getCurrent().navigate(Globals.Pages.LANDING_PAGE_STUDENT_VIEW);
        if(userDTO.getRole().equals(Globals.Roles.UNTERNEHMEN))
            UI.getCurrent().navigate(Globals.Pages.LANDING_PAGE_COMPANY_VIEW);
    }

    private void navigateToRegistrationPage() {
        // Navigation zur Registrierung.
        UI.getCurrent().navigate(Globals.Pages.REGISTRATION_VIEW);

    }
}
