package Processes;

public class Variable {
    String varValue;
    Integer varAddress;

    public Variable(String varValue) {
        this.varValue = varValue;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarAddress(Integer varAddress) {
        this.varAddress = varAddress;
    }

    public Integer getVarAddress() {
        return varAddress;
    }
}
