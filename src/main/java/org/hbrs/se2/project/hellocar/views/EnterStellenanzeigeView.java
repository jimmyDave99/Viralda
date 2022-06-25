package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import com.vaadin.flow.component.dependency.CssImport;

import java.time.LocalDate;
import java.time.ZoneId;


@Route(value = Globals.Pages.ENTER_STELLENANZEIGE_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeige erstellen")
@CssImport("./styles/views/enterstellenanzeige/enter-stellenanzeige-view.css")
public class EnterStellenanzeigeView extends Div {

    private TextField titel = new TextField("Titel");
    private TextField bereich = new TextField("Bereich");
    private TextArea beschreibung = new TextArea("Beschreibung");
    private DatePicker einstellungsdatum = new DatePicker("Einstellungsdatum");
    private LocalDate now = LocalDate.now(ZoneId.systemDefault());
    private NumberField gehalt = new NumberField("Gehalt");
    private NumberField wochenstunden = new NumberField("Wochenstunden");

    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button editStellenanzeige = new Button("Stellenanzeige bearbeiten");

    private Binder<StellenanzeigeDTOImpl> binder = new Binder(StellenanzeigeDTOImpl.class);

    public EnterStellenanzeigeView(JobApplicationControl jobApplicationControl) {
        addClassName("enter-stellenanzeige-view");

        add(createTitle());

        titel.setAutofocus(true);

//        gehalt.setValue(8.0);
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        gehalt.setSuffixComponent(euroSuffix);

        einstellungsdatum.setMin(now);
        einstellungsdatum.setMax(now.plusDays(180));

        add(createFormLayout());
        add(createButtonLayout());

        binder.forField(titel)
                .asRequired("Geben Sie bitte einen Titel ein.")
                .bind(StellenanzeigeDTOImpl::getTitel, StellenanzeigeDTOImpl::setTitel);

        binder.forField(bereich)
                .asRequired("Geben Sie bitte einen Bereich ein.")
                .bind(StellenanzeigeDTOImpl::getBereich, StellenanzeigeDTOImpl::setBereich);

        binder.forField(beschreibung)
                .asRequired("Geben Sie bitte eine Beschreibung ein.")
                .withValidator(
                        beschreibung -> beschreibung.length() >= 50,
                        "Beschreibung muss mindestens 50 Zeichen haben!"
                )
                .bind(StellenanzeigeDTOImpl::getBeschreibung, StellenanzeigeDTOImpl::setBeschreibung);

        binder.forField(gehalt)
                .asRequired("Geben Sie bitte ein Gehalt ein.")
                .withValidator(
                        gehalt -> 0 <= gehalt,
                        "Ein negatives Gehalt ist nicht zulässig!"
                )
                .withValidator(
                        gehalt -> 8.50 <= gehalt,
                        "Gehalt entspricht nicht dem Mindestlohn!"
                )
                .bind(StellenanzeigeDTOImpl::getGehalt, StellenanzeigeDTOImpl::setGehalt);

        binder.forField(wochenstunden)
                .asRequired("Geben Sie bitte eine Wochenstundenanzahl ein.")
                .withValidator(
                        wochenstunden -> 0 <= wochenstunden,
                        "Eine negative Wochenstundenanzahl ist nicht zulässig!"
                )
                .bind(StellenanzeigeDTOImpl::getWochenstunden, StellenanzeigeDTOImpl::setWochenstunden);

        // Binder
        binder.bindInstanceFields(this);
        binder.readBean(new StellenanzeigeDTOImpl());
        clearForm();

        cancel.addClickListener(event -> clearForm());

        save.addClickListener(e -> {
            UserDTO userDTO = (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
            try {
                System.out.println(binder.getBean().getTitel());
                jobApplicationControl.createStellenanzeige(binder.getBean(), userDTO);
            } catch (DatabaseLayerException ex) {
                throw new RuntimeException(ex);
            }
            clearForm();
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
}
