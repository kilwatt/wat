import com.kilowatt.Errors.WattParsingError;
import com.kilowatt.Errors.WattRuntimeError;

/*
Тест ошибок
 */
public class ErrorTests implements WattTest {
    public void errorTest0() {
        new WattRuntimeError(1, "bake.w", "test error", "did you forget something?").print();
    }

    public void errorTest1() {
        new WattParsingError(1, "bake.w", "here", "did you forget something?").print();
    }

    @Override
    public void run() {
        errorTest0();
        errorTest1();
    }
}
