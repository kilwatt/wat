import java.util.ArrayList;
import java.util.List;

/*
Тесты
 */
public class WattTests {
    public static void main(String[] args) {
        // тесты ошибок
        List<WattTest> wattTestList = new ArrayList<>();
        wattTestList.add(new ErrorTests());
        // запускаем
        for (WattTest test : wattTestList) {
            test.run();
        }
    }
}
