package gr.aueb.cf.elementaryclassmanager.service.exceptions;

public class TeacherAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TeacherAlreadyExists(String teacherRegistrationNumber) {
      super("Teacher with registration number " + teacherRegistrationNumber + " already exists");
    }
}
