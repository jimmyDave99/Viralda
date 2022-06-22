package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

@Route(value = Globals.Pages.PROFIL_VIEW, layout = AppView.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/profile.css")
public class ProfilView extends Div {

    private final Tab profile;
    private final Tab securitySettings;
    private final Tab notifications;

    private VerticalLayout content;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button editProfil = new Button("Profil bearbeiten");

    private Label empty = new Label("");

    private TextField firstName;
    private TextField lastName;
    private EmailField email;
    private DatePicker dateOfBirth;
    private TextField role;

    private TextField oldPassword;
    private TextField newPassword;
    private TextField newPasswordAgain;

    private Binder<UserDTOImpl> binder = new Binder(UserDTOImpl.class);

    public ProfilView() {
        addClassName("profile");

        add(createTitle());

        // The different tabs
        profile = new Tab(
                VaadinIcon.USER.create(),
                new Span("Profil")
        );

        securitySettings = new Tab(
                VaadinIcon.COG.create(),
                new Span("Sicherheitseinstellungen")
        );

        notifications = new Tab(
                VaadinIcon.BELL.create(),
                new Span("Benachrichtigungen")
        );

        // Set the icon on top
        for (Tab tab : new Tab[]{profile, securitySettings, notifications}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        Tabs tabs = new Tabs(profile, securitySettings, notifications);

        tabs.addSelectedChangeListener(event -> setContent(event.getSelectedTab()));

        // Content for the tabs
        content = new VerticalLayout();
        content.setSpacing(false);
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }

    // creates the title with the last- and first name of a person or the company name if the user is a company
    private Component createTitle() {
        H2 title = new H2();

        if (getCurrentUser().getRole().equals("Student")) {
            title = new H2("Studentenprofil von " + getCurrentUser().getFirstName() + " " + getCurrentUser().getLastName());
        } else if (getCurrentUser().getRole().equals("Unternehmen")) {
            title = new H2("Unternehmensprofil " + getCurrentUser().getCompanyName());
        } else {
            System.out.println("Error: User is not a student or a company.");
        }

        return title;
    }

    // function to produce the clicked tab
    private void setContent(Tab tab) {
        content.removeAll();

        if (tab.equals(profile)) {
            content.add(createButtonLayoutShowUserAttributes());
            //ToDo: add context for button editProfile, idea: Split Layout, Upload
            content.add(createFormLayoutShowUserAttributes());

        } else if (tab.equals(securitySettings)) {
            content.add(createFormLayoutChangePassword());

        } else if (tab.equals(notifications)) {
            content.add(new Paragraph("Du hast keine neuen Benachrichtigungen!"));
            content.add(new Text("Möchtest du dich nicht bewerben um das zu ändern?"));
            content.add(new Text(" Besser wäre es."));
        }
    }


    // --------------------  functions for tab "Profil" with current attributes of a user  --------------------
    private Component createButtonLayoutShowUserAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(editProfil);
        editProfil.addClickListener(event -> navigateToSubBarEditUserAttributes());

        return buttonLayout;
    }

    private Component createFormLayoutShowUserAttributes() {

        setFieldsShowUserAttributes();
        firstName.setPrefixComponent(new Div(new Text(getCurrentUser().getFirstName())));
        firstName.setEnabled(false);

        lastName.setPrefixComponent(new Div(new Text(getCurrentUser().getLastName())));
        lastName.setEnabled(false);

        email.setPrefixComponent(new Div(new Text(getCurrentUser().getEmail())));
        email.setEnabled(false);

        //ToDo: sobald Geburtsdatum in Datenbank vorhanden ist BDay abfragen
        dateOfBirth.setPlaceholder("'Platzhalter Geburtstag'");
        dateOfBirth.setEnabled(false);

        role.setPrefixComponent(new Div(new Text(getCurrentUser().getRole())));
        role.setEnabled(false);

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstName, lastName,
                email, dateOfBirth,
                role);
        return formLayout;
    }

    private void setFieldsShowUserAttributes() {
        firstName = new TextField("Vorname");
        lastName = new TextField("Name");
        email = new EmailField("E-Mail-Adresse");
        dateOfBirth = new DatePicker("Geburtsdatum");
        role = new TextField("Rolle");
    }


    // --------------------  functions for tab "Profil" with the ability to change the attributes  --------------------
    private Component createButtonLayoutTabProfileEditUserAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(save);
        buttonLayout.add(cancel);

        cancel.addClickListener(event -> navigateToSubBarShowUserAttributesWithoutSave());
        save.addClickListener(event -> navigateToSubBarShowUserAttributesWithSave());

        return buttonLayout;
    }

    private Component createFormLayoutEditUserAttributes() {
        setFieldsEditUserAttributes();

        firstName.setPlaceholder(getCurrentUser().getFirstName());
        lastName.setPlaceholder(getCurrentUser().getLastName());
        email.setPlaceholder(getCurrentUser().getEmail());
        //ToDo: sobald Geburtsdatum in Datenbank vorhanden ist BDay abfragen
        dateOfBirth.setPlaceholder("'Platzhalter Geburtsdatum'");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName,
                email, dateOfBirth);
        return formLayout;
    }

    private void setFieldsEditUserAttributes() {
        firstName = new TextField("Vorname");
        lastName = new TextField("Name");
        email = new EmailField("E-Mail");
        dateOfBirth = new DatePicker("Geburtsdatum");
    }


    // --------------------  functions for tab "Sicherheitseinstellungen"  --------------------
    private Component createFormLayoutChangePassword() {
        setFieldsEditUserPassword();

        FormLayout formLayout = new FormLayout();
        formLayout.add(new H4("Passwort ändern"), new H4(""),
                oldPassword, new H4(""),
                newPassword, new H4(""),
                newPasswordAgain);
        return formLayout;
    }

    //ToDo: Buttonlayout

    private void setFieldsEditUserPassword() {
        oldPassword = new TextField("Altes Passwort");
        newPassword = new TextField("Passwort");
        newPasswordAgain = new TextField("Passwort wiederholen");
    }


    // --------------------  functions to navigate between the tabs  --------------------
    private void navigateToSubBarEditUserAttributes() {
        content.removeAll();

        content.add(createButtonLayoutTabProfileEditUserAttributes());
        content.add(createFormLayoutEditUserAttributes());
    }

    private void navigateToSubBarShowUserAttributesWithoutSave() {
        content.removeAll();

        content.add(createButtonLayoutShowUserAttributes());
        content.add(createFormLayoutShowUserAttributes());
    }

    private void navigateToSubBarShowUserAttributesWithSave() {
        //ToDo: save new input
        if (firstName != null) {
            binder.forField(firstName)
                    .bind(UserDTOImpl::getFirstName, UserDTOImpl::setFirstName);
        } else if (lastName != null) {
            binder.forField(lastName)
                    .bind(UserDTOImpl::getLastName, UserDTOImpl::setLastName);
        } else if (email != null) {
            binder.forField(email)
                    .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        } else if (dateOfBirth != null) {
            //ToDo: sobald Geburtsdatum in Datenbank vorhanden ist anpassen, dass BDay geändert weden kann
        }

        binder.readBean((UserDTOImpl) getCurrentUser());
        clearForm();

        content.removeAll();

        content.add(createButtonLayoutShowUserAttributes());
        content.add(createFormLayoutShowUserAttributes());
    }


    // --------------------  other necessary functions  --------------------
    private void clearForm() {
        binder.setBean((UserDTOImpl) getCurrentUser());
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
