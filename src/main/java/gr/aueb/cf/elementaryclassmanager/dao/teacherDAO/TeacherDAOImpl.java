package gr.aueb.cf.elementaryclassmanager.dao.teacherDAO;

import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Optional;


@Provider
@ApplicationScoped
public class TeacherDAOImpl implements ITeacherDAO {

    @Override
    public Teacher insertTeacher(Teacher teacher) {
        getEntityManager().persist(teacher);
        return teacher;
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        getEntityManager().merge(teacher);
        return teacher;
    }

    @Override
    public void deleteTeacher(Long id) {
        Teacher teacherToDelete = getEntityManager().find(Teacher.class, id);
        getEntityManager().remove(teacherToDelete);
    }

    @Override
    public List<Teacher> getByLastname(String lastname) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Teacher> selectQuery = builder.createQuery(Teacher.class);
        Root<Teacher> root = selectQuery.from(Teacher.class);

        ParameterExpression<String> lastnameParam = builder.parameter(String.class);
        selectQuery.select(root).where(builder.like(root.get("lastname"), lastnameParam));
        return getEntityManager()
                .createQuery(selectQuery)
                .setParameter(lastnameParam, lastname + "%")
                .getResultList();
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        Teacher teacherToReturn = getEntityManager().find(Teacher.class, id);
        return Optional.ofNullable(teacherToReturn);
    }

    @Override
    public Optional<Teacher> getByRegistrationNumber(String registrationNumber) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Teacher> selectQuery = builder.createQuery(Teacher.class);
        Root<Teacher> root = selectQuery.from(Teacher.class);

        ParameterExpression<String> registrationNumberParam = builder.parameter(String.class);
        selectQuery.select(root).where(builder.equal(root.get("registrationNumber"), registrationNumberParam));

        List<Teacher> result = getEntityManager()
                .createQuery(selectQuery)
                .setParameter(registrationNumberParam, registrationNumber)
                .getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}

