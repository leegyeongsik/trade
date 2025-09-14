package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.common.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class SaveCommitter implements DmlCommitter {
    private final RepositoryMapper repositoryMapper;
    public SaveCommitter(RepositoryMapper repositoryMapper) {
        this.repositoryMapper = repositoryMapper;
    }

    @Override
    public void execute(BaseEntity[] baseEntities) {
        for (BaseEntity baseEntity : baseEntities) {
            JpaRepository<BaseEntity, ?> repo = (JpaRepository<BaseEntity, ?>) repositoryMapper.getRepository(baseEntity.getClass());
            repo.save(baseEntity);
        }
    }
}
