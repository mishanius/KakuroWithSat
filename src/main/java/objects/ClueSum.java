package objects;

import java.util.HashSet;
import java.util.Set;

public class ClueSum {
    private Set<CnfVar> vars;
    private int sum;

    public ClueSum(int sum) {
        this.vars = new HashSet<>();
        this.sum = sum;
    }

    public ClueSum addCnfVar(CnfVar var) {
        //update max possible value of cnf var
        var.updateMax(this.sum);
        vars.add(var);
        return this;
    }

    public Set<CnfVar> getVars() {
        return vars;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "" + sum + " " + vars;
    }
}
