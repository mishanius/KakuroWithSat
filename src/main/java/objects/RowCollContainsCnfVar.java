package objects;

import static objects.Constants.ROW_COLL_CONTAINS_VALUE_CNF;

public class RowCollContainsCnfVar extends CnfVar {

    public RowCollContainsCnfVar(String value) {
        super(value);
    }

    public RowCollContainsCnfVar(boolean isNot, String value) {
        super(isNot, value);
    }

    @Override
    int getType() {
        return ROW_COLL_CONTAINS_VALUE_CNF;
    }
}
