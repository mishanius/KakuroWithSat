package objects;

import static objects.Constants.CELL_VALUE_CNT_TYPE;

public class CellCnfVar extends CnfVar {

    private int maxVal = 0;

    public CellCnfVar(String index) {
        super(index);
    }

    public CellCnfVar(int index) {
        super("" + index);
    }

    public CellCnfVar(String index, boolean isnot) {
        super(index);
        if (isnot) {
            setNot();
        }
    }

    @Override
    int getType() {
        return CELL_VALUE_CNT_TYPE;
    }


    /**
     * for a cnf var that representing some cell
     * the max val is min({clue.sum s.t var in clue.vars})
     **/
    public void updateMax(int val) {
        if (maxVal == 0) {
            maxVal = val;
        } else if (maxVal > val) {
            maxVal = val;
        }
    }

    public int getMaxVal() {
        return maxVal;
    }
}
