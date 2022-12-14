package org.hbrs.se2.project.hellocar.util;

public class Globals {
    public static String CURRENT_USER = "current_User";

    public static class Pages {
        public static final String REGISTRATION_VIEW = "registration";
        public static final String LANDING_PAGE_COMPANY_VIEW = "landing-page-company";
        public static final String SHOW_JOB_COMPANY_VIEW = "show-job-company";
        public static final String LANDING_PAGE_STUDENT_VIEW = "landing-page-student";
        public static final String JOB_APPLICATION_VIEW = LANDING_PAGE_STUDENT_VIEW+ "/job/";
        public static final String JOB_COMPANY_VIEW = SHOW_JOB_COMPANY_VIEW+ "/job/";
        public static final String ENTER_STELLENANZEIGE_VIEW = "enter";
        public static final String PROFIL_VIEW = "profil";

        public static final String LOGIN_VIEW = "login";
        public static final String MAIN_VIEW = "";
    }

    public static class Roles {
        public static final String ADMIN = "admin";
        public static final String STUDENT = "Student";
        public static final String UNTERNEHMEN = "Unternehmen";
    }

    public static class Errors {
        public static final String NOUSERFOUND = "nouser";
        public static final String SQLERROR = "sql";
        public static final String DATABASE = "database";
        public static final String PROBLEM = "Fehler im SQL-Befehl!";
    }

    public static class JobStatus {
        public static final String BEWORBEN = "BEWORBEN";
        public static final String ZURUCKGEZOGEN = "ZURÜCKGEZOGEN";
        public static final String ABGELEHNT = "ABGELEHNT";
        public static final String ANGENOMMEN = "ANGENOMMEN";
        public static final String AKTIV = "AKTIV";
        public static final String INAKTIV = "INAKTIV";
    }

}
