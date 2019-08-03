package utils;

import objects.ClueSum;
import objects.CnfVar;
import org.sat4j.specs.IProblem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KakuroParser {
    public static final int MAX_DIGIT = 9;
    private static final HashMap<Integer, CnfVar> indexToCnf = new HashMap<>();
    private static int lineCount = 1;

    public static List<ClueSum> parse(String filePath) {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            return stream.map(KakuroParser::parseLine).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("can't open file");
        }
    }

    private static ClueSum parseLine(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        int val = Integer.parseInt(tokenizer.nextToken());
        ClueSum block = new ClueSum(val, lineCount);
        while (tokenizer.hasMoreTokens()) {
            int index = Integer.parseInt(tokenizer.nextToken());
            CnfVar temp = indexToCnf.computeIfAbsent(index, CnfVar::new);
            temp.updateMax(val);
            block.addCnfVar(temp);
        }
        lineCount++;
        return block;
    }

    public static int getVarsCount() {
        return indexToCnf.size();
    }

    public static void parseSolutionToFile(List<ClueSum> board, IProblem solvedProblem, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        HashSet<String> printed = new HashSet<>();
        board.stream()
                .flatMap(c -> c.getOrderdvars()
                        .stream())
                .map(v -> v.toString() + " " + getValue(v,  solvedProblem))
                .filter(s->!printed.contains(s))
                .peek(printed::add)
                .forEach(printWriter::println);
        printWriter.close();
    }

    private static int getValue(CnfVar var, IProblem problem) {
        return IntStream.rangeClosed(1, MAX_DIGIT)
                .reduce(1,
                        (curr, num) -> problem.model(Integer.parseInt("1" + var + num)) ?
                                num :
                                curr
                );
    }

}
