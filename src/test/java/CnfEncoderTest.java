import com.google.common.collect.ImmutableList;
import objects.CellCnfVar;
import objects.ClueSum;
import objects.CnfVar;
import org.testng.annotations.Test;
import utils.CnfEncoder;
import java.util.List;

import static utils.KakuroParser.parse;

public class CnfEncoderTest {
    @Test
    public void onlyOnePerCellCnf(){
        ClueSum clue = new ClueSum(5);
        clue.addCnfVar(new CellCnfVar("1"))
                .addCnfVar(new CellCnfVar("2"));
        System.out.println(CnfEncoder.exactlyOnePerCellConstrains(clue));
    }

    @Test
    public void uniquePerClue(){
        ClueSum clue = new ClueSum(5);
        clue.addCnfVar(new CellCnfVar("1"))
                .addCnfVar(new CellCnfVar("2"));
        System.out.println(CnfEncoder.uniquePerClue(ImmutableList.of(clue), 3));
    }

    @Test
    public void sumConstraintTest(){
        ClueSum clue = new ClueSum(5);
        clue.addCnfVar(new CellCnfVar("1"))
                .addCnfVar(new CellCnfVar("2"));
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
