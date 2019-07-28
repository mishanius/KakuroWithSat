package utils;

import objects.ClueSum;

import java.nio.file.Path;
import java.util.List;

public class KakuroParser {
    public static List<ClueSum> parse(Path filePath){
        return null;
    }

    private static ClueSum parseLine(String line){
        String[] splited = line.split("\\s+");

        return new ClueSum()
    }
}
