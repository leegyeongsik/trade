package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.common.BaseEntity;


public class Worker {
    private final DmlWorker dmlWorker;
    private final BaseEntity[] baseEntities;

    public Worker(DmlWorker dmlWorker, BaseEntity[] baseEntities) {
        this.dmlWorker = dmlWorker;
        this.baseEntities = baseEntities;
    }
    public void execute(){
        dmlWorker.execute(baseEntities);
    }
}
