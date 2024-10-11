package gr.aueb.cf.elementaryclassmanager.service.exceptions;

public class ClassGroupFullException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClassGroupFullException(Long classGroupId) {
        super("Class group id " + classGroupId + " is full");
    }
}
