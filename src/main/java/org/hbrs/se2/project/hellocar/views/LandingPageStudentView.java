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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.treegrid.TreeGridArrayUpdater;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.hbrs.se2.project.hellocar.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The LandingPageStundent is the home page for users with the role = "stundent".
 */

@Route(value = Globals.Pages.LANDING_PAGE_STUDENT_VIEW, layout = AppView.class)
@PageTitle("Startseite")
@CssImport("./styles/views/landingpage/landing-page.css")
public class LandingPageStudentView extends Div {

    //ToDO: muss nach Einbindung Datenbank noch angepasst werden
    //private List<> employmentAds;

    public LandingPageStudentView() {
        addClassName("landing-page");

        add(createTitle());

        //ToDO: generische Typ ersetzen
        Grid<Integer> grid = new Grid<>(Integer.class, false);
        grid.addColumn(Integer::intValue).setHeader("Stellenangebot");
        grid.addColumn(Integer::intValue).setHeader("Unternehmen");
        grid.addColumn(Integer::intValue).setHeader("Ort");
        grid.addColumn(Integer::intValue).setHeader("Anstellungsart");
        grid.addColumn(Integer::intValue).setHeader("Einstellungsdatum");

        grid.setDetailsVisibleOnClick(false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        add(grid);

    }

    private Component createTitle() { return new H2("Stellenanzeigen"); }
}
