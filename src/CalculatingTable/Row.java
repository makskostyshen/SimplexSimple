package CalculatingTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import LinearProgrammingProblem.Constraint;


public class Row {
    private List<Double> components;
    private double RHS;
    private int basicIndex;

    public Row(Constraint constraint, int basicIndex){
        components = new ArrayList<>();
        this.basicIndex = basicIndex;

        components.addAll(constraint.getVarComponents());
        components.addAll(constraint.getSlackVarComponents());
        components.addAll(constraint.getSurplusVarComponents());

        RHS = constraint.getRHS();
    }

    public Row(List<Double> components){
        this.components = new ArrayList<>(components);
    }

    public void pivot(int index){
        double divider = get(index);
        RHS/=divider;

        components = components.stream()
                .map(value -> value/divider)
                .collect(Collectors.toList());
    }

    public void elementaryRowReduce(Row other, int index){
        double multiplier = -get(index)/other.get(index);
        linearAdd(other, 1., multiplier);
    }

    public void linearAdd(Row other, double multiplierThis, double multiplierOther){
        RHS = RHS * multiplierThis + other.RHS * multiplierOther;
        for(int i = 0; i < components.size(); i++){
            set(i, multiplierThis * get(i) + multiplierOther * other.get(i));
        }
    }

    public List<Double> getComponents() {
        return components;
    }

    public double getRHS() {
        return RHS;
    }

    public int getBasicIndex() {
        return basicIndex;
    }

    public void setBasicIndex(int basicIndex) {
        this.basicIndex = basicIndex;
    }

    public double get(int compIndex){
        return components.get(compIndex);
    }

    public void set(int index, double value){
        components.set(index, value);
    }

    @Override
    public String toString() {
        return basicIndex + " " + components + " " + RHS;
    }
}
