package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.util.List;

/**
 * The LandingPageStudent is the home page for users with the role = "Student".
 */

@Route(value = Globals.Pages.LANDING_PAGE_STUDENT_VIEW, layout = AppView.class)
@PageTitle("Startseite")
@CssImport(value = "./styles/views/landingpage/landing-page.css", themeFor = "vaadin-grid")
public class LandingPageStudentView extends Div {

    protected static volatile int jobId = 0;

    private List<StellenanzeigeDTO> jobList;

    public LandingPageStudentView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {
        addClassName("landing-page");

        jobList = jobApplicationControl.readAllJobApplications();

        add(createTitle());

        add(createGridTable());
    }

    private Component createGridTable(){

        Grid<StellenanzeigeDTO> grid = new Grid<>(StellenanzeigeDTO.class, false);
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // Befüllen der Tabelle mit den zuvor ausgelesenen Stellen
        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(jobList);
        grid.setDataProvider(dataProvider);

        Grid.Column<StellenanzeigeDTO> titleColumn = grid
                .addColumn(StellenanzeigeDTO::getTitel)
                .setHeader("Titel");

         grid.addColumn(StellenanzeigeDTO::getBeschreibung)
                 .setHeader("Beschreibung der Stelle").setWidth("450px").setFlexGrow(0);

         grid.addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstieg");

        Grid.Column<StellenanzeigeDTO> salaryColumn = grid
                .addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt");

        grid.addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden");

        grid.addComponentColumn( job -> {
                    Button saveButton = new Button("Bewerben");
                    saveButton.addClickListener(e -> {
                        jobId = job.getJobId();
                        navigateToJobApplicationView(jobId);
                    });
                    return saveButton;
                }).setWidth("150px").setFlexGrow(0);

        HeaderRow filterRow = grid.appendHeaderRow();

        // First filter
        TextField titleField = new TextField();
        titleField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(job.getTitel(),
                        titleField.getValue())));

        titleField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(titleColumn).setComponent(titleField);
        titleField.setSizeFull();
        titleField.setPlaceholder("Filter");

        // Second filter
        TextField salaryField = new TextField();
        salaryField.addValueChangeListener(event -> dataProvider
                .addFilter(job -> StringUtils.containsIgnoreCase(
                        String.valueOf(job.getGehalt()), salaryField.getValue())));

        salaryField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(salaryColumn).setComponent(salaryField);
        salaryField.setSizeFull();
        salaryField.setPlaceholder("Filter");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

         return grid;
    }

    private Component createTitle() { return new H2("Stellenanzeigen"); }

    private void navigateToJobApplicationView(int jobId) {
        UI.getCurrent().navigate(Globals.Pages.JOB_APPLICATION_VIEW + jobId);
    }
}
