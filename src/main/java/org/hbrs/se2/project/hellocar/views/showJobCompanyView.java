package org.hbrs.se2.project.hellocar.views;

import com.sun.xml.bind.v2.runtime.unmarshaller.TextLoader;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import javax.security.auth.callback.TextOutputCallback;
import java.awt.font.TextLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The LandingPageStudent is the home page for users with the role = "Student".
 */

@Route(value = Globals.Pages.SHOW_JOB_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeigen des eigenen Unternehmens")
@CssImport("./styles/views/profile/profile.css")//, themeFor = "vaadin-grid")
public class showJobCompanyView extends Div {

    protected static volatile int jobId = 0;

    UserDTO currentUser = this.getCurrentUser();

    private List<StellenanzeigeDTO> jobList;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public showJobCompanyView(JobControl jobControl) throws DatabaseLayerException {
        addClassName("profile");

        jobList = jobControl.readCurrentCompanyJob(currentUser);

        add(createTitle());

        if (jobList.isEmpty()) {
            add(NotJobFound());
        } else {
            add(createGridTable());
        }
    }

    private Component createGridTable() {

        Grid<StellenanzeigeDTO> grid = new Grid<>(StellenanzeigeDTO.class, false);
        Editor<StellenanzeigeDTO> editor = grid.getEditor();
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(jobList);
        grid.setDataProvider(dataProvider);

        Grid.Column<StellenanzeigeDTO> idColumn = grid
                .addColumn(StellenanzeigeDTO::getStellenId)
                .setHeader("Stellen ID");

        Grid.Column<StellenanzeigeDTO> titleColumn = grid
                .addColumn(StellenanzeigeDTO::getTitel)
                .setHeader("Titel");

        Grid.Column<StellenanzeigeDTO> dateOfDeploymentColumn = grid
                .addColumn(StellenanzeigeDTO::getEinstellungsdatum)
                .setHeader("Einstellungsdatum");

        Grid.Column<StellenanzeigeDTO> salaryColumn = grid
                .addColumn(StellenanzeigeDTO::getGehalt)
                .setHeader("Gehalt");

        Grid.Column<StellenanzeigeDTO> hoursPerWeekColumn = grid
                .addColumn(StellenanzeigeDTO::getWochenstunden)
                .setHeader("Wochenstunden");

        grid.addColumn(StellenanzeigeDTO::getStatus)
                .setHeader("Status");

        grid.addComponentColumn(job -> {
            Button editButton = new Button("Bearbeiten");
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> {
                jobId = job.getStellenId();
                navigateToJobApplicationView(jobId);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        HeaderRow filterRow = grid.appendHeaderRow();

        // idColumn filter
        TextField idColumnField = new TextField();
        idColumnField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getUnternehmenId()),
                        idColumnField.getValue())));

        filterRow.getCell(idColumn).setComponent(idColumnField);
        idColumnField.setSizeFull();
        idColumnField.setPlaceholder("Filter");

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

    private Component createTitle() {
        return new H2("Stellenanzeigen des eigenen Unternehmens");
    }

    private Component NotJobFound() {
        return new H4("Es wurden leider keine Stellenanzeigen des eigenen"
                + " Unternehmens gefunden. Vermutlich wurden noch keine Stellenanzeigen erstellt.");
    }

    private void navigateToJobApplicationView(int jobId) {
        UI.getCurrent().navigate(Globals.Pages.JOB_COMPANY_VIEW + jobId);
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
