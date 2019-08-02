package utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import objects.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CnfEncoder {
    private static final Set<String> ALL_NUMBERS = ImmutableSet.of("1", "2", "3", "4");
    private static int partitionCount = 1;

    public static void encodeCnf(List<ClueSum> board, int totalCellsInGame) throws IOException {
        Set<Set<CnfVar>> finalClous = new HashSet<>();
        for (ClueSum clue : board) {
            exactlyOnePerCellConstrains(clue).forEach(finalClous::addAll);
            finalClous.addAll(correctSum(clue));
        }
        finalClous.addAll(uniquePerClue(board, totalCellsInGame));

//        FileWriter fileWriter = new FileWriter("cnfToSolve");
//        PrintWriter printWriter = new PrintWriter(fileWriter);
//        finalClous.stream()
//        printWriter.close();

    }

    public static List<Set<Set<CnfVar>>> exactlyOnePerCellConstrains(ClueSum clue) {
        return clue.getVars().stream().
                map(CnfEncoder::onePerCellConstrain).collect(Collectors.toList());
    }

    private static Set<Set<CnfVar>> onePerCellConstrain(CellCnfVar var) {
        Set<Set<CnfVar>> constraint = new HashSet<>();
        Set<CnfVar> vars = IntStream.rangeClosed(1, var.getMaxVal()).mapToObj(i -> new CellCnfVar(var.getValue() + i)).collect(Collectors.toSet());
        constraint.add(vars);
        constraint.addAll(
                Sets.combinations(vars, 2)
                        .stream()
//                        .peek(set -> set.forEach(CnfVar::setNot))
                        .collect(Collectors.toSet())
        );
        return constraint;
    }

    public static Set<Set<CnfVar>> uniquePerClue(List<ClueSum> board, int indexStart) {
        Set<Set<CnfVar>> vars = new HashSet<>();
        Set<CnfVar> temp;
        int indicatorsPrefix = indexStart;
        for (ClueSum clue : board) {
            for (int i = 1; i <= 4; i++) {
                int finalI = i;
                int finalIndicatorsPrefix = indicatorsPrefix; //we need those declarations because of lambda issue
                temp = clue.getVars().stream().map(v -> new CellCnfVar("" + v + finalI)).collect(Collectors.toSet());
                temp.forEach(t -> vars.add(ImmutableSet.of(new RowCollContainsCnfVar("" + finalIndicatorsPrefix + finalI), new CellCnfVar(t.getValue(), true))));
                temp.add(new RowCollContainsCnfVar("" + indicatorsPrefix + i));
                vars.add(temp);
                indicatorsPrefix++;
            }
        }
        return vars;
    }

    /**
     * creates a sum constrain
     *
     * @param clue - the clue containing all the cells and the sum
     * @return sum constraint for specific clue
     **/
    public static Set<Set<CnfVar>> correctSum(ClueSum clue) {
        int partitions = partitionCount;
        Set<Set<CnfVar>> vars = new HashSet<>();
        List<Set<String>> possibleSumsPartitions = Sets.combinations(IntStream.rangeClosed(1, clue.getSum())
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

        possibleSumsPartitions.forEach(parttion -> addSumsConstraintToContainer(vars,
                parttion,
                clue.getClueNumber()
        ));
        vars.add(IntStream
                .range(0, possibleSumsPartitions.size())
                .mapToObj(idx -> new PartitionCnfVar("" + (partitions + idx)))
                .collect(Collectors.toSet()));
        return vars;
    }

    //for every partition
    private static void addSumsConstraintToContainer(Set<Set<CnfVar>> container,
                                                     Set<String> partitionNumbers,
                                                     int clueContainsPrefix) {
        Set<CnfVar> first = Sets.difference(ALL_NUMBERS, partitionNumbers).stream()
                .map(s -> new RowCollContainsCnfVar(true,"" + clueContainsPrefix + s))
                .collect(Collectors.toSet());
        first.addAll(partitionNumbers.stream().map(s -> new RowCollContainsCnfVar(true,"" + clueContainsPrefix + s)).collect(Collectors.toSet()));
        container.addAll(Sets.cartesianProduct(ImmutableSet.of(new PartitionCnfVar(""+partitionCount)), first).stream()
                .map(HashSet::new)
                .collect(Collectors.toSet()));
        first.add(new PartitionCnfVar(""+partitionCount));
        container.add(first);
        partitionCount+=1;
    }

    public static String sumConstraint(ClueSum clue) {
        //(3,2), (4,1) {13, 14}, {22, 21}, {-13,-21}, {-14,-22}, {-24, -12) {-24, -13}
        //(13, 14) (24,
        // 3+2 2+3
        // 1+4 4+1
        List<Set<String>> possibleSums = Sets.combinations(IntStream.rangeClosed(1, clue.getSum())
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
                .collect(Collectors.toList());
        Set<String> allVars = possibleSums.stream().reduce(new HashSet<>(), Sets::union);
        for (CnfVar var : clue.getVars()) {

        }
        return "";
    }


    private static class SpacedStringBuilder {
        private boolean isNewLine = true;
        private StringBuilder builder = new StringBuilder();

        StringBuilder append(String s) {
            if (!isNewLine) {
                builder.append(" ");
            }
            isNewLine = false;
            return builder.append(s);
        }

        StringBuilder newLine() {
            isNewLine = true;
            return builder.append("\n");
        }
    }

}
