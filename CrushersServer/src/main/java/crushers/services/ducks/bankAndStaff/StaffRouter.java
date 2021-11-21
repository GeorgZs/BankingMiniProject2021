package crushers.services.ducks.bankAndStaff;

import com.sun.net.httpserver.HttpExchange;
import crushers.models.users.Clerk;
import crushers.server.Router;

import java.util.Collection;

public class StaffRouter extends Router<Clerk>{

    private StaffService staffService;

    public StaffRouter(StaffService staffService) {
        super("/staff");
        this.staffService = staffService;
    }

    @Override
    protected void get(HttpExchange exchange, int id)throws Exception{
        final Clerk responseData = staffService.get(id);
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void getAll(HttpExchange exchange)throws Exception{
        final Collection<Clerk> responseData = staffService.getAll();
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void post(HttpExchange exchange)throws Exception{
        final Clerk requestData = getJsonBodyData(exchange, Clerk.class);
        final Clerk responseData = staffService.create(requestData);
        sendJsonResponse(exchange, responseData);
    }
}
