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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.*;

/**
 * The LandingPageCompanyView is the home page for user with the role 'employer'.
 */
@Route(value = Globals.Pages.LANDING_PAGE_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Startseite")
@CssImport("./styles/views/landingpage/landing-page.css")
public class LandingPageCompanyView extends Div {

    UserDTO currentUser = this.getCurrentUser();

    private List<UserDTO> bewerbungslist;

    public LandingPageCompanyView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {
        addClassName("landing-page");

        bewerbungslist = jobApplicationControl.readAllApplicant(currentUser);

        add(createTitle());

        Grid<UserDTO> grid = new Grid<>();
        grid.setHeight("800px");

        // Befüllen der Tabelle mit den zuvor ausgelesenen Stellen
        ListDataProvider<UserDTO> dataProvider = new ListDataProvider<>(bewerbungslist);
        grid.setDataProvider(dataProvider);

        Grid.Column<UserDTO> stellenIdColumn = grid
                .addColumn(UserDTO::getStelleId)
                .setHeader("StelleID");

        grid.addColumn(UserDTO::getFirstName)
                .setHeader("Vorname");

        grid.addColumn(UserDTO::getLastName)
                .setHeader("Nachname");

        grid.addColumn(UserDTO::getEmail)
                .setHeader("Email").setWidth("250px").setFlexGrow(0);

        grid.addColumn(UserDTO::getbewerbungsDatum)
                .setHeader("BewerbungsDatum");

        grid.addColumn(UserDTO::getStatus)
                .setHeader("Status");

        grid.addComponentColumn( job -> {
            Button saveButton = new Button("Annehmen");
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            saveButton.addClickListener(e -> {
                try {
                    jobApplicationControl.updateJobApplicationStatus(job.getStelleId(), job.getStudentId(), ANGENOMMEN);
                } catch (DatabaseLayerException ex) {
                    ex.printStackTrace();
                }
                Notification.show("Bewerbung angenommen");
                UI.getCurrent().getPage().reload();
            });
            return saveButton;
        }).setWidth("150px").setFlexGrow(0);

        grid.addComponentColumn( job -> {
            Button saveButton = new Button("Ablehnen");
            saveButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            saveButton.addClickListener(e -> {
                try {
                    jobApplicationControl.updateJobApplicationStatus(job.getStelleId(), job.getStudentId(), ABGELEHNT);
                } catch (DatabaseLayerException ex) {
                    ex.printStackTrace();
                }
                Notification.show("Bewerbung Abgelehnt");
                UI.getCurrent().getPage().reload();
            });
            return saveButton;
        }).setWidth("150px").setFlexGrow(0);


        HeaderRow filterRow = grid.appendHeaderRow();

        // filter
        TextField stellenIdField = new TextField();
        stellenIdField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getStelleId()),
                        stellenIdField.getValue())));

        stellenIdField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(stellenIdColumn).setComponent(stellenIdField);
        stellenIdField.setSizeFull();
        stellenIdField.setPlaceholder("Filter");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        if(bewerbungslist.isEmpty()){
            add(NotJobFound());
        }else {
            add(grid);
        }
    }

    private Component createTitle() { return new H2("Bewerbungen Von Studenten"); }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private Component NotJobFound() { return new H4("   keine Bewerbungen eingegangen"); }
}