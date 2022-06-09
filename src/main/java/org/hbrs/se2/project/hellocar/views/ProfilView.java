package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.router.*;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;

@Route(value = Globals.Pages.PROFIL_VIEW, layout = AppView.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/profile.css")
public class ProfilView extends Div {

    private final Tab profile;
    private final Tab settings;
    private final Tab notifications;

    private VerticalLayout content;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button editProfil = new Button("Profil bearbeiten");

    private Label empty = new Label("");

    private TextField firstNameShow;
    private TextField lastNameShow;
    private EmailField emailShow;
    private DatePicker dateOfBirthShow;
    private TextField roleShow;

    private TextField firstNameEdit;
    private TextField lastNameEdit;
    private EmailField emailEdit;
    private DatePicker dateOfBirthEdit;
    private TextField oldPasswordEdit;
    private TextField newPasswordEdit;
    private TextField newPasswordAgainEdit;

    private Checkbox savestOptions = new Checkbox("Die besten Sicherheitseinstellungen verwenden.");
    private Checkbox getNotifications = new Checkbox("Benachrichtigungen erhalten.");

    public ProfilView() {
        addClassName("profile");

        add(createTitle());

        // The different tabs
        profile = new Tab(
                VaadinIcon.USER.create(),
                new Span("Profil")
        );

        settings = new Tab(
                VaadinIcon.COG.create(),
                new Span("Einstellungen")
        );

        notifications = new Tab(
                VaadinIcon.BELL.create(),
                new Span("Benachrichtigungen")
        );

        // Set the icon on top
        for (Tab tab : new Tab[] { profile, settings, notifications }) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        Tabs tabs = new Tabs(profile, settings, notifications);

        tabs.addSelectedChangeListener( event -> setContent( event.getSelectedTab() ) );

        // Content for the tabs
        content = new VerticalLayout();
        content.setSpacing(false);
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }

    private Component createTitle() {
        H2 title = new H2();

        if(getCurrentUser().getRole().equals("Student")) {
            title = new H2("Studentenprofil von " + getCurrentUser().getFirstName() + " " + getCurrentUser().getLastName());
        } else if (getCurrentUser().getRole().equals("Unternehmen")) {
            title = new H2("Unternehmensprofil " + getCurrentUser().getCompanyName());
        } else {
            System.out.println("Error: User is not a student or a company.");
        }

        return title;
    }

    private void setContent( Tab tab ) {
        content.removeAll();

        if ( tab.equals( profile ) ) {
            content.add(createButtonLayoutShowProfile());
            //ToDo: add context for button editProfile, idea: Split Layout, Upload
            content.add(createFormLayoutShowProfile());

        } else if ( tab.equals( settings ) ) {
            content.add(new H4("Sicherheitseinstellungen"));
            content.add(savestOptions);
            content.add(new H4("Privatssphäreeinstellungen"));
            content.add(getNotifications);
            content.add(new H4("Sonstiges"));

        } else if ( tab.equals( notifications ) ) {
            content.add(new Paragraph("Du hast keine neuen Benachrichtigungen!"));
            content.add(new Text("Möchtest du dich nicht bewerben um das zu ändern?"));
            content.add(new Text(" Besser wäre es."));
        }
    }



    private Component createButtonLayoutShowProfile() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(editProfil);
        editProfil.addClickListener(event  -> navigateToSubBarEditProfile());

        return buttonLayout;
    }

    private Component createButtonLayoutTabProfileEdit() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(save);
        buttonLayout.add(cancel);

        cancel.addClickListener(event -> navigateToSubBarShowProfilWithoutSave() );
        save.addClickListener(event -> navigateToSubBarShowProfilWithSave());

        return buttonLayout;
    }

    private Component createFormLayoutShowProfile() {

        setFieldsShow();
        firstNameShow.setPrefixComponent(new Div(new Text(getCurrentUser().getFirstName())));
        firstNameShow.setEnabled(false);

        lastNameShow.setPrefixComponent(new Div(new Text(getCurrentUser().getLastName())));
        lastNameShow.setEnabled(false);

        emailShow.setPrefixComponent(new Div(new Text(getCurrentUser().getEmail())));
        emailShow.setEnabled(false);

        //ToDo: sobald Geburtsdatum in Datenbank vorhanden ist BDay abfragen
        dateOfBirthShow.setPlaceholder("'Platzhalter Geburtstag'");
        dateOfBirthShow.setEnabled(false);

        roleShow.setPrefixComponent(new Div(new Text(getCurrentUser().getRole())));
        roleShow.setEnabled(false);

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstNameShow, lastNameShow,
                emailShow, dateOfBirthShow,
                roleShow);
        return formLayout;
    }

    private Component createFormLayoutEditProfile() {

        setFieldsEdit();
        firstNameEdit.setPlaceholder(getCurrentUser().getFirstName());
        lastNameEdit.setPlaceholder(getCurrentUser().getLastName());
        emailEdit.setPlaceholder(getCurrentUser().getEmail());
        //ToDo: sobald Geburtsdatum in Datenbank vorhanden ist BDay abfragen
        dateOfBirthEdit.setPlaceholder("'Platzhalter Geburtsdatum'");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameEdit, lastNameEdit,
                emailEdit, dateOfBirthEdit,
                new H4("Passwort ändern"), new H4(""),
                oldPasswordEdit, new H4(""),
                newPasswordEdit, new H4(""),
                newPasswordAgainEdit);
        return formLayout;
    }

    private void navigateToSubBarEditProfile() {
        content.removeAll();

        content.add(createButtonLayoutTabProfileEdit());
        content.add(createFormLayoutEditProfile());
    }

    private void navigateToSubBarShowProfilWithoutSave() {
        content.removeAll();

        content.add(createButtonLayoutShowProfile());
        content.add(createFormLayoutShowProfile());
    }

    private void navigateToSubBarShowProfilWithSave() {
        content.removeAll();

        //ToDo: save new input

        content.add(createButtonLayoutShowProfile());
        content.add(createFormLayoutShowProfile());
    }

    private void setFieldsShow() {
        firstNameShow = new TextField("Vorname");
        lastNameShow = new TextField("Name");
        emailShow = new EmailField("E-Mail-Adresse");
        dateOfBirthShow = new DatePicker("Geburtsdatum");
        roleShow = new TextField("Rolle");
    }

    public void setFieldsEdit() {
        firstNameEdit = new TextField( "Vorname");
        lastNameEdit = new TextField( "Name");
        emailEdit = new EmailField("E-Mail");
        dateOfBirthEdit = new DatePicker("Geburtsdatum");
        oldPasswordEdit = new TextField("Altes Passwort");
        newPasswordEdit = new TextField("Passwort");
        newPasswordAgainEdit = new TextField("Passwort wiederholen");
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
