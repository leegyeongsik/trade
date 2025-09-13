package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.common.BaseEntity;

public interface DmlWorker {
    void execute(BaseEntity[] entities);
}
