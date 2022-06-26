package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.hellocar.control.JobControl;
import org.hbrs.se2.project.hellocar.dtos.StellenanzeigeDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.time.LocalDate;
import java.util.List;

import static org.hbrs.se2.project.hellocar.util.Globals.JobStatus.*;
import static org.hbrs.se2.project.hellocar.util.Globals.Pages.JOB_COMPANY_VIEW;

@Route()
@PageTitle("Stellenanzeige des eigenen Unternehmens bearbeiten")
@CssImport("./styles/views/showjobsfromcompany/show-jobs-from-company-view.css")
public class JobCompanyViewReworked extends VerticalLayout implements HasUrlParameter<String> {

    LocalDate now = LocalDate.now();

    int jobId = 0;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        Location location = event.getLocation();
        this.jobId = Integer.parseInt(location.getPath().replaceAll("[\\D]", ""));
    }





















    private Component createTitle(String title) {
        return new H2("Stelle " + title);
    }

    private void navigateToshowJobCompanyView() {
        UI.getCurrent().navigate(Globals.Pages.SHOW_JOB_COMPANY_VIEW);
        UI.getCurrent().getPage().reload();
    }

}
