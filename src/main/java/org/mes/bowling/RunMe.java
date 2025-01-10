package org.mes.bowling;

import java.util.Arrays;
import java.util.List;

public class RunMe {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Michael Smith's Bowling Score Calculator Examples");
        System.out.println("-------------------------------------------------");
        System.out.println();
        System.out.println("(1) First example from the coding exercise:");
        char[] input1 = {'4', '5', 'X', '8'};
        System.out.println("    Input: " + Arrays.toString(input1));
        List<Integer> output1 = BowlingScoreCalculator.calculateScores(input1);
        System.out.println("    Output: " + output1);
        System.out.println();
        System.out.println("(2) Second example from the coding exercise:");
        char[] input2 = {'4', '5', 'X', '8', '1'};
        System.out.println("    Input: " + Arrays.toString(input2));
        List<Integer> output2 = BowlingScoreCalculator.calculateScores(input2);
        System.out.println("    Output: " + output2);
        System.out.println();
        System.out.println("(3) Perfect game:");
        char[] input3 = {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'};
        System.out.println("    Input: " + Arrays.toString(input3));
        List<Integer> output3 = BowlingScoreCalculator.calculateScores(input3);
        System.out.println("    Output: " + output3);
        System.out.println();
        System.out.println("For other examples see the unit test cases in src/test/java/org.mes.bowling/BowlingScoreCalculatorTest.java.");

    }
}
