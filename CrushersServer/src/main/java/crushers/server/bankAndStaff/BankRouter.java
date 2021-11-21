package crushers.server.bankAndStaff;

import crushers.server.Router;
import java.lang.reflect.Type;

public class BankRouter extends Router<Type> {
    public BankRouter(String basePath) {
        super("/bank");
    }
}
