package gr.aueb.cf.elementaryclassmanager.service.exceptions;

import gr.aueb.cf.elementaryclassmanager.model.Grade;

public class ClassGroupAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClassGroupAlreadyExists(Grade grade, Character groupName) {
        super("Class group " + groupName + " already exists in grade " + grade);
    }
}
