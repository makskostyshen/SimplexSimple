import java.util.*;

import LinearProgrammingProblem.Problem;
import Methods.SimplexMethod;

public class Main {


    public static void main(String[] args) {

        String s0 = "-20 -16 -12";
        String s1 = "MORE MORE MORE";
        String s2 = "1 0 0 LESS 4";
        String s3 = "2 1 1 LESS 10";
        String s4 = "2 2 1 LESS 16";

        List<String> data = new ArrayList<>(Arrays.asList(s0, s1, s2, s3, s4));
        Problem problem = Problem.createProblem(data);

        System.out.println(problem+"\n");
        SimplexMethod sm = new SimplexMethod();
        System.out.println(sm.solve(problem));

    }
}
