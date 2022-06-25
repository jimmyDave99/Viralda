package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.time.LocalDate;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.*;
import static org.hbrs.se2.project.hellocar.util.Globals.Pages.JOB_COMPANY_VIEW;

@Route(value = JOB_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeige")
@CssImport("./styles/views/landingpage/landing-page.css")
public class JobCompanyView extends VerticalLayout implements HasUrlParameter<String>{

    LocalDate now = LocalDate.now();

    int jobId = 0;
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        this.jobId = Integer.parseInt(location.getPath().replaceAll("[\\D]", ""));
    }

    private List<StellenanzeigeDTO> currentJob;

    public JobCompanyView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {

        addClassName("job-company");

        currentJob = jobApplicationControl.findJob(showJobCompanyView.jobId);

        add(createTitle(currentJob.get(0).getTitel()));

        Button publishButton = new Button("Freigeben");
        publishButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        publishButton.addClickListener(event -> {
            try {
                jobApplicationControl.updateJobStatus(currentJob.get(0), AKTIV);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }
            Notification notification = Notification.show("Stellenanzeige freigegeben!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        });

        Button retrieveButton = new Button("Zurückziehen");
        retrieveButton.addClickListener(event -> {
            try {
                jobApplicationControl.updateJobStatus(currentJob.get(0), INAKTIV);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }
            Notification notification = Notification.show("Stellenanzeige zurückgezogen!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        });

        Button deleteButton = new Button("Löschen");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            navigateToshowJobCompanyView();
            try {
                jobApplicationControl.deleteAnnouncement(currentJob.get(0));
            } catch (DatabaseLayerException ex) {
                ex.printStackTrace();
            }
        });

        Grid<StellenanzeigeDTO> grid = new Grid<>();
        Editor<StellenanzeigeDTO> editor = grid.getEditor();

        grid.setHeightByRows(true);

        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(currentJob);
        grid.setDataProvider(dataProvider);

        grid.addColumn(StellenanzeigeDTO::getStellenId)
                .setHeader("Stellen ID");

        Grid.Column<StellenanzeigeDTO> titleColumn = grid.addColumn(StellenanzeigeDTO::getTitel)
                .setHeader("Titel");

        Grid.Column<StellenanzeigeDTO> descriptionColumn = grid.addColumn(StellenanzeigeDTO::getBeschreibung)
                .setHeader("Beschreibung der Stelle").setWidth("450px").setFlexGrow(0);

        Grid.Column<StellenanzeigeDTO> brancheColumn = grid.addColumn(StellenanzeigeDTO::getBereich)
                .setHeader("Bereich");

        Grid.Column<StellenanzeigeDTO> dateColumn = grid.addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstieg");

        Grid.Column<StellenanzeigeDTO> priceColumn = grid.addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt");

        Grid.Column<StellenanzeigeDTO> weeklyHoursColumn = grid.addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden");

        grid.addColumn(StellenanzeigeDTO::getStatus)
                .setHeader("Status");

        Grid.Column<StellenanzeigeDTO> editColumn = grid.addComponentColumn(job -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(job);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        Binder<StellenanzeigeDTO> binder = new Binder<>(StellenanzeigeDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField titleField = new TextField();
        titleField.setWidthFull();
        binder.forField(titleField)
                .asRequired("Titel eingeben")
                .bind(StellenanzeigeDTO::getTitel, StellenanzeigeDTO::setTitel);
        titleColumn.setEditorComponent(titleField);

        TextArea descriptionField = new TextArea();
        descriptionField.setWidthFull();
        binder.forField(descriptionField).asRequired("Beschreibung eingeben")
                .withValidator(
                        description -> description.length() >= 50,
                        "Beschreibung muss mindestens 50 Zeichen haben!"
                )
                .bind(StellenanzeigeDTO::getBeschreibung, StellenanzeigeDTO::setBeschreibung);
        descriptionColumn.setEditorComponent(descriptionField);

        TextField brancheField = new TextField();
        brancheField.setWidthFull();
        binder.forField(brancheField).asRequired("Bereich eingeben")
                .bind(StellenanzeigeDTO::getBereich, StellenanzeigeDTO::setBereich);
        brancheColumn.setEditorComponent(brancheField);

        DatePicker dateField = new DatePicker();
        dateField.setWidthFull();
        dateField.setMin(now);
        binder.forField(dateField).asRequired()
                .bind(StellenanzeigeDTO::getEinstellungsdatum, StellenanzeigeDTO::setEinstellungsdatum);
        dateColumn.setEditorComponent(dateField);

        NumberField priceField = new NumberField();
        priceField.setMin(8.5);
        priceField.setStep(0.5);
        binder.forField(priceField).asRequired()
                .bind(StellenanzeigeDTO::getGehalt, StellenanzeigeDTO::setGehalt);
        priceColumn.setEditorComponent(priceField);

        NumberField weeklyHoursField = new NumberField();
        weeklyHoursField.setMin(0);
        weeklyHoursField.setStep(0.5);
        binder.forField(weeklyHoursField).asRequired()
                .bind(StellenanzeigeDTO::getWochenstunden, StellenanzeigeDTO::setWochenstunden);
        weeklyHoursColumn.setEditorComponent(weeklyHoursField);

        Button saveButton = new Button("Save", e -> {
            editor.save();
            try {
                jobApplicationControl.updateAnnouncement(currentJob.get(0));
            } catch (DatabaseLayerException ex) {
                ex.printStackTrace();
            }
            UI.getCurrent().getPage().reload();
        });

        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);


        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        add(grid);

        HorizontalLayout buttonLayout = new HorizontalLayout(publishButton, retrieveButton, deleteButton);
        buttonLayout.setPadding(true);
        buttonLayout.addClassName("button-layout");
        add(buttonLayout);
    }

    private Component createTitle(String title) { return new H2("Stelle " + title); }

    private void navigateToshowJobCompanyView() {
        UI.getCurrent().navigate(Globals.Pages.SHOW_JOB_COMPANY_VIEW);
        UI.getCurrent().getPage().reload();
    }

}
