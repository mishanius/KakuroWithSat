package objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClueSum {
    private Set<CnfVar> vars;
    private List<CnfVar> orderdvars;
    private int sum;
    private int clueNumber;

    public ClueSum(int sum, int clueNumber) {
        this.vars = new HashSet<>();
        this.orderdvars = new ArrayList<>();
        this.sum = sum;
        this.clueNumber = clueNumber;
    }

    public ClueSum addCnfVar(CnfVar var) {
        //update max possible value of cnf var
        var.updateMax(this.sum);
        orderdvars.add(var);
        vars.add(var);
        return this;
    }

    public int getClueNumber() {
        return clueNumber;
    }

    public Set<CnfVar> getVars() {
        return vars;
    }

    public List<CnfVar> getOrderdvars(){
        return orderdvars;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "" + sum + " " + vars;
    }
}
