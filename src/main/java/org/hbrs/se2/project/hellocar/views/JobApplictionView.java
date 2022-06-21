package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.hellocar.dao.StellenAnzeigeDAO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;

import java.io.InputStream;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.Pages.JOB_APPLICATION_VIEW;
import static org.hbrs.se2.project.hellocar.util.Globals.Pages.LANDING_PAGE_STUDENT_VIEW;

@Route(value = JOB_APPLICATION_VIEW, layout = AppView.class)
@PageTitle("Bewerben")
@CssImport("./styles/views/landingpage/landing-page.css")
public class JobApplictionView extends VerticalLayout implements HasUrlParameter<String>{

    int jobId = 0;
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        this.jobId = Integer.parseInt(location.getPath().replaceAll("[\\D]", ""));
    }

    private StellenAnzeigeDAO stellenAnzeigeDAO = new StellenAnzeigeDAO();
    private List<StellenanzeigeDTO> currentJob;

    public  JobApplictionView() throws DatabaseLayerException {

        addClassName("job-application");

        currentJob = stellenAnzeigeDAO.findJobWithId(LandingPageStudentView.jobId);

        add(createTitle(currentJob.get(0).getTitel()));

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

        Button saveButton = new Button("Bewerben");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> {
            Notification.show("Sie haben sich erfolgreich beworben.");
            UI.getCurrent().getPage().reload();
        });
        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener(e -> {
            navigateToLandingPageStudentView();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setPadding(true);
        buttonLayout.addClassName("button-layout");
        add(buttonLayout);

    }

    private Component createGridTable(){
        Grid<StellenanzeigeDTO> grid = new Grid<>();
        grid.setHeightByRows(true);

        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(currentJob);
        grid.setDataProvider(dataProvider);

        grid.addColumn(StellenanzeigeDTO::getBeschreibung)
                .setHeader("Beschreibung der Stelle").setWidth("450px").setFlexGrow(0);

        grid.addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstieg");

         grid.addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt");

        grid.addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden");

        grid.addColumn(StellenanzeigeDTO::getStatus)
                .setHeader("Status");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        return grid;
    }

    private void navigateToLandingPageStudentView() {
        UI.getCurrent().navigate(LANDING_PAGE_STUDENT_VIEW);
    }

    private Component createTitle(String title) { return new H2("Bewerbung für die Stelle " + title); }

    private Component createsubTitle() { return new H3("Bewerbung Unterlagen"); }
}
