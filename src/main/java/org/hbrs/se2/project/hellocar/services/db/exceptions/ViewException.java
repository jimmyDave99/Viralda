package org.hbrs.se2.project.hellocar.services.db.exceptions;

public class ViewException extends Exception{
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ViewException( String reason ) {
        this.reason = reason;
    }
}
