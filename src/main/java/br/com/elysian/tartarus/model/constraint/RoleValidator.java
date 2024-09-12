package br.com.elysian.tartarus.model.constraint;


import br.com.elysian.tartarus.model.AccountRole;
import br.com.elysian.tartarus.repository.AccountRoleDAO;
import br.com.elysian.tartarus.utils.BeanUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;


@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true)
public class RoleValidator implements ConstraintValidator<UniqueDefaultRole, AccountRole> {

    private AccountRoleDAO accountRoleDAO;
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueDefaultRole constraintAnnotation) {
        accountRoleDAO = BeanUtil.getBean(AccountRoleDAO.class);
        entityManager = BeanUtil.getBean(EntityManager.class);
    }

    @Override
    public boolean isValid(AccountRole accountRole, ConstraintValidatorContext context) {
        try {
            entityManager.setFlushMode(FlushModeType.COMMIT);

            if (accountRole.getIsDefault()) {
                List<AccountRole> existingDefaultRoles = accountRoleDAO.findByAccountAndIsDefault(accountRole.getAccount(), true);
                return existingDefaultRoles.isEmpty();
            }
            return true;

        } finally {
            entityManager.setFlushMode(FlushModeType.AUTO);
        }
    }
}
