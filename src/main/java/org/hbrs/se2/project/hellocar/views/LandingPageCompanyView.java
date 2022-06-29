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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
@PageTitle("Bewerbungen einsehen")
@CssImport("./styles/views/landingpage/landing-page.css")
public class LandingPageCompanyView extends Div {

    UserDTO currentUser = this.getCurrentUser();

    private Button rejectButton;
    private Button applyButton;

    private List<UserDTO> bewerbungslist;

    public LandingPageCompanyView(JobApplicationControl jobApplicationControl) throws DatabaseLayerException {
        addClassName("landing-page");

        bewerbungslist = jobApplicationControl.readAllApplicant(currentUser);

        add(createTitle());

        Grid<UserDTO> grid = new Grid<>(UserDTO.class, false);
        grid.setHeightByRows(true);

        // Befüllen der Tabelle mit den zuvor ausgelesenen Stellen
        ListDataProvider<UserDTO> dataProviderApplication = new ListDataProvider<>(bewerbungslist);
        grid.setDataProvider(dataProviderApplication);

        grid.addComponentColumn(person -> {return VaadinIcon.ANGLE_DOWN.create();}).setWidth("4em").setFlexGrow(0);

        Grid.Column<UserDTO> stellenIdColumn = grid
                .addColumn(UserDTO::getStelleId)
                .setHeader("Stellen-ID")
                .setWidth("7em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<UserDTO> vornameColumn = grid
                .addColumn(UserDTO::getFirstName)
                .setHeader("Vorname")
                .setWidth("12em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<UserDTO> nachnameColumn = grid
                .addColumn(UserDTO::getLastName)
                .setHeader("Nachname")
                .setWidth("20em").setFlexGrow(0)
                .setSortable(true);

        Grid.Column<UserDTO> eMailAdresseColumn = grid
                .addColumn(UserDTO::getEmail)
                .setHeader("E-Mail-Adresse")
                .setSortable(true);

        Grid.Column<UserDTO> bewerbungsdatumColumn = grid
                .addColumn(UserDTO::getbewerbungsDatum)
                .setHeader("Bewerbungsdatum")
                .setWidth("12em").setFlexGrow(0)
                .setSortable(true);

        grid.addColumn(UserDTO::getStatus)
                .setHeader("Status")
                .setWidth("12em").setFlexGrow(0)
                .setSortable(true);

        grid.addComponentColumn( job -> {
            applyButton = new Button("Annehmen");
            applyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
            applyButton.addClickListener(e -> {
                try {
                    jobApplicationControl.updateJobApplicationStatus(job.getStelleId(), job.getStudentId(), ANGENOMMEN);
                } catch (DatabaseLayerException ex) {
                    ex.printStackTrace();
                }
                Notification.show("Bewerbung angenommen");
                UI.getCurrent().getPage().reload();
            });
            return applyButton;
        }).setWidth("150px").setFlexGrow(0);

        grid.addComponentColumn( job -> {
            rejectButton = new Button("Ablehnen");
            rejectButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
            rejectButton.addClickListener(e -> {
                try {
                    jobApplicationControl.updateJobApplicationStatus(job.getStelleId(), job.getStudentId(), ABGELEHNT);
                } catch (DatabaseLayerException ex) {
                    ex.printStackTrace();
                }
                Notification.show("Bewerbung Abgelehnt");
                UI.getCurrent().getPage().reload();
            });
            return rejectButton;
        }).setWidth("150px").setFlexGrow(0);


        HeaderRow filterRow = grid.appendHeaderRow();

        // stellenId filter
        TextField stellenIdField = new TextField();
        stellenIdField.addValueChangeListener(event -> dataProviderApplication.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getStelleId()),
                        stellenIdField.getValue())));

        stellenIdField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(stellenIdColumn).setComponent(stellenIdField);
        stellenIdField.setSizeFull();
        stellenIdField.setPlaceholder("Filter");

