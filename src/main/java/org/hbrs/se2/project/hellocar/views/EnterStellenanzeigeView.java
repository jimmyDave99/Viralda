package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;


//import javax.management.Notification;

@Route(value = Globals.Pages.ENTER_STELLENANZEIGE_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeige erstellen")
@CssImport("./styles/views/enterstellenanzeige/enter-stellenanzeige-view.css")
public class EnterStellenanzeigeView extends Div {

    private TextField titel = new TextField("Titel");
    private TextField bereich = new TextField("Bereich");
    private TextArea beschreibung = new TextArea("Beschreibung");
    private DatePicker einstellungsdatum = new DatePicker("Einstellungsdatum");
    private NumberField gehalt = new NumberField("Gehalt");
    private NumberField wochenstunden = new NumberField("Wochenstunden");

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button editStellenanzeige = new Button("Stellenanzeige bearbeiten");

    private Binder<StellenanzeigeDTOImpl> binder = new Binder(StellenanzeigeDTOImpl.class);

    public EnterStellenanzeigeView(JobApplicationControl jobApplicationControl) {
        addClassName("enter-stellenanzeige-view");

        add(createTitle());

        add(createFormLayout());
        add(createButtonLayout());

        // Binder
        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(event -> clearForm());

        save.addClickListener(e -> {
            UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
            // TODO: 17.06.22 create-method in jobApplicationControl
            jobApplicationControl.createStellenanzeige(binder.getBean(), userDTO);

            Notification.show("Stellenanzeige mit den Angegebenen Details wurden gespeichert!");
        });
    }

    private void clearForm() {
        binder.setBean(new StellenanzeigeDTOImpl());
    }

    private Component createTitle() {
        return new H2("Stellenanzeige Angaben/Details");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(titel, bereich, beschreibung, einstellungsdatum, gehalt, wochenstunden);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    /*private void setContent( Tab tab ) {
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
    }*/



    /*private Component createButtonLayoutShowProfile() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(editProfil);
        editProfil.addClickListener(event  -> navigateToSubBarEditProfile());

        return buttonLayout;
    }*/

    /*private Component createButtonLayoutTabProfileEdit() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(save);
        buttonLayout.add(cancel);

        cancel.addClickListener(event -> navigateToSubBarShowProfilWithoutSave() );
        save.addClickListener(event -> navigateToSubBarShowProfilWithSave());

        return buttonLayout;
    }*/

    /*private Component createFormLayoutShowProfile() {

        firstNameShow.setPrefixComponent(new Div(new Text("'Platzhalter Vorname'")));
        firstNameShow.setEnabled(false);

        lastNameShow.setPrefixComponent(new Div(new Text("'Platzhalter Name'")));
        lastNameShow.setEnabled(false);

        emailShow.setPrefixComponent(new Div(new Text("'Platzhalter E-Mail-Adresse'")));
        emailShow.setEnabled(false);

        dateOfBirthShow.setPlaceholder("'Platzhalter Geburtstag'");
        dateOfBirthShow.setEnabled(false);

        roleShow.setPrefixComponent(new Div(new Text("'Platzhalter Rolle'")));
        roleShow.setEnabled(false);

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstNameShow, lastNameShow,
                emailShow, dateOfBirthShow,
                roleShow);
        return formLayout;
    }*/

    /*private Component createFormLayoutEditProfile() {

        firstNameEdit.setPlaceholder("'Platzhalter Vorname'");
        lastNameEdit.setPlaceholder("'Platzhalter Name'");
        emailEdit.setPlaceholder("'Platzhalter E-Mail-Adresse'");
        dateOfBirthEdit.setPlaceholder("'Platzhalter Geburtsdatum'");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameEdit, lastNameEdit,
                emailEdit, dateOfBirthEdit,
                new H4("Passwort ändern"), new H4(""),
                oldPasswordEdit, new H4(""),
                newPasswordEdit, new H4(""),
                newPasswordAgainEdit);
        return formLayout;
    }*/

    /*private void navigateToSubBarEditProfile() {
        content.removeAll();

        content.add(createButtonLayoutTabProfileEdit());
        content.add(createFormLayoutEditProfile());
    }*/

    /*private void navigateToSubBarShowProfilWithoutSave() {
        content.removeAll();

        content.add(createButtonLayoutShowProfile());
        content.add(createFormLayoutShowProfile());
    }*/

    /*private void navigateToSubBarShowProfilWithSave() {
        content.removeAll();

        //ToDo: save new input

        content.add(createButtonLayoutShowProfile());
        content.add(createFormLayoutShowProfile());
    }*/
}
