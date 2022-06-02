package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.security.NoSuchAlgorithmException;

import static org.hbrs.se2.project.hellocar.util.Globals.Roles.STUDENT;
import static org.hbrs.se2.project.hellocar.util.Globals.Roles.UNTERNEHMEN;

@Route(value = Globals.Pages.REGISTRATION_VIEW)
@PageTitle("Registration")
@CssImport("./styles/views/register/register-user.css")
public class RegistrationView extends VerticalLayout {

    private RadioButtonGroup<String> userGroup = new RadioButtonGroup<>();

    private EmailField email = new EmailField("E-Mail");
    private PasswordField password = new PasswordField("Passwort");
    private PasswordField confirmPassword = new PasswordField("Passwort bestätigen");
    private TextField firstName = new TextField( "Vorname");
    private TextField lastName = new TextField( "Name");
    private TextField companyName = new TextField( "Unternehmensname");
    private TextField branche = new TextField( "Branche");
    private Checkbox termsOfService = new Checkbox("Hiermit bestätige ich die Endnutzervereinbarung.");

    private Button cancel = new Button("Abbrechen");
    private Button register = new Button("Registrieren");

    private Binder<UserDTOImpl> binder = new Binder(UserDTOImpl.class);

    public RegistrationView(RegistrationControl registrationService) {
        addClassName("register-user");

        add(createTitle());
        userGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        userGroup.setLabel("Benutzer");
        userGroup.setItems(STUDENT, UNTERNEHMEN);
        add(userGroup);
        add(createFormLayout());
        add(createButtonLayout());
        userGroup.addValueChangeListener( event -> {
            if(userGroup.getValue().contains(STUDENT)) {
                removeAll();
                add(createTitle());
                add(createStudentFormLayout());
                add(createButtonLayout());

            } else {
                removeAll();
                add(createTitle());
                add(createCompanyFormLayout());
                add(createButtonLayout());
            }
        });



        // Default Mapping of User attributes and the names of this View based on names
        // Source: https://vaadin.com/docs/flow/binding-data/tutorial-flow-components-binder-beans.html
        binder.forField(userGroup)
                .asRequired("Bitte wählen Sie eine Benutzerrolle.")
                .bind(UserDTOImpl::getRole, UserDTOImpl::setRole);

        binder.forField(password)
                .asRequired("Passwort eingeben")
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);

        binder.forField(confirmPassword)
                .asRequired("passwort bestätigen")
                .bind(UserDTOImpl::getConfirmPassword, UserDTOImpl::setConfirmPassword);

        binder.forField(lastName)
                .asRequired("Nachname eingeben")
                .bind(UserDTOImpl::getLastName, UserDTOImpl::setLastName);

        binder.forField(companyName)
                .asRequired("Unternehmenname eingeben")
                .bind(UserDTOImpl::getCompanyName, UserDTOImpl::setCompanyName);

        binder.forField(branche)
                .asRequired("Branche eingeben")
                .bind(UserDTOImpl::getBranche, UserDTOImpl::setBranche);

        binder.bindInstanceFields(this);
        binder.readBean(new UserDTOImpl());
        clearForm();

        // Registrierung eines Listeners Nr. 1 (moderne Variante mit Lambda-Expression)
        cancel.addClickListener(event -> navigateToLoginPage() );

        termsOfService.setValue(false);
        register.setEnabled(false);
        termsOfService.addValueChangeListener(ev -> {
            boolean value = ev.getValue();
            register.setEnabled(value);
        });

        register.addClickListener(e -> {
            // Speicherung der Daten über das zuhörige Control-Object.
            // Daten des Autos werden aus Formular erfasst und als DTO übergeben.
            // Zusätzlich wird das aktuelle UserDTO übergeben.
            try {
                if(!(binder.getBean().getPassword().equals(binder.getBean().getConfirmPassword()))){
                    Notification.show("Passwörter stimmen nicht überein!");
                }

                if(!(registrationService.isPasswordValid(binder.getBean().getPassword()))){
                    Notification.show("Passwort invalid!");
                }

                registrationService.createUser(binder.getBean());
                navigateToLoginPage();
                Notification.show("Sie haben sich erfolgreich registriert.");
            } catch (DatabaseLayerException | NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void clearForm() {
        binder.setBean(new UserDTOImpl());
    }

    private Component createTitle() {
        return new H2("Registrierung");
    }

    private Component createFormLayout() {
        email.getElement().setAttribute("name", "email");
        email.setValue("random@test.de");
        email.setErrorMessage("Geben Sie bitte eine gültige Emailadresse ein!");
        password.setHelperText("Ein Passwort muss mind. 8 Zeichen lang sein. Es muss aus mind. einem Buchstaben " +
                                "und einer Zahl bestehen.");;
        password.setPattern("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}.*$");
        password.setErrorMessage("Kein valides Passwort!");
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                userGroup,
                email,
                password,
                confirmPassword,
                termsOfService);
        formLayout.setResponsiveSteps( new FormLayout.ResponsiveStep("0",1));
        return formLayout;
    }

    private Component createStudentFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                userGroup,
                email,
                password,
                confirmPassword,
                firstName,
                lastName,
                termsOfService);
        formLayout.setResponsiveSteps( new FormLayout.ResponsiveStep("0",1));
        return formLayout;
    }
    private Component createCompanyFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                userGroup,
                email,
                password,
                confirmPassword,
                companyName,
                branche,
                termsOfService);
        formLayout.setResponsiveSteps( new FormLayout.ResponsiveStep("0",1));
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        buttonLayout.add(register);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private void navigateToLoginPage() {
        // Navigation zur Login-Seite.
        UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);

    }


}
