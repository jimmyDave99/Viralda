package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.hbrs.se2.project.hellocar.control.JobControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.StellenanzeigeDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import com.vaadin.flow.component.dependency.CssImport;

import java.time.LocalDate;
import java.time.ZoneId;


@Route(value = Globals.Pages.ENTER_STELLENANZEIGE_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeige erstellen")
@CssImport("./styles/views/profile/profile.css")
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

    private Binder<StellenanzeigeDTOImpl> binder = new Binder(StellenanzeigeDTOImpl.class);

    public EnterStellenanzeigeView(JobControl jobControl) {
        addClassName("profile");

        add(createTitle());

        add(new H4());

        titel.setAutofocus(true);

        Div euroSuffix = new Div();
        euroSuffix.setText("€");

        gehalt.setSuffixComponent(euroSuffix);
        gehalt.setPlaceholder("Der Mindestlohn in Deutschland beträgt aktuell 9,60 pro Stunde.");

        einstellungsdatum.setMin(now);
        einstellungsdatum.setMax(now.plusDays(180));

        beschreibung.setWidthFull();
        beschreibung.setValueChangeMode(ValueChangeMode.EAGER);
        beschreibung.addValueChangeListener(event -> {
            event.getSource().setHelperText("Zeichen: " + event.getValue().length());
        });

        add(createButtonLayout());
        add(createFormLayoutWithoutBeschreibung());
        add(createHorizontalLayoutWithBeschreibung());

        binder.forField(titel)
                .asRequired("Geben Sie bitte einen Titel ein.")
                .withValidator(
                        titel -> Character.isUpperCase(titel.charAt(0)),
                        "Der Titel muss mit einem Großbuchstaben anfangen."
                )
                .bind(StellenanzeigeDTOImpl::getTitel, StellenanzeigeDTOImpl::setTitel);

        binder.forField(bereich)
                .asRequired("Geben Sie bitte einen Bereich ein.")
                .withValidator(
                        bereich -> Character.isUpperCase(bereich.charAt(0)),
                        "Der Bereich muss mit einem Großbuchstaben anfangen."
                )
                .bind(StellenanzeigeDTOImpl::getBereich, StellenanzeigeDTOImpl::setBereich);

        binder.forField(beschreibung)
                .asRequired("Geben Sie bitte eine Beschreibung ein.")
                .withValidator(
                        beschreibung -> beschreibung.length() >= 25,
                        "Beschreibung muss mindestens 25 Zeichen haben!"
                )
                .bind(StellenanzeigeDTOImpl::getBeschreibung, StellenanzeigeDTOImpl::setBeschreibung);

        binder.forField(gehalt)
                .asRequired("Geben Sie bitte ein Gehalt ein.")
                .withValidator(
                        gehalt -> 0 <= gehalt,
                        "Ein negatives Gehalt ist nicht zulässig!"
                )
                .withValidator(
                        gehalt -> 9.60 <= gehalt,
                        "Gehalt entspricht nicht dem Mindestlohn!"
                )
                .bind(StellenanzeigeDTOImpl::getGehalt, StellenanzeigeDTOImpl::setGehalt);

        binder.forField(wochenstunden)
                .asRequired("Geben Sie bitte eine Wochenstundenanzahl ein.")
                .withValidator(
                        wochenstunden -> 0 <= wochenstunden,
                        "Eine negative Wochenstundenanzahl ist nicht zulässig!"
                )
                .withValidator(
                        wochenstunden -> 168 >= wochenstunden,
                        "Eine Woche hat maximal 168 Stunden!"
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
                jobControl.createStellenanzeige(binder.getBean(), userDTO);
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

    private Component createFormLayoutWithoutBeschreibung() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(titel, bereich, gehalt, wochenstunden, einstellungsdatum);

        gehalt.setValue(null);
        wochenstunden.setValue(2.0);

        return formLayout;
    }

    private Component createHorizontalLayoutWithBeschreibung() {
        HorizontalLayout details = new HorizontalLayout();

        details.add(beschreibung);

        return details;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.add(save);
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(cancel);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        return buttonLayout;
    }
}
