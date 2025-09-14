package edu.cnu.swacademy.security.common.worker;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.common.dml.DmlCommitter;


public class CommitWorker implements Worker{
    private final DmlCommitter dmlCommitter;
    private final BaseEntity[] baseEntities;

    public CommitWorker(DmlCommitter dmlCommitter, BaseEntity[] baseEntities) {
        this.dmlCommitter = dmlCommitter;
        this.baseEntities = baseEntities;
    }
    @Override
    public void execute(){
        dmlCommitter.execute(baseEntities);
    }
}
