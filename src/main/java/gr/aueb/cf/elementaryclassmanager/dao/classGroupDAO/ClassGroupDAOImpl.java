package gr.aueb.cf.elementaryclassmanager.dao.classGroupDAO;

import gr.aueb.cf.elementaryclassmanager.model.ClassGroup;
import gr.aueb.cf.elementaryclassmanager.model.Grade;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Optional;

@Provider
@ApplicationScoped
public class ClassGroupDAOImpl implements IClassGroupDAO {
    @Override
    public ClassGroup insertClassGroup(ClassGroup classGroup) {
        getEntityManager().persist(classGroup);
        return classGroup;
    }

    @Override
    public ClassGroup updateClassGroup(ClassGroup classGroup) {
        getEntityManager().merge(classGroup);
        return classGroup;
    }

    @Override
    public void deleteClassGroup(Long id) {
        ClassGroup classGroupToDelete = getEntityManager().find(ClassGroup.class, id);
        getEntityManager().remove(classGroupToDelete);
    }

    @Override
    public Optional<ClassGroup> getById(Long id) {
        ClassGroup classGroupToReturn = getEntityManager().find(ClassGroup.class, id);
        return Optional.ofNullable(classGroupToReturn);

    }

    @Override
    public Optional<ClassGroup> getByNameAndGrade(Character name, Grade grade) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ClassGroup> selectQuery = builder.createQuery(ClassGroup.class);
        Root<ClassGroup> root = selectQuery.from(ClassGroup.class);

        ParameterExpression<Character> nameParam = builder.parameter(Character.class);
        ParameterExpression<Grade> gradeParam = builder.parameter(Grade.class); // Use Grade enum here

        Predicate namePredicate = builder.equal(root.get("name"), nameParam);
        Predicate gradePredicate = builder.equal(root.get("grade"), gradeParam);

        selectQuery.select(root).where(builder.and(namePredicate, gradePredicate));

        return getEntityManager()
                .createQuery(selectQuery)
                .setParameter(nameParam, name)
                .setParameter(gradeParam, grade)
                .getResultStream()
                .findAny();
    }





    private EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }
}


