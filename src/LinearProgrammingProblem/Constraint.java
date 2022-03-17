package LinearProgrammingProblem;

import java.util.ArrayList;
import java.util.List;

public class Constraint {
    private List<Double> varComponents;
    private List<Double> slackVarComponents;
    private List<Double> surplusVarComponents;

    private Sign sign;
    private double RHS;

    private static final Sign[] constraintSigns = new Sign[]{Sign.MORE, Sign.LESS, Sign.EQUAL};


    private Constraint(){
        varComponents = new ArrayList<>();
        slackVarComponents = new ArrayList<>();
        surplusVarComponents = new ArrayList<>();
    }

    public Constraint(List<String> constraintString)
            throws IllegalArgumentException{

        this();
        int i;
        for(i = 0; i < constraintString.size()-2; i++){
            varComponents.add(Double.valueOf(constraintString.get(i)));
        }
        sign = Sign.getProperSign(constraintString.get(i), constraintSigns);
        RHS = Double.valueOf(constraintString.get(i+1));
    }


    public void addSlackComponent(){
        slackVarComponents.add(1.);
        sign = Sign.EQUAL;
    }
    public void addSlackEmpty(){
        slackVarComponents.add(0.);
    }

    public void addSurplusComponent(){
        surplusVarComponents.add(-1.);
        sign = Sign.EQUAL;
    }
    public void addSurplusEmpty(){
        surplusVarComponents.add(0.);
    }

    public void makeRHSPositive(){
        if(RHS < 0){
            RHS=-RHS;
            sign = Sign.getOpposite(sign);
            for(int i = 0; i < varComponents.size(); i++){
                varComponents.set(i, -varComponents.get(i));
            }
            for(int i = 0; i < slackVarComponents.size(); i++){
                slackVarComponents.set(i, -slackVarComponents.get(i));
            }
            for(int i = 0; i < surplusVarComponents.size(); i++){
                surplusVarComponents.set(i, -surplusVarComponents.get(i));
            }
        }
    }

    public int getBasicIndexFromSlackVars(){
        int basicIndex = -varComponents.size()-1;
        for(int i = 0; i < slackVarComponents.size(); i++){
            if(slackVarComponents.get(i) == 1.){
                basicIndex = i;
                break;
            }
        }
        return basicIndex + varComponents.size();
    }

    public List<Double> getVarComponents() {
        return varComponents;
    }

    public List<Double> getSlackVarComponents() {
        return slackVarComponents;
    }

    public List<Double> getSurplusVarComponents() {
        return surplusVarComponents;
    }

    public Sign getSign() {
        return sign;
    }

    public double getRHS() {
        return RHS;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        if(!slackVarComponents.isEmpty()){builder.append(slackVarComponents + " ");}
        if(!surplusVarComponents.isEmpty()){builder.append(surplusVarComponents+ " ");}

        return varComponents + " " + builder + sign + " " + RHS;
    }
}


