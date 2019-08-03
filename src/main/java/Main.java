import objects.ClueSum;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import utils.CnfEncoder;
import utils.KakuroParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final String CNF_FILE_NAME = "cnfToSolve";

    public static void main(String[] args) throws IOException {
        List<ClueSum> board = KakuroParser.parse(args[0]);
        CnfEncoder.encodeCnfTofile(board, KakuroParser.getVarsCount());
        ISolver solver = SolverFactory.newGreedySolver();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out, true);
        // CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance("cnfToSolve");
            if (problem.isSatisfiable()) {
                KakuroParser.parseSolutionToFile(board, problem, "kakuroSolution_michael_kerem.txt");
                reader.decode(problem.model(), out);
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        } finally {
            if (!(args.length > 1 && args[1].equals("verbose"))) {
                try {
                    Files.deleteIfExists(Paths.get(CNF_FILE_NAME));
                } catch (NoSuchFileException e) {
                    System.out.println("No such file/directory exists");
                } catch (DirectoryNotEmptyException e) {
                    System.out.println("Directory is not empty.");
                } catch (IOException e) {
                    System.out.println("Invalid permissions.");
                }
            }
        }
    }
}
