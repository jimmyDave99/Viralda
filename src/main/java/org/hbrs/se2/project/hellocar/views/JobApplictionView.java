package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.control.JobControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.io.InputStream;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.BEWORBEN;
import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.ZURUCKGEZOGEN;
import static org.hbrs.se2.project.hellocar.util.Globals.Pages.JOB_APPLICATION_VIEW;

@Route(value = JOB_APPLICATION_VIEW, layout = AppView.class)
@PageTitle("Bewerben")
@CssImport("./styles/views/landingpage/landing-page.css")
public class JobApplictionView extends VerticalLayout implements HasUrlParameter<String> {

    UserDTO currentUser = this.getCurrentUser();

    int jobId = 0;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        this.jobId = Integer.parseInt(location.getPath().replaceAll("[\\D]", ""));
    }

    private List<StellenanzeigeDTO> currentJob;

    public JobApplictionView(JobApplicationControl jobApplicationControl, JobControl jobControl) throws DatabaseLayerException {

        addClassName("job-application");

        currentJob = jobControl.findJob(LandingPageStudentView.jobId);

        add(createTitle(currentJob.get(0).getTitel()));

        add(createJobStatus(jobApplicationControl.getJobStatus(currentJob.get(0), currentUser)));

        add(createButtonLayout(jobApplicationControl));

        add(createGridTable());

        add(createsubTitle());

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf", ".pdf");
        Button uploadButton = new Button("Upload PDF...");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);

        int maxFileSizeInBytes = 10 * 1024 * 1024; // 10MB
        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.setDropLabel(new Label("Drop PDF file here (max 10MB)"));

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(
                    errorMessage,
                    5000,
                    Notification.Position.MIDDLE
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            // Do something with the file data
            // processFile(inputStream, fileName);
        });

        add(upload);
    }

    private Component createGridTable() {
        Grid<StellenanzeigeDTO> grid = new Grid<>();
        grid.setHeightByRows(true);

        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(currentJob);
        grid.setDataProvider(dataProvider);

        grid.addColumn(StellenanzeigeDTO::getUnternehmenId)
                        .setHeader("Unternehmen");

        grid.addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstellungsdatum");

        grid.addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt");

        grid.addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden");


        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        // Einstellungen zum Toggle
        //grid.setDetailsVisibleOnClick(false); // entfernt toggle
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(stellenanzeigeDTO -> {
                    VerticalLayout layout = new VerticalLayout();

                    TextArea details = new TextArea();

                    details.setValue(stellenanzeigeDTO.getBeschreibung());
                    details.setWidthFull();
                    details.setEnabled(false);

                    layout.add(new H5("Stellenbeschreibung:"));
                    layout.add(details);

                    return layout;
                })
        );

        return grid;
    }

    private Component createTitle(String title) {
        return new H2("Bewerbung für die Stelle: " + title);
    }

    private Component createJobStatus(String status) {
        return new H4("Status der Bewerbung: " + status);
    }

    private Component createButtonLayout(JobApplicationControl jobApplicationControl) {

        Button saveButton = new Button("Bewerben");
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {

            try {
                jobApplicationControl.createJobApplication(currentJob.get(0), currentUser, BEWORBEN);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }

            Notification.show("Sie haben sich erfolgreich beworben.");
            UI.getCurrent().getPage().reload();
        });

        Button withdrawButton = new Button("Zurückziehen");
        withdrawButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        withdrawButton.addClickListener(e -> {
            try {
                jobApplicationControl.updateJobApplicationStatus(currentJob.get(0).getStellenId(), currentUser.getStudentId(), ZURUCKGEZOGEN);
            } catch (DatabaseLayerException ex) {
                ex.printStackTrace();
            }
            Notification.show("Bewerbung zurückgezogen");
            UI.getCurrent().getPage().reload();
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(event -> {
            UI.getCurrent().navigate(Globals.Pages.LANDING_PAGE_STUDENT_VIEW);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, withdrawButton, cancelButton);
        buttonLayout.setPadding(true);
        buttonLayout.addClassName("button-layout");

        return buttonLayout;
    }

    private Component createsubTitle() {
        return new H3("Bewerbung Unterlagen");
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
