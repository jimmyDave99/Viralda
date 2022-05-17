package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
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
import org.hbrs.se2.project.hellocar.util.Globals;

@Route(value = Globals.Pages.PROFIL_VIEW, layout = AppView.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/profile.css")
public class ProfilView extends Div {

    private final Tab profile;
    private final Tab settings;
    private final Tab notifications;

    private final VerticalLayout content;

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button editProfil = new Button("Profil bearbeiten");

    private EmailField email = new EmailField("E-Mail");
    private TextField password = new TextField("Passwort");
    private TextField passwordAgain = new TextField("Passwort wiederholen");
    private TextField firstName = new TextField( "Vorname");
    private TextField lastName = new TextField( "Name");
    private DatePicker dateOfBirth = new DatePicker("Geburtsdatum");

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

    private Component createTitle() { return new H2("Profil von 'Name' 'Nachname' ");
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
        HorizontalLayout buttonLayout = new HorizontalLayout();

        buttonLayout.add(editProfil);
        editProfil.addClickListener(event  -> navigateToSubBarEditProfile());

        return buttonLayout;
    }

    private Component createButtonLayoutTabProfileEdit() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        //buttonLayout.addClassName("button-layout-tab-profile");
        buttonLayout.add(save);
        buttonLayout.add(cancel);

        cancel.addClickListener(event -> navigateToSubBarShowProfilWithoutSave() );
        save.addClickListener(event -> navigateToSubBarShowProfilWithSave());
        return buttonLayout;
    }

    private Component createFormLayoutShowProfile() {
        //email.getElement().setAttribute("name", "email");

        FormLayout formLayout = new FormLayout();

        return formLayout;
    }

    private Component createFormLayoutEditProfile() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(email, firstName, password, lastName, passwordAgain, dateOfBirth);
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
    }

    private void navigateToSubBarShowProfilWithSave() {
        content.removeAll();

        //ToDo: save new input

        content.add(createButtonLayoutShowProfile());
    }
}
