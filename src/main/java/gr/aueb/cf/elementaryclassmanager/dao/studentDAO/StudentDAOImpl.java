package gr.aueb.cf.elementaryclassmanager.dao.studentDAO;

import gr.aueb.cf.elementaryclassmanager.model.Student;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Optional;

@Provider
@ApplicationScoped
public class StudentDAOImpl implements IStudentDAO {

    @Override
    public Student insertStudent(Student student) {
        getEntityManager().persist(student);
        return student;
    }

    @Override
    public Student updateStudent(Student student) {
        getEntityManager().merge(student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        Student studentToDelete = getEntityManager().find(Student.class, id);
        getEntityManager().remove(studentToDelete);
    }

    @Override
    public List<Student> getByLastName(String lastName) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Student> selectQuery = builder.createQuery(Student.class);
        Root<Student> root = selectQuery.from(Student.class);

        ParameterExpression<String> lastnameParam = builder.parameter(String.class);
        selectQuery.select(root).where(builder.like(root.get("lastName"),lastName));
        return getEntityManager()
                .createQuery(selectQuery)
                .setParameter(lastnameParam, lastName + "%")
                .getResultList();
    }

    @Override
    public List<Student> getByClassGroupId(Long classGroupId) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Student> selectQuery = builder.createQuery(Student.class);
        Root<Student> root = selectQuery.from(Student.class);

        ParameterExpression<Long> classGroupIdParam = builder.parameter(Long.class);
        selectQuery.select(root).where(builder.equal(root.get("classGroup").get("id"), classGroupIdParam));

        return getEntityManager()
                .createQuery(selectQuery)
                .setParameter(classGroupIdParam, classGroupId)
                .getResultList();
    }


    @Override
    public Optional<Student> getById(Long id) {
        Student studentToReturn = getEntityManager().find(Student.class, id);
        return Optional.ofNullable(studentToReturn);

    }

    @Override
    public Optional<Student> getBySsn(String ssn) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        Root<Student> root = query.from(Student.class);

        ParameterExpression<String> ssnParam = builder.parameter(String.class);
        query.select(root).where(builder.equal(root.get("ssn"), ssnParam));

        return getEntityManager()
                .createQuery(query)
                .setParameter(ssnParam, ssn)
                .getResultStream()
                .findAny();
    }

    @Override
    public void archiveStudent(Long studentId) {
        Student studentToArchive = getEntityManager().find(Student.class, studentId);
        if (studentToArchive != null) {
            studentToArchive.setArchived(true);
            getEntityManager().merge(studentToArchive);
        }
    }

    @Override
    public Optional<Student> getActiveById(Long id) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Student> selectQuery = builder.createQuery(Student.class);
        Root<Student> root = selectQuery.from(Student.class);

        // Add predicates for ID and active status
        Predicate idPredicate = builder.equal(root.get("id"), id);
        Predicate activePredicate = builder.isFalse(root.get("archived"));
        selectQuery.select(root).where(builder.and(idPredicate, activePredicate));

        return getEntityManager()
                .createQuery(selectQuery)
                .getResultStream()
                .findAny();
    }


    private EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}
