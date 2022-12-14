package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The LandingPageStudent is the home page for users with the role = "Student".
 */

@Route(value = Globals.Pages.LANDING_PAGE_STUDENT_VIEW, layout = AppView.class)
@PageTitle("Verfügbare Stellenazeigen")
@CssImport(value = "./styles/views/landingpage/landing-page.css", themeFor = "vaadin-grid")
public class LandingPageStudentView extends Div {

    protected static volatile int jobId = 0;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private List<StellenanzeigeDTO> jobList;


    public LandingPageStudentView(JobControl jobControl) throws DatabaseLayerException {
        addClassName("landing-page");

        jobList = jobControl.readAllJobApplications();

        add(createTitle());

        if(jobList.isEmpty()){
            add(NotJobFound());
        }else {
            add(createGridTable());
        }
    }

    private Component createTitle() {
        return new H2("Aktuell verfügbare Stellenanzeigen");
    }

    private Component createGridTable() {

        Grid<StellenanzeigeDTO> grid = new Grid<>(StellenanzeigeDTO.class, false);
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // Befüllen der Tabelle mit den zuvor ausgelesenen Stellen
        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(jobList);
        grid.setDataProvider(dataProvider);

        grid.addComponentColumn(person -> {return VaadinIcon.ANGLE_DOWN.create();}).setWidth("4em").setFlexGrow(0);

        Grid.Column<StellenanzeigeDTO> companyNameColumn = grid
                .addColumn(StellenanzeigeDTO::getUnternehmenId)
                .setHeader("Unternehmens-ID")
                .setWidth("12em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<StellenanzeigeDTO> titleColumn = grid
                .addColumn(StellenanzeigeDTO::getTitel)
                .setHeader("Titel")
                .setSortable(true);

        Grid.Column<StellenanzeigeDTO> dateOfDeploymentColumn = grid
                .addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstellungsdatum")
                .setWidth("12em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<StellenanzeigeDTO> salaryColumn = grid
                .addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt (€)")
                .setWidth("8em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<StellenanzeigeDTO> hoursPerWeekColumn = grid
                .addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden")
                .setWidth("10em").setFlexGrow(0)
                .setSortable(true);

        grid.addComponentColumn(job -> {
            Button applyButton = new Button("Bewerben");

            applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            applyButton.addClickListener(e -> {
                jobId = job.getStellenId();
                navigateToJobApplicationView(jobId);
            });

            return applyButton;
        }).setWidth("150px").setFlexGrow(0);

        HeaderRow filterRow = grid.appendHeaderRow();


        // companyNameColumn filter
        TextField companyNameField = new TextField();
        companyNameField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getUnternehmenId()),
                        companyNameField.getValue())));

        companyNameField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(companyNameColumn).setComponent(companyNameField);
        companyNameField.setSizeFull();
        companyNameField.setPlaceholder("Filter");

        // titleColumn filter
        TextField titleField = new TextField();
        titleField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(job.getTitel(),
                        titleField.getValue())));

        titleField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(titleColumn).setComponent(titleField);
        titleField.setSizeFull();
        titleField.setPlaceholder("Filter");

        // dateOfDeploymentColumn filter
        TextField dateOfDeploymentField = new TextField();
        dateOfDeploymentField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(job.getEinstellungsdatum().format(formatter),
                        dateOfDeploymentField.getValue())));

        dateOfDeploymentField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(dateOfDeploymentColumn).setComponent(dateOfDeploymentField);
        dateOfDeploymentField.setSizeFull();
        dateOfDeploymentField.setPlaceholder("Filter");

        // salary filter
        TextField salaryField = new TextField();
        salaryField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getGehalt()),
                        salaryField.getValue())));

        salaryField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(salaryColumn).setComponent(salaryField);
        salaryField.setSizeFull();
        salaryField.setPlaceholder("Filter");

        // hoursPerWeekColumn filter
        TextField hoursPerWeekField = new TextField();
        hoursPerWeekField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getWochenstunden()),
                        hoursPerWeekField.getValue())));

        hoursPerWeekField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(hoursPerWeekColumn).setComponent(hoursPerWeekField);
        hoursPerWeekField.setSizeFull();
        hoursPerWeekField.setPlaceholder("Filter");


        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        // Einstellungen zum Toggle
        //grid.setDetailsVisibleOnClick(false); // entfernt toggle
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(stellenanzeigeDTO -> {
                    VerticalLayout layout = new VerticalLayout();

                    TextArea details = new TextArea();
                    if ((stellenanzeigeDTO.getBeschreibung() != null) && (!stellenanzeigeDTO.getBeschreibung().equals(""))) details.setValue(stellenanzeigeDTO.getBeschreibung());
                    else details.setValue("Diese Stellenanzeige besitzt leider keine nähere Beschreibung.");
                    details.setWidthFull();
                    details.setReadOnly(true);

                    layout.add(new H5("Stellenbeschreibung:"));
                    layout.add(details);

                    return layout;
                })
        );

        return grid;
    }

    /*private static Renderer<StellenanzeigeDTO> createToggleDetailsRenderer(Grid<StellenanzeigeDTO> grid) {
        return LitRenderer.<StellenanzeigeDTO>of("<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Toggle details</vaadin-button>").with
    }*/

    private void navigateToJobApplicationView(int jobId) {
        UI.getCurrent().navigate(Globals.Pages.JOB_APPLICATION_VIEW + jobId);
    }

    private Component NotJobFound() { return new H4("Es wurden leider keine Stellenanzeigen gefunden."); }
}
