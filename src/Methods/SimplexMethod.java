package Methods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import CalculatingTable.Tableau;
import LinearProgrammingProblem.Problem;
import LinearProgrammingProblem.ProblemResult;

public class SimplexMethod {

    public ProblemResult solve(Problem problem){
        ProblemResult result = new ProblemResult();

        try{
            turnAndCheckAppropriateForm(problem);
            result = simplexMethodSolve(problem);
        }
        catch (SimplexMethodException sme){
            System.out.println("Error:\n" + sme);
        }
        catch (Exception e){
            System.out.println("Error:\n" + e);
            System.out.println("Not expected problem detected");
            e.printStackTrace(System.out);
        }
        finally{
            return result;
        }
    }

    private void turnAndCheckAppropriateForm(Problem problem)
            throws SimplexMethodException{

        problem.toStandartForm();
        if(problem.isEmpty()){throw new EmptyProblemException();}
        if(!problem.hasSlackIdentityMatrix()){throw new NoSuchSolutionIsDoneYet();}
    }

    private ProblemResult simplexMethodSolve(Problem problem)
            throws NoFiniteOptimumException{

        Tableau tableau = new Tableau(problem);
        //System.out.println(tableau + "\n");

        while(isTargetNotAchieved(tableau)){
            nextIteration(tableau);
        }
        return getResult(problem, tableau);
    }

    private ProblemResult getResult(Problem problem, Tableau tableau){
        int limit = problem.getVarsNum();
        List<Double> minFuncComponents = problem.getMinFuncComponents().subList(0, limit);
        List<Double> resultComponentsFull = getResultsComponents(tableau).subList(0, limit);
        double minValue = -tableau.getSimplexDiffComponents().getRHS();

        return new ProblemResult(minFuncComponents, resultComponentsFull, minValue);
    }

    private void nextIteration(Tableau tableau) throws NoFiniteOptimumException{
        int columnIndex = getColumnIndex(tableau);
        int rowIndex = getRowIndex(tableau, columnIndex);
        pivot(tableau, columnIndex, rowIndex);
        changeBasicIndexes(tableau, columnIndex, rowIndex);
        //System.out.println(tableau + "\n");
    }

    private boolean isTargetNotAchieved(Tableau tableau){
        return tableau.getSimplexDiffComponents()
                .getComponents().stream()
                .min(Double::compareTo)
                .orElse(0.0) < 0;
    }

    private List<Double> getResultsComponents(Tableau tableau) {
        List<Double> resultComponents =
                new ArrayList<>(Collections.nCopies(tableau.getCompNum(), 0.));

        for (int j = 0; j < tableau.getRowNum(); j++){
            int basicIndex = tableau.get(j).getBasicIndex();
            double basicRHS = tableau.get(j).getRHS();
            resultComponents.set(basicIndex, basicRHS);
        }
        return resultComponents;
    }

    private void pivot(Tableau tableau, int columnIndex, int rowIndex) {
        tableau.get(rowIndex).pivot(columnIndex);
        for(int i = 0; i < tableau.getRowNum(); i++){
            if(i != rowIndex){
                tableau.get(i).elementaryRowReduce(tableau.get(rowIndex), columnIndex);
            }
        }
        tableau.getSimplexDiffComponents().elementaryRowReduce(tableau.get(rowIndex), columnIndex);
    }

    private int getColumnIndex(Tableau tableau){
        double minDiff = tableau.getSimplexDiffComponents().get(0);
        int minDiffIndex = 0;

        for (int i = 1; i < tableau.getCompNum(); i++){
            double thisDiff = tableau.getSimplexDiffComponents().get(i);
            if(thisDiff < minDiff){
                minDiff = thisDiff;
                minDiffIndex = i;
            }
        }
        return minDiffIndex;
    }

    private int getRowIndex(Tableau tableau, int columnIndex) throws NoFiniteOptimumException{
        List<Integer> positiveCompIndexes = getPositiveCompIndexes(tableau, columnIndex);

        if(positiveCompIndexes.size()==0) throw new NoFiniteOptimumException();

        int minDivIndex = positiveCompIndexes.get(0);

        for(int thisIndex: positiveCompIndexes){
            if(isNewDivAppropriate(tableau, minDivIndex, thisIndex, columnIndex)){
                minDivIndex = thisIndex;
            }
        }

        return minDivIndex;
    }

    private List<Integer> getPositiveCompIndexes(Tableau tableau, int columnIndex){
        List<Integer> positiveComponents = new ArrayList<>();
        for (int j = 0; j < tableau.getRowNum(); j++){
            if(tableau.get(j).get(columnIndex) > 0){
                positiveComponents.add(j);
            }
        }
        return positiveComponents;
    }

    private boolean isNewDivAppropriate(Tableau tableau, int minDivIndex, int thisIndex, int columnIndex){
        double minDivComp = tableau.get(minDivIndex).get(columnIndex);
        double thisComp = tableau.get(thisIndex).get(columnIndex);

        double minDiv = tableau.get(minDivIndex).getRHS()/minDivComp;
        double thisDiv = tableau.get(thisIndex).getRHS()/thisComp;

        if(thisDiv < minDiv){
            return true;
        }
        else if(minDiv < thisDiv){
            return false;
        }
        else if(minDivComp < thisComp){
            return true;
        }
        else return false;
    }

    private void changeBasicIndexes(Tableau tableau, int columnIndex, int rowIndex){
        tableau.get(rowIndex).setBasicIndex(columnIndex);
    }
}

class SimplexMethodException extends IllegalArgumentException{
    SimplexMethodException(String msg){
        super(msg);
    }
    SimplexMethodException(){}
}
class NoSuchSolutionIsDoneYet extends SimplexMethodException{
    NoSuchSolutionIsDoneYet(String msg){
        super(msg);
    }
    NoSuchSolutionIsDoneYet(){}
}
class NoFiniteOptimumException extends SimplexMethodException{
    NoFiniteOptimumException(String msg){
        super(msg);
    }
    NoFiniteOptimumException(){}
}
class EmptyProblemException extends  SimplexMethodException{
    EmptyProblemException(String msg){
        super(msg);
    }
    EmptyProblemException(){}
}
