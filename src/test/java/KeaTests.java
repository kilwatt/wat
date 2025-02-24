import java.util.ArrayList;
import java.util.List;

/*
Тесты
 */
public class KeaTests {
    public static void main(String[] args) {
        // тесты ошибок
        List<KeaTest> keaTestList = new ArrayList<>();
        keaTestList.add(new ErrorTests());
        // запускаем
        for (KeaTest test : keaTestList) {
            test.run();
        }
    }
}
