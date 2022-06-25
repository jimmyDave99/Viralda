package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.util.List;

/**
 * The LandingPageStudent is the home page for users with the role = "Student".
 */

@Route(value = Globals.Pages.SHOW_JOB_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Meine Stellenanzeigen")
@CssImport(value = "./styles/views/landingpage/landing-page.css", themeFor = "vaadin-grid")
public class showJobCompanyView extends Div {

    protected static volatile int jobId = 0;

    UserDTO currentUser = this.getCurrentUser();

    private List<StellenanzeigeDTO> jobList;

    public showJobCompanyView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {
        addClassName("show-job-company");

        jobList = jobApplicationControl.readCurrentCompanyJob(currentUser);

        add(createTitle());

        if(jobList.isEmpty()){
            add(NotJobFound());
        }else {
            add(createGridTable());
        }
    }

    private Component createGridTable(){

        Grid<StellenanzeigeDTO> grid = new Grid<>(StellenanzeigeDTO.class, false);
        Editor<StellenanzeigeDTO> editor=grid.getEditor();
        grid.setHeightByRows(true);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        ListDataProvider<StellenanzeigeDTO> dataProvider = new ListDataProvider<>(jobList);
        grid.setDataProvider(dataProvider);

        Grid.Column<StellenanzeigeDTO> titleColumn = grid
                .addColumn(StellenanzeigeDTO::getTitel)
                .setHeader("Titel");

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

        grid.addComponentColumn( job -> {
                    Button saveButton = new Button("Bearbeiten");
                    saveButton.addClickListener(e -> {
                        jobId = job.getStellenId();
                        navigateToJobApplicationView(jobId);
                    });
                    return saveButton;
                }).setWidth("150px").setFlexGrow(0);

        HeaderRow filterRow = grid.appendHeaderRow();

        // filter
        TextField titleField = new TextField();
        titleField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(job.getTitel(),
                        titleField.getValue())));

        titleField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(titleColumn).setComponent(titleField);
        titleField.setSizeFull();
        titleField.setPlaceholder("Filter");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

         return grid;
    }

    private Component createTitle() { return new H2("Meine Stellenanzeigen"); }

    private Component NotJobFound() { return new H4("   keine Stellenanzeigen gefunden"); }

    private void navigateToJobApplicationView(int jobId) {
        UI.getCurrent().navigate(Globals.Pages.JOB_COMPANY_VIEW + jobId);
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
