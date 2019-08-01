package utils;

import objects.ClueSum;
import objects.CnfVar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KakuroParser {
    private static final HashMap<Integer, CnfVar> indexToCnf = new HashMap<>();

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
        ClueSum block = new ClueSum(val);
        while (tokenizer.hasMoreTokens()) {
            int index = Integer.parseInt(tokenizer.nextToken());
            CnfVar temp = indexToCnf.computeIfAbsent(index, CnfVar::new);
            temp.updateMax(val);
            block.addCnfVar(temp);
        }
        return block;
    }
}
