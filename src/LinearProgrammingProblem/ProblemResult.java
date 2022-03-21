package LinearProgrammingProblem;

import java.util.List;

public class ProblemResult {
    List<Double> minFuncComponents;
    List<Double> resultComponents;
    double resultValue;


    public ProblemResult(){}
    public ProblemResult(List<Double> minFuncComponents, List<Double> resultComponents, double resultValue){
        this.minFuncComponents = minFuncComponents;
        this.resultComponents = resultComponents;
        this.resultValue = resultValue;
    }

    private boolean isEmpty(){
        return minFuncComponents == null;
    }
    @Override
    public String toString() {
        if(isEmpty()) {
            return "...empty result...";
        }
        else{
            return minFuncComponents + " - minimize function components\n"
                +  resultComponents + " - result components\n"
                +  resultValue + " - result value";
        }
    }
}
