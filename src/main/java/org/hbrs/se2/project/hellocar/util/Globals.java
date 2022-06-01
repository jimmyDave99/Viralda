package org.hbrs.se2.project.hellocar.util;

import com.vaadin.flow.component.Component;

public class Globals {
    public static String CURRENT_USER = "current_User";

    public static class Pages {
        public static final String REGISTRATION_VIEW = "registration";
        public static final String LANDING_PAGE_COMPANY_VIEW = "landing-page-company";
        public static final String LANDING_PAGE_STUDENT_VIEW = "landing-page-student";
        public static final String PROFIL_VIEW = "profil";

        public static final String LOGIN_VIEW = "login";
        public static final String MAIN_VIEW = "";
    }

    public static class Roles {
        public static final String ADMIN = "admin";
        public static final String USER = "user";
        public static final String STUDENT = "Student";
        public static final String UNTERNEHMEN = "Unternehmen";
    }

    public static class Errors {
        public static final String NOUSERFOUND = "nouser";
        public static final String SQLERROR = "sql";
        public static final String DATABASE = "database";
    }

}
