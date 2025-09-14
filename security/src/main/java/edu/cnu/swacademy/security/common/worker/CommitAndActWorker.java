package edu.cnu.swacademy.security.common.worker;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.common.dml.DmlCommitter;

import java.util.UUID;

public class CommitAndActWorker implements Worker{
    private final DmlCommitter dmlCommitter;
    private final BaseEntity[] baseEntities;
    private final String redisKey;
    private final UUID setUUid;

    public CommitAndActWorker(String redisKey, DmlCommitter dmlCommitter, BaseEntity[] baseEntities, UUID setUUid) {
        this.dmlCommitter = dmlCommitter;
        this.baseEntities = baseEntities;
        this.redisKey = redisKey;
        this.setUUid = setUUid;
    }
    @Override
    public void execute() {
        if(baseEntities.length>1){
            return;
        }
        dmlCommitter.execute(baseEntities);
        // 여기서 레디스에서 uuid 로 찾아서 poll
        // 서비스에서 변경할때 이력 uuid 넣고
        // 여기서 레디스에서 찾아서 uuid 지우셈
        // 그리고 그 레디스 row가 만료됬을때 set이 비워졌으면 만료시키고 아니면 좀더 늘리셈 그래서 만료됬는데 아직 스레드가 처리 못했는데 db에서 읽어오는거 방지
    }
}
