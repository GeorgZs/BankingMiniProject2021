package crushers.services.staff;

import com.sun.net.httpserver.*;

import crushers.models.users.Clerk;
import crushers.models.users.Manager;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

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
    public void addEndpoints(HttpServer server) throws Exception {
        super.addEndpoints(server); // add the prewiring
        server.createContext(basePath + "/@me", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getLoggedIn(exchange);
                        break;

                    default:
                        throw new MethodNotAllowedException();
                }
            }
            catch (HttpException ex) {
                sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
            }
            catch (Exception ex) {
                sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
                ex.printStackTrace();
            }
        });

        server.createContext(basePath + "/@bank", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getClerksOfBank(exchange);
                        break;

                    default:
                        throw new MethodNotAllowedException();
                }
            }
            catch (HttpException ex) {
                sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
            }
            catch (Exception ex) {
                sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
                ex.printStackTrace();
            }
        });
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
        final Manager loggedInManager = Authenticator.instance.authManager(exchange);
        final Clerk requestData = getJsonBodyData(exchange, Clerk.class);
        final Clerk responseData = staffService.create(loggedInManager.getWorksAt(), requestData);
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void put(HttpExchange exchange, int id) throws Exception{
        final Clerk loggedInClerk = Authenticator.instance.authClerk(exchange);
        final Clerk responseData = staffService.updateClerk(id, loggedInClerk);
        sendJsonResponse(exchange, responseData);
    }

    private void getLoggedIn(HttpExchange exchange) throws Exception {
        final Clerk loggedInClerk = Authenticator.instance.authClerk(exchange);
        final Clerk responseData = staffService.getLoggedIn(loggedInClerk);
        sendJsonResponse(exchange, responseData);
    }

    protected void getClerksOfBank(HttpExchange exchange) throws Exception{
        final Manager manager = Authenticator.instance.authManager(exchange);
        final Collection<Clerk> responseData = staffService.getClerksOfBank(manager.getWorksAt());
        sendJsonResponse(exchange, responseData);
    }
}
