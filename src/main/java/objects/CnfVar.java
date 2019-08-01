package objects;

public class CnfVar {
    int index;
    int maxVal = 0;

    public CnfVar(int index) {
        this.index = index;
    }

    public CnfVar(String index) {
        this.index = Integer.parseInt(index);
    }

    @Override
    public String toString() {
        return ""+index;
    }

    /**
     * for a cnf var that representing some cell
     * the max val is min({clue.sum | this in clue.vars})
     * **/
    public void updateMax(int val){
        if(maxVal==0){
            maxVal=val;
        }
        else if(maxVal>val){
            maxVal=val;
        }
    }



    public int getMaxVal(){
        return maxVal;
    }
}
