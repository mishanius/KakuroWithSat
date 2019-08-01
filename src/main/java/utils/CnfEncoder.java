package utils;
import com.google.common.collect.Sets;
import objects.ClueSum;
import objects.CnfVar;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CnfEncoder {
    public static void encodeCnf(List<ClueSum> board) {

    }

    public static List<Set<Set<String>>> exactlyOnePerCellConstrains(ClueSum clue) {
        return clue.getVars().stream().
                map(CnfEncoder::onePerCellConstrain).collect(Collectors.toList());
    }

    private static Set<Set<String>> onePerCellConstrain(CnfVar var) {
        Set<Set<String>> constraint = new HashSet<>();
        Set<String> vars = IntStream.rangeClosed(1, var.getMaxVal()).mapToObj(i -> ""+var+i).collect(Collectors.toSet());
        constraint.add(vars);
        constraint.addAll(
                Sets.combinations(vars,2).stream()
                        .map(set->set.stream().map(v->"-"+v)
                                .collect(Collectors.toSet()))
                        .collect(Collectors.toSet())
        );
        return constraint;
    }

    public static String sumConstraint(ClueSum clue) {
        //(3,2), (4,1) {13, 14}, {22, 21}, {-13,-21}, {-14,-22}, {-24, -12) {-24, -13}
        //(13, 14) (24,
        // 3+2 2+3
        // 1+4 4+1
        List<Set<String>> possibleSums = Sets.combinations(IntStream.rangeClosed(1,clue.getSum())
                .boxed()//get all subsets in size clue.getVars().size()
                .collect(Collectors.toSet()), clue.getVars().size()
        )
                .stream()
                .filter(set->set.stream()//filter out wrong sums
                        .reduce(0, Integer::sum)==clue.getSum())
                .map(//convert to strings
                        set->set.stream().map(x->""+x)
                                .collect(Collectors.toSet())
                )
                .collect(Collectors.toList());
        Set<String> allVars = possibleSums.stream().reduce(new HashSet<>(), Sets::union);
        for(CnfVar var : clue.getVars()){
            
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
