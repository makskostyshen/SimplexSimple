package CalculatingTable;

import java.util.ArrayList;
import java.util.List;

import LinearProgrammingProblem.Constraint;
import LinearProgrammingProblem.Problem;


public class Tableau {
    private List<Double> minFuncComponents;
    private Row simplexDiffComponents;
    private List<Row> rows;

    private int compNum;
    private int rowNum;


    private Tableau(){
        minFuncComponents = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public Tableau(Problem problem){
        this();

        compNum = problem.getVarsNum()+problem.getSlackVarsNum()+problem.getSurplusVarsNum();
        rowNum = problem.getConsNum();

        minFuncComponents = problem.getMinFuncComponents();             //attention::shallow copy!!!!
        simplexDiffComponents = new Row(problem.getMinFuncComponents());              //here
        for(Constraint constraint : problem.getConstraints()){
            rows.add(new Row(constraint, constraint.getBasicIndexFromSlackVars()));                            //here
        }
    }

    public List<Double> getMinFuncComponents() {
        return minFuncComponents;
    }

    public Row getSimplexDiffComponents() {
        return simplexDiffComponents;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getCompNum() {
        return compNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public Row get(int rowIndex){
        return rows.get(rowIndex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(Row row: rows){
            builder.append(row + "\n");
        }

        return "minF: " + minFuncComponents + "\n" +
                builder.toString() +
                "sD: " + simplexDiffComponents;
    }
}
