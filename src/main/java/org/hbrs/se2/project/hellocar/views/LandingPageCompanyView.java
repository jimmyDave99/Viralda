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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.hbrs.se2.project.hellocar.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

/**
 * The LandingPageCompanyView is the home page for user with the role 'employer'.
 */
@Route(value = Globals.Pages.LANDING_PAGE_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Startseite")
@CssImport("./styles/views/landingpage/landing-page.css")
public class LandingPageCompanyView extends Div {

    //ToDO: muss nach Einbindung Datenbank noch angepasst werden
    //private List<> employmentAds;

    public LandingPageCompanyView() {
        addClassName("landing-page");

        add(createTitle());
        add(createFormLayout());

        //ToDO: generische Typ ersetzen
        Grid<Integer> grid = new Grid<>(Integer.class, false);
        grid.addColumn(Integer::intValue).setHeader("Stellenangebot");
        grid.addColumn(Integer::intValue).setHeader("Unternehmen");
        grid.addColumn(Integer::intValue).setHeader("Ort");
        grid.addColumn(Integer::intValue).setHeader("Anstellungsart");
        grid.addColumn(Integer::intValue).setHeader("Einstellungsdatum");
        grid.addColumn(Integer::intValue).setHeader("Status");

        grid.setDetailsVisibleOnClick(false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        add(grid);

    }

    private Component createTitle() { return new H3("Stellenazeigen von 'Platzhalter unternehmen'"); }
    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        return formLayout;
    }
}