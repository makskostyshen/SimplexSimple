import LinearProgrammingProblem.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String s0 = "-20 -16 12";
        String s1 = "MORE MORE MORE";
        String s2 = "1 0 0 LESS 4";
        String s3 = "2 1 1 LESS 10";
        String s4 = "2 2 1 LESS 16";

        List<String> stringy = new ArrayList<>(Arrays.asList(s0, s1, s2, s3, s4));
        Problem problem = new Problem(stringy);

        System.out.println(problem);
        SimplexMethod sm = new SimplexMethod();
        System.out.println(sm.solve(problem));


    }
}
