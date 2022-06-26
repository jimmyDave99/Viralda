package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
//import org.hbrs.se2.project.hellocar.control.AuthorizationControl;
import org.hbrs.se2.project.hellocar.control.ManageExistingUserControl;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.security.NoSuchAlgorithmException;

@Route(value = Globals.Pages.PROFIL_VIEW, layout = AppView.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/profile.css")
public class ProfilView extends Div {

    private final Tab profile;
    private final Tab securitySettings;
    private final Tab notifications;

    private final VerticalLayout content;

    private final Button cancel = new Button("Abbrechen");
    private final Button save = new Button("Speichern");
    private final Button editProfil = new Button("Profil bearbeiten");

    private final TextField empty = new TextField();

    // user attributes
    private EmailField email;
    private TextField role;
    private TextArea description;
    private Image image;

    // student attributes
    private TextField firstName;
    private TextField lastName;
    private DatePicker dateOfBirth;
    private TextField faculty;
    private IntegerField semester;
    private TextField specialization;

    // company attributes
    private TextField companyName;
    private TextField branch;

    // password fields for resetting the password
    private PasswordField oldPassword;
    private PasswordField newPassword;
    private PasswordField newPasswordAgain;

    private final Binder<UserDTOImpl> binder = new Binder(UserDTOImpl.class);
    private final ManageExistingUserControl userService;

    // ------  functions for all fields  ------
    private void setFieldsStudentAttributes() {
        firstName = new TextField("Vorname");
        lastName = new TextField("Name");
        email = new EmailField("E-Mail-Adresse");
        dateOfBirth = new DatePicker("Geburtsdatum");
        role = new TextField("Rolle");
        faculty = new TextField("Fakultät");
        semester = new IntegerField("Semester");
        semester.setMin(1);
        semester.setMax(30);
        specialization = new TextField("Spezialisierung");
        image = new Image();
        description = new TextArea("Beschreibung");
    }

    private void setFieldsCompanyAttributes() {
        companyName = new TextField("Unternehmensname");
        email = new EmailField("E-Mail-Adresse");
        role = new TextField("Rolle");
        description = new TextArea("Beschreibung");
    }

    private void setFieldsEditUserPassword() {
        oldPassword = new PasswordField("Altes Passwort");
        newPassword = new PasswordField("Neues Passwort");
        newPassword.setHelperText("Ein Passwort besteht aus mind. 8 Zeichen, wovon mind. eines ein Buchstabe und eine Zahl ist.");
        newPassword.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        newPassword.setErrorMessage("Passwort entspricht nicht der Form.");
        newPasswordAgain = new PasswordField("Neues Passwort wiederholen");
    }



    public ProfilView(ManageExistingUserControl userService) {
        addClassName("profile");
        this.userService = userService;

        add(createTitle());

        // The different tabs
        profile = new Tab(
                VaadinIcon.USER.create(),
                new Span("Profil")
        );

        securitySettings = new Tab(
                VaadinIcon.COG.create(),
                new Span("Sicherheitseinstellungen")
        );

        notifications = new Tab(
                VaadinIcon.BELL.create(),
                new Span("Benachrichtigungen")
        );

        // Set the icon on top
        for (Tab tab : new Tab[]{profile, securitySettings, notifications}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        Tabs tabs = new Tabs(profile, securitySettings, notifications);

        tabs.addSelectedChangeListener(event -> setContent(event.getSelectedTab()));

        // Content for the tabs
        content = new VerticalLayout();
        content.setSpacing(false);
        setContent(tabs.getSelectedTab());

        add(tabs, content);
    }

    // creates the title with the last- and first name of a person or the company name if the user is a company
    private Component createTitle() {
        H2 title = new H2();

        if (getCurrentUser().getRole().equals("Student")) {
            title = new H2("Studentenprofil von " + getCurrentUser().getFirstName() + " " + getCurrentUser().getLastName());

        } else if (getCurrentUser().getRole().equals("Unternehmen")) {
            title = new H2("Unternehmensprofil von " + getCurrentUser().getCompanyName());

        } else {
            System.out.println("Error: User is not a student or a company.");
        }

        return title;
    }

    // function to produce the clicked tab
    private void setContent(Tab tab) {
        content.removeAll();

        if (tab.equals(profile)) {

            if (getCurrentUser().getRole().equals("Student")) {
                content.add(createButtonLayoutShowStudentAttributes());
                content.add(createFormLayoutShowStudentAttributes());

            } else if (getCurrentUser().getRole().equals("Unternehmen")) {
                content.add(createButtonLayoutShowCompanyAttributes());
                content.add(createFormLayoutShowCompanyAttributes());
            }

        } else if (tab.equals(securitySettings)) {
            content.add(createButtonLayoutTabSecuritySettings());
            content.add(createFormLayoutChangePassword());

        } else if (tab.equals(notifications)) {
            content.add(new Paragraph("Du hast keine neuen Benachrichtigungen!"));
            content.add(new Text("Möchtest du dich nicht bewerben um das zu ändern?"));
            content.add(new Text(" Besser wäre es."));
        }
    }


    // ------  functions for tab "Profil" with Role "Student" with current attributes of a user  ------
    private Component createButtonLayoutShowStudentAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();

        buttonLayout.add(editProfil);
        editProfil.addClickListener(event -> navigateToSubBarEditStudentAttributes());

        return buttonLayout;
    }

    private Component createFormLayoutShowStudentAttributes() {
        setFieldsStudentAttributes();

        firstName.setValue(getCurrentUser().getFirstName());
        firstName.setReadOnly(true);

        lastName.setValue(getCurrentUser().getLastName());
        lastName.setReadOnly(true);

        email.setValue(getCurrentUser().getEmail());
        email.setReadOnly(true);

        if(getCurrentUser().getDateOfBirth() == null) dateOfBirth.setPlaceholder("'Platzhalter Geburtstag'");
        else dateOfBirth.setValue(getCurrentUser().getDateOfBirth());
        dateOfBirth.setReadOnly(true);

        if(getCurrentUser().getFaculty() == null) faculty.setPlaceholder("Z.B. Informatik");
        else faculty.setValue(getCurrentUser().getFaculty());
        faculty.setReadOnly(true);

        if(getCurrentUser().getSemester() == 0) semester.setPlaceholder("1");
        else semester.setValue(getCurrentUser().getSemester());
        semester.setReadOnly(true);

        if(getCurrentUser().getSpecialization() == null) specialization.setPlaceholder("Z.B. Visual Computing");
        else specialization.setValue(getCurrentUser().getSpecialization());
        specialization.setReadOnly(true);

        if(getCurrentUser().getDescription() == null) description.setPlaceholder("Beschreibe dich selbst!");
        else description.setValue(getCurrentUser().getDescription());
        description.setReadOnly(true);

        if(getCurrentUser().getProfilePicture() == null) image.setAlt("User Bild");

        role.setValue(getCurrentUser().getRole());
        role.setReadOnly(true);


        FormLayout formLayout = new FormLayout();

        formLayout.add(
                firstName, lastName,
                email, dateOfBirth,
                faculty, semester,
                specialization, description,
                role);

        return formLayout;
    }


    // ------  functions for tab "Profil" with role "Student" with the ability to change the attributes  ------
    private Component createButtonLayoutTabProfileEditStudentAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();

        buttonLayout.add(save);
        buttonLayout.add(cancel);

        save.addClickListener(event -> {
            if(binder.validate().isOk()){
                try {
                    binderBindFieldsStudent();
                    boolean isOK = userService.updateUser(binder.getBean());
                    if(isOK){
                        navigateToSubBarShowStudentAttributesWithSave();
                        Notification.show("Änderungen erfolgreich gespeichert");
                    }
                } catch (DatabaseLayerException | ValidationException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.addClickListener(event -> navigateToSubBarShowStudentAttributesWithoutSave());

        return buttonLayout;
    }

    private Component createFormLayoutEditStudentAttributes() {
        setFieldsStudentAttributes();

        firstName.setValue(getCurrentUser().getFirstName());
        lastName.setValue(getCurrentUser().getLastName());
        email.setValue(getCurrentUser().getEmail());
        email.setReadOnly(true);
        if(getCurrentUser().getDateOfBirth() == null){
            dateOfBirth.setPlaceholder("'Platzhalter Geburtsdatum'");
        } else {
            dateOfBirth.setValue(getCurrentUser().getDateOfBirth());
        }
        if(!(getCurrentUser().getFaculty() == null)) faculty.setValue(getCurrentUser().getFaculty());
        if(!(getCurrentUser().getSemester() == 0)) semester.setValue(getCurrentUser().getSemester());
        if(!(getCurrentUser().getSpecialization() == null)) specialization.setValue(getCurrentUser().getSpecialization());
        if(!(getCurrentUser().getDescription() == null)) description.setValue(getCurrentUser().getDescription());

        role.setValue(getCurrentUser().getRole());
        role.setReadOnly(true);

        FormLayout formLayout = new FormLayout();

        formLayout.add(firstName, lastName,
                email, dateOfBirth,
                faculty, semester,
                specialization, description,
                role);

        return formLayout;
    }


    // ------  functions for tab "Profil" with Role "Unternehmen" with current attributes of a user  ------
    private Component createButtonLayoutShowCompanyAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();

        buttonLayout.add(editProfil);
        editProfil.addClickListener(event -> navigateToSubBarEditCompanyAttributes());

        return buttonLayout;
    }

    private Component createFormLayoutShowCompanyAttributes() {
        setFieldsCompanyAttributes();

        companyName.setValue(getCurrentUser().getCompanyName());
        companyName.setReadOnly(true);

        email.setValue(getCurrentUser().getEmail());
        email.setReadOnly(true);

        role.setValue(getCurrentUser().getRole());
        role.setReadOnly(true);

        if(getCurrentUser().getDescription() == null) description.setPlaceholder("Beschreibe dein Unternehmen.");
        else description.setValue(getCurrentUser().getDescription());
        description.setReadOnly(true);

        FormLayout formLayout = new FormLayout();

        formLayout.add(companyName, email,
                role, description);

        return formLayout;
    }


    // ------  functions for tab "Profil" with role "Unternehmen" with the ability to change the attributes  ------
    private Component createButtonLayoutEditCompanyAttributes() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();

        buttonLayout.add(save);
        buttonLayout.add(cancel);

        save.addClickListener(event -> {
            if(binder.validate().isOk()){
                try {
                    binderBindFieldsCompany();
                    boolean isOK = userService.updateUser(binder.getBean());
                    if(isOK){
                        navigateToSubBarShowCompanyAttributesWithSaving();
                        Notification.show("Änderungen erfolgreich gespeichert");
                    }

                } catch (DatabaseLayerException | ValidationException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.addClickListener(event -> navigateToSubBarShowCompanyAttributesWithoutSaving());

        return buttonLayout;
    }

    private Component createFormLayoutEditCompanyAttributes() {
        setFieldsCompanyAttributes();

        companyName.setValue(getCurrentUser().getCompanyName());
        email.setValue(getCurrentUser().getEmail());
        email.setReadOnly(true);

        if(getCurrentUser().getDescription() == null) description.setPlaceholder("Beschreibe dein Unternehmen");
        else description.setValue(getCurrentUser().getDescription());

        role.setValue(getCurrentUser().getRole());
        role.setReadOnly(true);

        FormLayout formLayout = new FormLayout();

        formLayout.add(companyName, email,
                role, description);

        return formLayout;
    }


    // ------  functions for tab "Sicherheitseinstellungen"  ------
    private Component createFormLayoutChangePassword() {
        setFieldsEditUserPassword();

        FormLayout formLayout = new FormLayout();

        formLayout.add(new H4("Passwort ändern"), new H4(""),
                oldPassword, new H4(""),
                newPassword, new H4(""),
                newPasswordAgain);

        return formLayout;
    }

    // Buttonlayout
    private Component createButtonLayoutTabSecuritySettings() {
        content.removeAll();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(save);
        buttonLayout.add(cancel);

        cancel.addClickListener(event -> {
            navigateToSubBarSecuritySettingsWithoutSaving();
        });

        save.addClickListener(event -> {
            try{
                if(binder.validate().isOk()){
                    binderBindPassword();
                    boolean isOK = userService.updateUserPassword(binder.getBean(), oldPassword.getValue(), getCurrentUser().getEmail());
                    if(isOK) {
                        Notification.show("Änderungen erfolgreich gespeichert.");
                        navigateToSubBarSecuritySettingsWithSaving();
                    }
                    else Notification.show("Änderungen konnten nicht gespeichert werden.");
                } else Notification.show("Änderungen konnten nicht gespeichert werden.");

            } catch (DatabaseLayerException | NoSuchAlgorithmException | ValidationException e) {
                e.printStackTrace();
            }

        });

        return buttonLayout;
    }


    // ------  functions to navigate between the tabs for student attributes  ------
    private void navigateToSubBarEditStudentAttributes() {
        content.removeAll();

        content.add(createButtonLayoutTabProfileEditStudentAttributes());
        content.add(createFormLayoutEditStudentAttributes());
    }

    private void navigateToSubBarShowStudentAttributesWithSave() {
        content.add(createButtonLayoutShowStudentAttributes());
        content.add(createFormLayoutShowStudentAttributes());
    }

    private void navigateToSubBarShowStudentAttributesWithoutSave() {
        content.removeAll();

        content.add(createButtonLayoutShowStudentAttributes());
        content.add(createFormLayoutShowStudentAttributes());
    }


    // ------  functions to navigate between the tabs for company attributes  ------
    private void navigateToSubBarEditCompanyAttributes() {
        content.removeAll();

        content.add(createButtonLayoutEditCompanyAttributes());
        content.add(createFormLayoutEditCompanyAttributes());
    }

    private void navigateToSubBarShowCompanyAttributesWithSaving() {
        content.add(createButtonLayoutShowCompanyAttributes());
        content.add(createFormLayoutShowCompanyAttributes());
    }

    private void navigateToSubBarShowCompanyAttributesWithoutSaving() {
        content.removeAll();

        content.add(createButtonLayoutShowCompanyAttributes());
        content.add(createFormLayoutShowCompanyAttributes());
    }


    // ------  functions to navigate between the tabs for password changes  ------
    private void navigateToSubBarSecuritySettingsWithoutSaving() {
        content.removeAll();

        content.add(createButtonLayoutTabSecuritySettings());
        content.add(createFormLayoutChangePassword());
    }


    private void navigateToSubBarSecuritySettingsWithSaving() {
        content.add(createButtonLayoutTabSecuritySettings());
        content.add(createFormLayoutChangePassword());
    }


    // ------  other necessary functions  ------

    private void binderBindFieldsStudent() throws ValidationException {
        binder.forField(firstName)
                .bind(UserDTOImpl::getFirstName, UserDTOImpl::setFirstName);
        binder.forField(description)
                .bind(UserDTOImpl::getDescription, UserDTOImpl::setDescription);
        binder.forField(lastName)
                .bind(UserDTOImpl::getLastName, UserDTOImpl::setLastName);
        binder.forField(email)
                .bind(UserDTOImpl::getEmail, UserDTOImpl::setEmail);
        binder.forField(dateOfBirth)
                .bind(UserDTOImpl::getDateOfBirth, UserDTOImpl::setDateOfBirth);
        binder.forField(faculty)
                .bind(UserDTOImpl::getFaculty, UserDTOImpl::setFaculty);
        binder.forField(semester)
                .bind(UserDTOImpl::getSemester, UserDTOImpl::setSemester);
        binder.forField(specialization)
                .bind(UserDTOImpl::getSpecialization, UserDTOImpl::setSpecialization);
        binder.forField(role)
                .bind(UserDTOImpl::getRole, UserDTOImpl::setRole);
        binder.writeBean((UserDTOImpl) getCurrentUser());

        clearForm();
    }

    private void binderBindFieldsCompany() throws ValidationException {
        binder.forField(role)
                .bind(UserDTOImpl::getRole, UserDTOImpl::setRole);
        binder.forField(description)
                .bind(UserDTOImpl::getDescription, UserDTOImpl::setDescription);
        binder.forField(companyName)
                .bind(UserDTOImpl::getCompanyName, UserDTOImpl::setCompanyName);
        binder.forField(branch)
                .bind(UserDTOImpl::getBranche, UserDTOImpl::setBranche);
        binder.writeBean((UserDTOImpl) getCurrentUser());

        clearForm();
    }

    private void binderBindPassword() throws ValidationException {
        binder.forField(newPassword)
                .bind(UserDTOImpl::getPassword, UserDTOImpl::setPassword);
        binder.forField(newPasswordAgain)
                .bind(UserDTOImpl::getConfirmPassword, UserDTOImpl::setConfirmPassword);
        binder.writeBean((UserDTOImpl) getCurrentUser());

        clearForm();
    }


    private void clearForm() {
        binder.setBean((UserDTOImpl) getCurrentUser());
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }
}
