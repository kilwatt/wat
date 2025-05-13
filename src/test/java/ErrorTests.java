import com.kilowatt.Errors.WattParseError;
import com.kilowatt.Errors.WattRuntimeError;
import com.kilowatt.WattVM.VmAddress;

/*
Тест ошибок
 */
public class ErrorTests implements WattTest {
    public void errorTest0() {
        new WattRuntimeError(new VmAddress("bake.wt", -1, -1, "text"), "test error", "did you forget something?").panic();
    }

    public void errorTest1() {
        new WattParseError(new VmAddress("bake.wt", -1, -1, "text"), "here", "did you forget something?").panic();
    }

    @Override
    public void run() {
        errorTest0();
        errorTest1();
    }
}
