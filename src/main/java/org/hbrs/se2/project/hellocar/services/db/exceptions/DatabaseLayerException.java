package org.hbrs.se2.project.hellocar.services.db.exceptions;

import java.util.ArrayList;
import java.util.List;

public class DatabaseLayerException extends Exception {

    private List<String> reasons;

    public DatabaseLayerException(String reason) {
        this.reasons = new ArrayList<String>();
        addReason(reason);
    }

    public List<String> getReasons() {
        return reasons;
    }

    public DatabaseLayerException addReason(String reason) {
        this.reasons.add(reason);
        return this;
    }

}
