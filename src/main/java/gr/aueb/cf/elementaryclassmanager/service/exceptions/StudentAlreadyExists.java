package gr.aueb.cf.elementaryclassmanager.service.exceptions;

public class StudentAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StudentAlreadyExists(String ssn) {
        super("Student with SSN " + ssn + " already exists");
    }
}
