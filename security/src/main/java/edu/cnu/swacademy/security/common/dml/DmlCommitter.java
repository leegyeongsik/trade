package edu.cnu.swacademy.security.common.dml;

import edu.cnu.swacademy.security.common.BaseEntity;

public interface DmlCommitter {
    void execute(BaseEntity[] entities);
}
