import LinearProgrammingProblem.Problem;
import LinearProgrammingProblem.ProblemResult;
import Methods.SimplexMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemsTesterTool {
    List<Problem> problems;
    List<ProblemResult> results;

    public static final String divider = "\n========";

    private ProblemsTesterTool(List<String>... problemsString){
        List<List<String>> problemsList = Arrays.asList(problemsString);
        problems = problemsList.stream()
                .map(problemList -> Problem.createProblem(problemList))
                .collect(Collectors.toList());
    }

    public static ProblemsTesterTool createProblemTesterTool(List<String>... problemsString){
        return new ProblemsTesterTool(problemsString);
    }

    public void displayProblem(){
        System.out.println("Problems:" + divider);
        for(int i = 0; i < problems.size(); i++){
            System.out.println(i + ")\n" + problems.get(i)+ "\n");
        }
    }

    public void displayResult(){
        System.out.println("Problems:" + divider);
        for(int i = 0; i < results.size(); i++){
            System.out.println(i + ")\n" + results.get(i)+ "\n");
        }
    }

    public void calculateResult(){
        SimplexMethod sm = new SimplexMethod();
        results = problems.stream()
                .map(problem -> sm.solve(problem))
                .collect(Collectors.toList());
    }


}
