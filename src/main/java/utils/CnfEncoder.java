package utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import objects.ClueSum;
import objects.CnfVar;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utils.KakuroParser.MAX_DIGIT;

/**
 * we work with the following clauses
 * "cell i contains j":  1ij s.t i is cell index and j is value, i is unbounded and 0<j<=MAX_DIGIT
 * "row/col of clue i contains j": 2ij s.t i is the clue number (unbounded) and j is the number (0<j<=MAX_DIGIT)
 * "partition number j": 3j s.t j is the partition index taken from the global counter partitionCount
 **/
public class CnfEncoder {
    private static int partitionCount = 1;
    private static final String CELL_VARIABLE_PREFIX = "1";
    private static final String CLUE_CELLS_CONTAINS_PREFIX = "2";
    private static final String PARSITION_PREFIX = "3";

    public static void encodeCnfTofile(List<ClueSum> board, int cellsCount) throws IOException {
        Set<Set<String>> finalClous = new HashSet<>();
        for (ClueSum clue : board) {
            exactlyOnePerCellConstrains(clue).forEach(finalClous::addAll);
            finalClous.addAll(correctSum(clue));
        }
        finalClous.addAll(clueContainsI(board));
        int varsCount = board.size() * MAX_DIGIT + partitionCount - 1 + cellsCount * MAX_DIGIT;
        try (PrettyPrinter printer = new PrettyPrinter("cnfToSolve", varsCount, finalClous.size())) {
            finalClous.forEach(printer::prettyPrint);
        }
    }

    /**
     * creates a clause which represents "cell number i gexactly one value
     * example: for cell i -
     * (1i1,1i2,1i3,1i4...),(-1i1,-1i2),(-1i1,-1i3)...(-1i1,-1i9)...(-1i8,-1i9)
     *
     * @param clue - a {@link ClueSum}
     * @return list of {{(1i1,1i2,1i3,1i4...), (-1i1,-1i2),(-1i1,-1i3)...(-1i1,-1i9)...},{...}}
     * for each cell that connected to the clue
     **/
    public static List<Set<Set<String>>> exactlyOnePerCellConstrains(ClueSum clue) {
        return clue.getVars().stream().
                map(CnfEncoder::onePerCellConstrain).collect(Collectors.toList());
    }

    private static Set<Set<String>> onePerCellConstrain(CnfVar var) {
        Set<Set<String>> constraint = new HashSet<>();
        Set<String> vars = IntStream.rangeClosed(1, MAX_DIGIT).mapToObj(j -> CELL_VARIABLE_PREFIX + var + j)
                .collect(Collectors.toSet());
        constraint.add(vars);
        constraint.addAll(
                Sets.combinations(vars, 2).stream()
                        .map(set -> set.stream().map(v -> "-" + v)
                                .collect(Collectors.toSet()))
                        .collect(Collectors.toSet())
        );
        return constraint;
    }


    /**
     * generates the following claus: "row/col of clue number i contains j"
     * example- clue i contains j, clue got vars x,y,z:
     * (-2ij, 1xj, 1yj, 1zj),(2ij,-1xj), (2ij,-1yj), (2ij, -1zj)
     *
     * @param board: list of all the clues in the game
     * @return vars: Set<Set<String>> of cnf variables
     **/
    public static Set<Set<String>> clueContainsI(List<ClueSum> board) {
        Set<Set<String>> vars = new HashSet<>();
        Set<String> temp;
        for (ClueSum clue : board) {
            for (int i = 1; i <= MAX_DIGIT; i++) {
                int finalI = i;
                temp = clue.getVars().stream().map(v -> CELL_VARIABLE_PREFIX + v + finalI)
                        .collect(Collectors.toSet());
                temp.forEach(t -> vars.add(
                        ImmutableSet.of(CLUE_CELLS_CONTAINS_PREFIX + clue.getClueNumber() + finalI, "-" + t))
                );
                temp.add("-" + CLUE_CELLS_CONTAINS_PREFIX + clue.getClueNumber() + finalI);
                vars.add(temp);
            }
        }
        return vars;
    }

