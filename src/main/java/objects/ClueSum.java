package objects;

import java.util.List;

public class ClueSum {
    private List<CnfVar> vars;
    private int sum;

    public ClueSum(List<CnfVar> vars, int sum) {
        this.vars = vars;
        this.sum = sum;
    }
}
