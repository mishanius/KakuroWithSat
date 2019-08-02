package objects;
public abstract class CnfVar {
    private boolean isNot = false;
    private String value;
    public CnfVar(String value) {
        this(false, value);
    }

    public CnfVar(boolean isNot, String value) {
        this.isNot = isNot;
        this.value = value;
    }

    abstract int getType();

    public void setNot() {
        isNot = true;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (isNot?"-":"")+getType()+value;
    }
}
