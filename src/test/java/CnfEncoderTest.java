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
        clue.addCnfVar(new CnfVar(1))
                .addCnfVar(new CnfVar(2));
        System.out.println(CnfEncoder.exactlyOnePerCellConstrains(clue));
    }

    @Test
    public void parserTest(){
        List<ClueSum> res = parse("example1.txt");
        for(ClueSum clue : res){
            System.out.println(clue);
        }
    }
}
