package gr.aueb.cf.elementaryclassmanager.service.exceptions;

public class GradeIncrementNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GradeIncrementNotAllowedException() {
        super("The current grade is the highest and cannot be incremented.");
    }
}
