package org.hbrs.se2.builder;

import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;

import java.awt.image.BufferedImage;

public class UserBuilder {

    private UserDTOImpl userDTO = null;

    public static UserBuilder getInstance() {
        return new UserBuilder();
    }

    public UserBuilder createNewUser() {
        userDTO = new UserDTOImpl();
        return this;
    }

    public UserBuilder withUserID(int id) {
        this.userDTO.setUserId(id);
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.userDTO.setEmail(email);
        return this;
    }
    public UserBuilder withRole( String role ) {
        this.userDTO.setRole(role);
        return this;
    }

    public UserBuilder withPassword( String password ) {
        this.userDTO.setPassword(password);
        return this;
    }

    public UserBuilder withProfilePicture( BufferedImage profilePicture ) {
        this.userDTO.setProfilePicture(profilePicture);
        return this;
    }

    public UserBuilder withPlz( int plz ) {
        this.userDTO.setPlz(plz);
        return this;
    }

    public UserBuilder withLand( String land ) {
        this.userDTO.setLand(land);
        return this;
    }

    public UserBuilder withStadt( String stadt ) {
        this.userDTO.setStadt(stadt);
        return this;
    }

    public UserBuilder withStrasse( String strasse ) {
        this.userDTO.setStrasse(strasse);
        return this;
    }

    public UserBuilder withNummer( int nummer ) {
        this.userDTO.setNummer(nummer);
        return this;
    }

    public UserBuilder withStudentID( int studentID ) {
        this.userDTO.setStudentId(studentID);
        return this;
    }

    public UserBuilder withFirstName(String firstName){
        this.userDTO.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName){
        this.userDTO.setLastName(lastName);
        return this;
    }

    public UserBuilder withUnternehmenID( int unternehmenID ) {
        this.userDTO.setUnternehmenId(unternehmenID);
        return this;
    }

    public UserBuilder withCompanyName( String companyName ) {
        this.userDTO.setCompanyName(companyName);
        return this;
    }

    public UserBuilder withBranche( String branche ) {
        this.userDTO.setBranche(branche);
        return this;
    }

    public UserBuilder withDescription( String description ) {
        this.userDTO.setDescription(description);
        return this;
    }

    public UserDTOImpl build() {
        return this.userDTO;
    }

}