        // vorname filter
        TextField vornameField = new TextField();
        vornameField.addValueChangeListener(event -> dataProviderApplication.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getFirstName()),
                        vornameField.getValue())));

        vornameField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(vornameColumn).setComponent(vornameField);
        vornameField.setSizeFull();
        vornameField.setPlaceholder("Filter");

        // nachname filter
        TextField nachnameField = new TextField();
        nachnameField.addValueChangeListener(event -> dataProviderApplication.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getLastName()),
                        nachnameField.getValue())));

        nachnameField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(nachnameColumn).setComponent(nachnameField);
        nachnameField.setSizeFull();
        nachnameField.setPlaceholder("Filter");

        // emailAdresss filter
        TextField emailAdresseField = new TextField();
        emailAdresseField.addValueChangeListener(event -> dataProviderApplication.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getEmail()),
                        emailAdresseField.getValue())));

        emailAdresseField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(eMailAdresseColumn).setComponent(emailAdresseField);
        emailAdresseField.setSizeFull();
        emailAdresseField.setPlaceholder("Filter");

        // emailAdresss filter
        TextField bewerbungsDatumField = new TextField();
        bewerbungsDatumField.addValueChangeListener(event -> dataProviderApplication.addFilter(
                job -> StringUtils.containsIgnoreCase(String.valueOf(job.getbewerbungsDatum()),
                        bewerbungsDatumField.getValue())));

        bewerbungsDatumField.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(bewerbungsdatumColumn).setComponent(bewerbungsDatumField);
        bewerbungsDatumField.setSizeFull();
        bewerbungsDatumField.setPlaceholder("Filter");

        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(userDTO -> {

                    HorizontalLayout studentDetailsLayout = new HorizontalLayout();

                    // Fachbereich
                    TextField department = new TextField("Fachbereich");

                    if ((userDTO.getFaculty() != null) && (!userDTO.getFaculty().equals(""))) department.setValue(userDTO.getFaculty());
                    else department.setValue("nicht eingetragen");

                    department.setWidth("40em");
                    department.setReadOnly(true);

                    // Semester
                    TextField semester = new TextField("Semester");

                    if (userDTO.getSemester() != 0) semester.setValue(String.valueOf(userDTO.getSemester()));
                    else semester.setValue("nicht eingetragen");

                    semester.setWidth("10em");
                    semester.setReadOnly(true);

                    // Spezialisierung
                    TextField specialization = new TextField("Spezialisierung");

                    if ((userDTO.getSpecialization() != null) && (!userDTO.getSpecialization().equals(""))) specialization.setValue(userDTO.getSpecialization());
                    else specialization.setValue("nicht eingetragen");

                    specialization.setWidth("40em");
                    specialization.setReadOnly(true);

                    studentDetailsLayout.add(department, specialization, semester);


                    VerticalLayout descriptionLayout = new VerticalLayout();

                    // Beschreibung Student
                    TextArea descriptionStudent = new TextArea();

                    if ((userDTO.getDescription() != null) && (!userDTO.getDescription().equals(""))) descriptionStudent.setValue(userDTO.getDescription());
                    else descriptionStudent.setValue("Dieser Student besitzt leider keine nähere Beschreibung zu seiner Person.");

                    descriptionStudent.setWidthFull();
                    descriptionStudent.setReadOnly(true);

                    descriptionLayout.add(new H6("Studentenbeschreibung:"));
                    descriptionLayout.add(descriptionStudent);


                    HorizontalLayout jobLayout = new HorizontalLayout();

                    // Titel
                    TextField title = new TextField("Titel");

                    if ((userDTO.getTitle() != null) && (!userDTO.getTitle().equals(""))) title.setValue(userDTO.getTitle());
                    else title.setValue("nicht eingetragen");

                    title.setWidth("40em");
                    title.setReadOnly(true);

                    // Einstellungsdatum
                    TextField dateOfDeployment = new TextField("Einstellungsdatum");

                    if (userDTO.getDateOfDeployment() != null) dateOfDeployment.setValue(userDTO.getDateOfDeployment().toString());
                    else dateOfDeployment.setValue("nicht eingetragen");

                    dateOfDeployment.setWidth("12em");
                    dateOfDeployment.setReadOnly(true);

                    // Gehalt
                    TextField salary = new TextField("Gehalt");

                    if (userDTO.getSalary() != 0) salary.setValue(String.valueOf(userDTO.getSalary()));
                    else salary.setValue("nicht eingetragen");

                    Div euroSuffix = new Div();
                    euroSuffix.setText("€");

                    salary.setSuffixComponent(euroSuffix);

                    salary.setWidth("10em");
                    salary.setReadOnly(true);

                    // Wochenstunden
                    TextField hoursPerWeek = new TextField("Wochenstunden");

                    if (userDTO.getHoursPerWeek() != 0) hoursPerWeek.setValue(String.valueOf(userDTO.getHoursPerWeek()));
                    else hoursPerWeek.setValue("nicht eingetragen");

                    hoursPerWeek.setWidth("10em");
                    hoursPerWeek.setReadOnly(true);

                    jobLayout.add(title, dateOfDeployment, salary, hoursPerWeek);


                    VerticalLayout jobDescriptionLayout = new VerticalLayout();

                    // Stellenbeschreibung
                    TextArea descriptionJob = new TextArea();

                    if ((userDTO.getEmploymentDescription() != null) && (!userDTO.getEmploymentDescription().equals(""))) descriptionJob.setValue(userDTO.getEmploymentDescription());
                    else descriptionJob.setValue("Diese Stellenanzeige besitzt leider keine nähere Beschreibung.");

                    descriptionJob.setWidthFull();
                    descriptionJob.setReadOnly(true);

                    jobDescriptionLayout.add(new H6("Stellenbeschreibung"));
                    jobDescriptionLayout.add(descriptionJob);


                    VerticalLayout layout = new VerticalLayout();

                    layout.add(new H5("Details zum Studium von: " + userDTO.getFirstName() + " " + userDTO.getLastName()));
                    layout.add(studentDetailsLayout);
                    layout.add(descriptionLayout);
                    layout.add(new H4(" "));
                    layout.add(new H5("Details zur Stellenanzeige:"));
                    layout.add(jobLayout);
                    layout.add(jobDescriptionLayout);

                    return layout;
                })
        );


        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        if(bewerbungslist.isEmpty()){
            add(NotJobFound());
        }else {
            add(grid);
        }
    }

    private Component createTitle() { return new H2("Bewerbungen von Studenten"); }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    private Component NotJobFound() { return new H4("Leider wurden keine Bewerbungen von Studenten gefunden. Vermutlich wurde sich noch nicht auf eine Stelle beworben."); }
}