    /**
     *
     * generates clauses for all possible partitions of {1,2,3,4,5,6,7,8,9} in the size of {@link ClueSum}.getVars()
     * example for clue sum=5 that have 2 cells we will generate the following:
     * p1 = (2,3), p2=(1,4)
     * the final clause is :
     * (p1, -2, -3, 1, 4, 5, 6, 7, 8, 9), (-p1, 2), (-p1,3),(-p1,-1),(-p1,-4),(-p1,-5),(-p1,-6)....
     * (p2, -4, -1, 3, 2, 5, 6, 7, 8, 9), (-p2, 4), (-p2,1),(-p1,-3),(-p1,-2),(-p1,-5),(-p1,-6)....
     * @implNote uses addSumsConstraintToContainer as a helper function
     * @param clue the clue that for his sum we want to find the partitions
     * @return set of clauses
     *
     * **/
    public static Set<Set<String>> correctSum(ClueSum clue) {
        Set<Set<String>> vars = new HashSet<>();
        int startPartition = partitionCount;
        List<Set<String>> possibleSumsPartitions = Sets.combinations(IntStream.rangeClosed(1, MAX_DIGIT)
                .boxed()//get all subsets in size clue.getVars().size()
                .collect(Collectors.toSet()), clue.getVars().size()
        )
                .stream()
                .filter(set -> set.stream()//filter out wrong sums
                        .reduce(0, Integer::sum) == clue.getSum())
                .map(//convert to strings
                        set -> set.stream().map(x -> "" + x)
                                .collect(Collectors.toSet())
                )
                .collect(Collectors.toList());//we recive a list of all partitions with the provided sum
        IntStream.rangeClosed(1, possibleSumsPartitions.size()).forEach(idx -> addSumsConstraintToContainer(vars,
                possibleSumsPartitions.get(idx - 1),
                clue.getClueNumber()
        ));
        vars.add(IntStream
                .range(0, possibleSumsPartitions.size())
                .mapToObj(idx -> PARSITION_PREFIX + (startPartition + idx))
                .collect(Collectors.toSet()));
        return vars;
    }

    private static void addSumsConstraintToContainer(Set<Set<String>> container,
                                                     Set<String> partitionNumbers,
                                                     int clueNumber
    ) {
        Set<String> allNumbers = IntStream.rangeClosed(1, MAX_DIGIT)
                .mapToObj(Integer::toString)
                .collect(Collectors.toSet());
        Set<String> first = Sets.difference(allNumbers, partitionNumbers).stream()
                .map(s -> CLUE_CELLS_CONTAINS_PREFIX + clueNumber + s)
                .collect(Collectors.toSet());
        first.addAll(partitionNumbers.stream().map(s -> "-"+CLUE_CELLS_CONTAINS_PREFIX + clueNumber + s)
                .collect(Collectors.toSet()));
        container.addAll(Sets.cartesianProduct(ImmutableSet.of("-"+PARSITION_PREFIX + partitionCount),
                partitionNumbers
                        .stream()
                        .map(s -> CLUE_CELLS_CONTAINS_PREFIX + clueNumber + s)
                        .collect(Collectors.toSet()))
                .stream()
                .map(HashSet::new)
                .collect(Collectors.toSet()));
        first.add(PARSITION_PREFIX + partitionCount);
        container.add(first);
        partitionCount += 1;
    }

    public static class PrettyPrinter implements Closeable {
        /**
         * helper class to print normally to the cnf file
         * **/

        PrintWriter printWriter;

        public PrettyPrinter(String fileName, int numberOfVars, int numberOfClauses) throws IOException {
            FileWriter fileWriter = new FileWriter(fileName);
            printWriter = new PrintWriter(fileWriter);
            printWriter.println("p cnf " + numberOfVars + " " + numberOfClauses);
        }

        public void prettyPrint(Set<String> set) {
            printWriter.println(String.join(" ", set) + " 0");
        }

        @Override
        public void close() throws IOException {
            printWriter.close();
        }
    }
}
