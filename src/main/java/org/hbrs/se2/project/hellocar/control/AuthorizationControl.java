package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthorizationControl {

    /**
     * Methode zur Überprüfung, ob ein Benutzer eine gegebene Rolle besitzt
     * 
     */
   public boolean isUserInRole(UserDTO user , String role  ) {
        if ( user.getRole().equals(role) ) return true;
        return false;
    }

    /**
     * Erweiterte Methode zur Bestimmung, ob ein User mit einer bestimmten Rolle ein
     * Feature (hier: ein Web-Seite bzw. eine View) zu einem bestimmten Kontext (Bsp: ein Tageszeit, mit
     * einem bestimmten Device etc.) angezeigt bekommt
     */
    public boolean isUserisAllowedToAccessThisFeature(UserDTO user , String role , String feature , String[] context  ) {
        String userRole = user.getRole();
        // Check, ob ein Benutzer eine Rolle besitzt:
        // Check, ob context erfüllt ist, bleibt hier noch aus, kann man nachziehen
        if ( userRole.equals(role) )
            // Einfache (!) Kontrolle,  ob die Rolle auf ein Feature zugreifen kann
            return checkRolleWithFeature(userRole, feature);

        return false;
    }

    private boolean checkRolleWithFeature(String rolle, String feature) {
        String[] rolles = getRollesForFeature(feature);
        if  ( rolles.length == 0 ) return false;
        return Arrays.asList(rolles).contains(rolle);


    }

    /**
     * Methode zur Bestimmung, welche Rollen ein Feature (hier: View) verwenden dürfen
     * Diese Zuordnung sollte man natürlich in Praxis in einer Datenbank hinterlegen, so dass man
     * die Zuordnungen flexibel anpassen kann.
     * @param feature beschreibt eine View der Webseite
     * @return die
     */
    private String[] getRollesForFeature(String feature) {
        // Da im Framework nur zwei Views unterstützt werden, werden auch diese nur unterschieden
        if ( feature.equals( Globals.Pages.LANDING_PAGE_STUDENT_VIEW ) ) {
            return new String[]{ Globals.Roles.STUDENT };
        } else if (  feature.equals( Globals.Pages.LANDING_PAGE_COMPANY_VIEW ) ) {
            return (  new String[] { Globals.Roles.UNTERNEHMEN }  );
        }
        return new String[] {};
    }


}
