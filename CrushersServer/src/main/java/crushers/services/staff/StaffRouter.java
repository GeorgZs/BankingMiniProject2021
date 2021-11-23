package crushers.services.staff;

import com.sun.net.httpserver.HttpExchange;
import crushers.models.users.Clerk;
import crushers.server.Router;

import java.util.Collection;

public class StaffRouter extends Router<Clerk>{

    private StaffService staffService;

    //this constructor means that when the StaffRouter object
    //is created it sets the base path from the Router
    //super to /staff therefore setting it as an endpoint
    //for our server - routes us to the bank storage
    public StaffRouter(StaffService staffService) {
        super("/staff");
        this.staffService = staffService;
    }

    @Override
    protected void get(HttpExchange exchange, int id) throws Exception {
        final Clerk responseData = staffService.get(id);
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void getAll(HttpExchange exchange) throws Exception {
        final Collection<Clerk> responseData = staffService.getAll();
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void post(HttpExchange exchange) throws Exception {
        final Clerk requestData = getJsonBodyData(exchange, Clerk.class);
        final Clerk responseData = staffService.create(requestData);
        sendJsonResponse(exchange, responseData);
    }
}
