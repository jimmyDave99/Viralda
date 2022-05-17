package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
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

@Route(value = Globals.Pages.PROFIL_VIEW, layout = AppView.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/profile.css")
public class ProfilView extends Div {

    private final Tab profile;
    private final Tab settings;
    private final Tab notifications;

    private final VerticalLayout content;

    private Button save = new Button("Speichern");
    private Button edit = new Button("Bearbeiten");

    public ProfilView() {
        addClassName("profile");

        add(createTitle());

        // The different tabs
        profile = new Tab(
                VaadinIcon.USER.create(),
                new Span("Profil")
        );

        settings = new Tab(
                VaadinIcon.COG.create(),
                new Span("Einstellungen")
        );

        notifications = new Tab(
                VaadinIcon.BELL.create(),
                new Span("Benachrichtigungen")
        );

        // Set the icon on top
        for (Tab tab : new Tab[] { profile, settings, notifications }) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        Tabs tabs = new Tabs(profile, settings, notifications);

        tabs.addSelectedChangeListener( selectedChangeEvent ->
                setContent(selectedChangeEvent.getSelectedTab()));

        // Content for the tabs
        content = new VerticalLayout();
        content.setSpacing(false);
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }

    private Component createTitle() { return new H2("Profil von 'Name' 'Nachname' ");
    }

    private void setContent( Tab tab ) {
        content.removeAll();

        if ( tab.equals( profile ) ) {
            content.add(createButtonLayoutTabProfile());

        } else if ( tab.equals( settings ) ) {
            content.add(new Paragraph("Sicherheitseinstellungen"));
            content.add(new Paragraph("Privatssphäreeinstellungen"));
            content.add(new H3("Sonstiges"));

        } else if ( tab.equals( notifications ) ) {
            content.add(new Text("Beispieltext in vielleicht einer ganzen Zeile."));
        }
    }

    private Component createButtonLayoutTabProfile() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        //buttonLayout.addClassName("button-layout-tab-profile");
        buttonLayout.add(save);
        buttonLayout.add(edit);
        return buttonLayout;
    }
}
