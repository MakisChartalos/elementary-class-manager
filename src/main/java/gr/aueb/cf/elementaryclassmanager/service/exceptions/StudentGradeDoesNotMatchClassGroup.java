package gr.aueb.cf.elementaryclassmanager.service.exceptions;

public class StudentGradeDoesNotMatchClassGroup extends Exception {
    private static final long serialVersionUID = 1L;

    public StudentGradeDoesNotMatchClassGroup(Long studentId, Long classGroupId ) {
        super("Student with id " + studentId + " does not match class group with id " + classGroupId);
    }
}
