import java.util.*;
import java.util.stream.DoubleStream;

import CalculatingTable.Row;
import LinearProgrammingProblem.Problem;
import Methods.SimplexMethod;

public class Main {


    public static void main(String[] args) {

        String s00 = "-20 -16 -12";
        String s01 = "MORE MORE MORE";
        String s02 = "1 0 0 LESS 4";
        String s03 = "2 1 1 LESS 10";
        String s04 = "2 2 1 LESS 16";

        List<String> data0 = new ArrayList<>(Arrays.asList(s00, s01, s02, s03, s04));
        Problem problem0 = Problem.createProblem(data0);
        System.out.println(problem0+"\n");
        SimplexMethod sm0 = new SimplexMethod();
        System.out.println(sm0.solve(problem0)+"\n");


        String s10 = "3 -2";
        String s11 = "MORE MORE";
        String s12 = "2 1 LESS 14";
        String s13 = "-3 2 LESS 9";
        String s14 = "3 4 LESS 27";

        List<String> data1 = new ArrayList<>(Arrays.asList(s10, s11, s12, s13, s14));
        Problem problem1 = Problem.createProblem(data1);
        System.out.println(problem1+"\n");
        SimplexMethod sm1 = new SimplexMethod();
        System.out.println(sm1.solve(problem1)+"\n");


//        String s20 = "-16 -12 -20";
//        String s21 = "MORE MORE MORE";
//        String s22 = "0 0 -1 LESS 4";
//        String s23 = "1 1 0 LESS 2";
//        String s24 = "2 1 -2 LESS 8";
//
//        List<String> data2 = new ArrayList<>(Arrays.asList(s20, s21, s22, s23, s24));
//        Problem problem2 = Problem.createProblem(data2);
//        System.out.println(problem2+"\n");
//        SimplexMethod sm2 = new SimplexMethod();
//        System.out.println(sm2.solve(problem2)+"\n");

    }
}
