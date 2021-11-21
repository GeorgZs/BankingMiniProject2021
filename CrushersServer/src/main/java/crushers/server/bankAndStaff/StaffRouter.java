package crushers.server.bankAndStaff;

import crushers.models.users.Clerk;
import crushers.server.Router;

public class StaffRouter extends Router<Clerk>{
    public StaffRouter(String basePath) {
        super("/staff");
    }
}
