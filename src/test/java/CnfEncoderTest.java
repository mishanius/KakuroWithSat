import com.google.common.collect.ImmutableList;
import objects.ClueSum;
import objects.CnfVar;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.testng.annotations.Test;
import utils.CnfEncoder;
import utils.KakuroParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utils.KakuroParser.parse;

public class CnfEncoderTest {
    @Test
    public void fullTest() throws IOException {
        ClueSum clue = new ClueSum(5,1);
        clue.addCnfVar(new CnfVar(1))
                .addCnfVar(new CnfVar(2));
        List<ClueSum> board = ImmutableList.of(clue);
        CnfEncoder.encodeCnfTofile(board, 2);
        ISolver solver = SolverFactory.newGreedySolver();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out,true);
        // CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance("cnfToSolve");
            if (problem.isSatisfiable()) {
                KakuroParser.parseSolutionToFile(board, problem, "testResult1.txt");
                reader.decode(problem.model(),out);
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
        }
    }

    @Test
    public void onlyOnePerCellCnf(){
        ClueSum clue = new ClueSum(5,1);
        clue.addCnfVar(new CnfVar(1))
                .addCnfVar(new CnfVar(2));
        System.out.println(CnfEncoder.exactlyOnePerCellConstrains(clue));
    }

    @Test
    public void rowColContains(){
        ClueSum clue = new ClueSum(5,1);
        clue.addCnfVar(new CnfVar(1))
                .addCnfVar(new CnfVar(2));
        System.out.println(CnfEncoder.clueContainsI(ImmutableList.of(clue)));
    }

    @Test
    public void sumConstraintTest(){
        ClueSum clue = new ClueSum(5,1);
        clue.addCnfVar(new CnfVar(1))
                .addCnfVar(new CnfVar(2));
        System.out.println(CnfEncoder.correctSum(clue));
    }

    @Test
    public void parserTest(){
        List<ClueSum> res = parse("example1.txt");
        for(ClueSum clue : res){
            System.out.println(clue);
        }
    }
}
