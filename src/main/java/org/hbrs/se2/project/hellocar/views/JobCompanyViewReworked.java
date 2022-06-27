package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
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

//@Route(value = JOB_COMPANY_VIEW, layout = AppView.class)
@PageTitle("Stellenanzeige des eigenen Unternehmens bearbeiten")
@CssImport("./styles/views/profile/profile.css")
public class JobCompanyViewReworked extends Div implements HasUrlParameter<String> {

    LocalDate now = LocalDate.now();

    int jobId = 0;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        //Location location = event.getLocation();
        //this.jobId = Integer.parseInt(location.getPath().replaceAll("[\\D]", ""));
    }

    private List<StellenanzeigeDTO> currentJob;

    public JobCompanyViewReworked(JobControl jobControl) throws DatabaseLayerException {
        addClassName("profile");

        currentJob = jobControl.findJob(showJobCompanyView.jobId);

        add(createTitle(currentJob.get(0).getTitel()));

        add(new H3("Stellen-ID: " + (currentJob.get(0).getStellenId())));

        add(createButtonLayout(jobControl));

        add(createFormLayout());
        add(new Text(currentJob.get(0).getBeschreibung()));
    }




    private Component createButtonLayout(JobControl jobControl) {
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Button publishButton = new Button("Freigeben");
        publishButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        publishButton.addClickListener(event -> {
            try {
                jobControl.updateJobStatus(currentJob.get(0), AKTIV);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }
            Notification notification = Notification.show("Stellenanzeige freigegeben!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        });

        Button retrieveButton = new Button("Zurückziehen");
        retrieveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        retrieveButton.addClickListener(event -> {
            try {
                jobControl.updateJobStatus(currentJob.get(0), INAKTIV);
            } catch (DatabaseLayerException e) {
                e.printStackTrace();
            }
            Notification notification = Notification.show("Stellenanzeige zurückgezogen!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        });

        Button deleteButton = new Button("Löschen");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.addClickListener(e -> {
            navigateToshowJobCompanyView();
            try {
                jobControl.deleteAnnouncement(currentJob.get(0));
            } catch (DatabaseLayerException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener(event -> {
            UI.getCurrent().navigate(Globals.Pages.SHOW_JOB_COMPANY_VIEW);
        });

        buttonLayout.add(publishButton, retrieveButton, deleteButton, cancelButton);

        return buttonLayout;
    }

    private Component createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();



        return formLayout;
    }

    private Component createTitle(String title) {
        return new H2("Stelle " + title);
    }

    private void navigateToshowJobCompanyView() {
        UI.getCurrent().navigate(Globals.Pages.SHOW_JOB_COMPANY_VIEW);
        UI.getCurrent().getPage().reload();
    }

}
