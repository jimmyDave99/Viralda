package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.hellocar.control.JobApplicationControl;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.hbrs.se2.project.hellocar.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The LandingPageCompanyView is the home page for user with the role 'employer'.
 */
@Route(value = Globals.Pages.LANDING_PAGE_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Startseite")
@CssImport("./styles/views/landingpage/landing-page.css")
public class LandingPageCompanyView extends Div {

    //ToDO: muss nach Einbindung Datenbank noch angepasst werden
    private List<BewerbungDTO> bewerbungslist;

    public LandingPageCompanyView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {
        addClassName("landing-page");

        bewerbungslist = jobApplicationControl.readAllJobApplications2();

        add(createTitle());
        //add(createFormLayout());
        add(createGridTable());

        //ToDO: generische Typ ersetzen
//        Grid<Integer> grid = new Grid<>(Integer.class, false);
//        grid.addColumn(Integer::intValue).setHeader("Stellenangebot");
//        grid.addColumn(Integer::intValue).setHeader("Unternehmen");
//        grid.addColumn(Integer::intValue).setHeader("Ort");
//        grid.addColumn(Integer::intValue).setHeader("Anstellungsart");
//        grid.addColumn(Integer::intValue).setHeader("Einstellungsdatum");
//        grid.addColumn(Integer::intValue).setHeader("Status");
//
//        grid.setDetailsVisibleOnClick(false);
//        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
//        add(grid);

    }
    private Component createGridTable(){

        Grid<BewerbungDTO> grid = new Grid<>();
        grid.setHeight("800px");

        // Befüllen der Tabelle mit den zuvor ausgelesenen Stellen
        ListDataProvider<BewerbungDTO> dataProvider = new ListDataProvider<>(bewerbungslist);
        grid.setDataProvider(dataProvider);

        Grid.Column<BewerbungDTO> stellenIdColumn = grid
                .addColumn(BewerbungDTO::getStellenId)
                .setHeader("StelleID");

        Grid.Column<BewerbungDTO> bewerberIdColumn = grid
                .addColumn(BewerbungDTO::getBewerberId)
                .setHeader("BewerberId");

        grid.addColumn(BewerbungDTO::getAnschreiben)
                .setHeader("Anschreiben").setWidth("250px").setFlexGrow(0);

        grid.addColumn(BewerbungDTO::getLebenslauf)
                .setHeader("Lebenslauf").setWidth("250px").setFlexGrow(0);

        grid.addColumn(BewerbungDTO::getWeitereUnterlagen)
                .setHeader("WeitererUnterlagen").setWidth("250px").setFlexGrow(0);

        grid.addColumn(BewerbungDTO::getBewerbungsdatum)
                .setHeader("Datum");

        Grid.Column<BewerbungDTO> submitColumn = grid.addComponentColumn( job -> {
            Button saveButton = new Button("Bearbeiten");
            saveButton.addClickListener(e -> {
                Notification.show("Sollte bearbeiten werden");
            });
            return saveButton;
        }).setWidth("150px").setFlexGrow(0);


        HeaderRow filterRow = grid.appendHeaderRow();

        // First filter
        TextField stellenIdField = new TextField();
        stellenIdField.addValueChangeListener(event -> dataProvider.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getStellenId()),
                        stellenIdField.getValue())));

        stellenIdField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(stellenIdColumn).setComponent(stellenIdField);
        stellenIdField.setSizeFull();
        stellenIdField.setPlaceholder("Filter");

        // Second filter
        TextField bewerberIdField = new TextField();
        bewerberIdField.addValueChangeListener(event -> dataProvider
                .addFilter(job -> StringUtils.containsIgnoreCase(
                        String.valueOf(job.getBewerberId()), bewerberIdField.getValue())));

        bewerberIdField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(bewerberIdColumn).setComponent(bewerberIdField);
        bewerberIdField.setSizeFull();
        bewerberIdField.setPlaceholder("Filter");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        return grid;

    }

    private Component createTitle() { return new H2("Bewerbungen Von Studenten"); }
//    private Component createFormLayout() {
//        FormLayout formLayout = new FormLayout();
//        return formLayout;
//    }
}