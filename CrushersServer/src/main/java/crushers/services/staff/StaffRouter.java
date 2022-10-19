package crushers.services.staff;

import com.sun.net.httpserver.*;

import crushers.common.httpExceptions.HttpException;
import crushers.common.httpExceptions.MethodNotAllowedException;
import crushers.common.models.*;
import crushers.server.Authenticator;
import crushers.server.Router;

import java.util.Collection;

public class StaffRouter extends Router {

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

    /**
     * @param exchange
     * @param id of the Staff member that is being requested
     * @return the Staff member with the specified ID number
     * @throws Exception
     */
    @Override
    protected void get(HttpExchange exchange, int id) throws Exception {
        final User responseData = staffService.get(id);
        sendJsonResponse(exchange, responseData, User.class);
    }

    /**
     * @param exchange
     * @return the Collection of clerks
     * @throws Exception
     */
    @Override
    protected void getAll(HttpExchange exchange) throws Exception {
        final Collection<User> responseData = staffService.getAll();
        sendJsonCollectionResponse(exchange, responseData, User.class);
    }

    /**
     * @param exchange
     * creates a Clerk if and only if the logged-in User is a Manager
     * @throws Exception
     */
    @Override
    protected void post(HttpExchange exchange) throws Exception {
        final User loggedInManager = Authenticator.instance.authManager(exchange);
        final User requestData = getJsonBodyData(exchange, User.class);
        final User responseData = staffService.create(loggedInManager.getWorksAt(), requestData);
        sendJsonResponse(exchange, responseData, User.class);
    }

    /**
     * @param exchange
     * @param id of the Clerk that is being updated
     * @return the updated Clerk based on the given changes
     * @throws Exception
     */
    @Override
    protected void put(HttpExchange exchange, int id) throws Exception{
        final User loggedInClerk = Authenticator.instance.authClerk(exchange);
        final User responseData = staffService.updateClerk(id, loggedInClerk);
        sendJsonResponse(exchange, responseData, User.class);
    }

    /**
     * @param exchange
     * @return the Clerk object of the logged-in Clerk
     * @throws Exception
     */
    protected void getLoggedIn(HttpExchange exchange) throws Exception {
        final User loggedInClerk = Authenticator.instance.authClerk(exchange);
        final User responseData = staffService.getLoggedIn(loggedInClerk);
        sendJsonResponse(exchange, responseData, User.class);
    }

    /**
     * @param exchange
     * @return the Collection of Clerks registered to the Bank that the logged-in Manager works at
     * @throws Exception
     */
    protected void getClerksOfBank(HttpExchange exchange) throws Exception{
        final User manager = Authenticator.instance.authManager(exchange);
        final Collection<User> responseData = staffService.getClerksOfBank(manager.getWorksAt());
        sendJsonCollectionResponse(exchange, responseData, User.class);
    }
}
