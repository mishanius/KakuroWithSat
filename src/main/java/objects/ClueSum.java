package objects;

import java.util.HashSet;
import java.util.Set;

public class ClueSum {
    private Set<CellCnfVar> vars;
    private int sum;
    private int clueNumber;

    public ClueSum(int sum) {
        this(sum, 0);
    }

    public ClueSum(int sum, int clueNumber) {
        this.sum = sum;
        this.clueNumber = clueNumber;
        this.vars = new HashSet<>();
    }

    public int getClueNumber(){
        return clueNumber;
    }

    public ClueSum addCnfVar(CellCnfVar var) {
        //update max possible value of cnf var
        var.updateMax(this.sum);
        vars.add(var);
        return this;
    }

    public Set<CellCnfVar> getVars() {
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
