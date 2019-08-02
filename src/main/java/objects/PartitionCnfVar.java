package objects;

import static objects.Constants.ROW_COLL_SUM_PARTITION_CNT_TYPE;

public class PartitionCnfVar extends CnfVar {

    public PartitionCnfVar(String value) {
        super(value);
    }

    public PartitionCnfVar(boolean isNot, String value) {
        super(isNot, value);
    }

    @Override
    int getType() {
        return ROW_COLL_SUM_PARTITION_CNT_TYPE;
    }
}
