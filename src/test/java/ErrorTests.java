import com.kea.Errors.KeaParsingError;
import com.kea.Errors.KeaRuntimeError;

/*
Тест ошибок
 */
public class ErrorTests implements KeaTest {
    public void errorTest0() {
        new KeaRuntimeError(1, "test.kea", "test error", "Did you forget something?").print();
    }

    public void errorTest1() {
        new KeaParsingError(1, "test.kea", "here", "Did you forget something?").print();
    }

    @Override
    public void run() {
        errorTest0();
        errorTest1();
    }
}
