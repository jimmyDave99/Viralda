package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

@Route(value = Globals.Pages.REGISTRATION_VIEW)
@PageTitle("Registration")
@CssImport("./styles/views/register/register-user.css")
public class RegistrationView extends VerticalLayout {

    private TextField email = new TextField("E-Mail");
    private TextField password = new TextField("Passwort");
    private TextField firstName = new TextField( "Vorname");
    private TextField lastName = new TextField( "Name");
    private DatePicker dateOfBirth = new DatePicker("Geburtsdatum");

    private Button cancel = new Button("Abbrechen");
    private Button register = new Button("Registrieren");

    private Binder<UserDTOImpl> binder = new Binder(UserDTOImpl.class);

    public RegistrationView(RegistrationControl registrationService) {
        addClassName("register-user");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        // Default Mapping of User attributes and the names of this View based on names
        // Source: https://vaadin.com/docs/flow/binding-data/tutorial-flow-components-binder-beans.html
        binder.bindInstanceFields(this);
        clearForm();

        // Registrierung eines Listeners Nr. 1 (moderne Variante mit Lambda-Expression)
        cancel.addClickListener(event -> clearForm());

        // Registrierung eines Listeners Nr. 2 (traditionelle Variante mit anonymen Objekt)
        cancel.addAttachListener( new ComponentEventListener() {
            @Override
            public void onComponentEvent(ComponentEvent event) {
                clearForm();
            }
        } );

        register.addClickListener(e -> {
            // Speicherung der Daten über das zuhörige Control-Object.
            // Daten des Autos werden aus Formular erfasst und als DTO übergeben.
            // Zusätzlich wird das aktuelle UserDTO übergeben.
            registrationService.createUser( binder.getBean() );
            Notification.show("Sie haben sich erfolgreich registriert.");
            clearForm();
            navigateToLoginPage();
        });
    }

    private void clearForm() {
        binder.setBean(new UserDTOImpl());
    }

    private Component createTitle() {
        return new H3("Registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(email, firstName, lastName, password, dateOfBirth);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(register);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private void navigateToLoginPage() {
        // Navigation zur Login-Seite.
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);

    }

}
