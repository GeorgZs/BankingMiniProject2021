package crushers.services.banks;

import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpServer;
import crushers.models.Bank;

import crushers.models.exchangeInformation.BankDetails;
import crushers.models.exchangeInformation.InterestRate;
import crushers.models.users.Manager;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

import java.util.Collection;

public class BankRouter extends Router<Bank> {

    private BankService bankService;

    //this constructor means that when the BankRouter object
    //is created it sets the base path from the Router
    //super to /banks therefore setting it as an endpoint
    //for our server - routes us to the bank storage
    public BankRouter(BankService bankService) {
        super("/banks");
        this.bankService = bankService;
    }

    @Override
    public void addEndpoints(HttpServer server) throws Exception {
        super.addEndpoints(server);
        server.createContext(basePath + "/@interestRate", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "PUT":
                        changeInterestRate(exchange);
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

        server.createContext(basePath + "/bankInfo", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "PUT":
                        editBank(exchange);
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
     * @param id of the specified Bank
     * returns the Bank with the specified ID, displaying all of its information
     * @throws Exception
     */
    @Override
    protected void get(HttpExchange exchange, int id) throws Exception {
        final Bank responseData = bankService.get(id);
        sendJsonResponse(exchange, responseData);
    }

    /**
     * @param exchange
     * returns the Collection of all the Banks
     * @throws Exception
     */
    @Override
    protected void getAll(HttpExchange exchange) throws Exception {
        final Collection<Bank> responseData = bankService.getAll();
        sendJsonResponse(exchange, responseData);
    }

    /**
     * @param exchange
     * creates a Bank with the information specified in the HTTP body
     * @throws Exception
     */
    @Override
    protected void post(HttpExchange exchange) throws Exception {
        final Bank requestData = getJsonBodyData(exchange, Bank.class);
        final Bank responseData = bankService.create(requestData);
        sendJsonResponse(exchange, responseData);
    }

    /**
     * @param exchange
     * @param id of the Bank that needs updating
     * returns the updated Bank - HTTP body is the entire bank object
     * @throws Exception
     */
    @Override
    protected void put(HttpExchange exchange, int id) throws Exception {
        final Bank requestData = getJsonBodyData(exchange, Bank.class);
        final Bank responseData = bankService.updateBank(id, requestData);
        sendJsonResponse(exchange, responseData);
    }

    /**
     * @param exchange
     * returns the changed interest rate based on the passed InterestRate object
     * @example
     *  {
     *     "rate": 0.142
     *  }
     * @throws Exception
     */
    private void changeInterestRate(HttpExchange exchange) throws Exception {
        final Manager loggedInManager = Authenticator.instance.authManager(exchange);
        final InterestRate requestData = getJsonBodyData(exchange, InterestRate.class);
        final double responseData = bankService.changeInterestRate(loggedInManager, requestData);
        sendJsonResponse(exchange, responseData);
    }

    /**
     * @param exchange
     * returns the Bank Details that were updated - the HTTP body is that of the Bank Details class
     * @example
     *  {
     *      "logo": "logo.png",
     *      "details": "This is a test bank",
     *      "name": "TestBank"
     *  }
     * @throws Exception
     */
    protected void editBank(HttpExchange exchange) throws Exception {
        final Manager manager = Authenticator.instance.authManager(exchange);
        final BankDetails requestData = getJsonBodyData(exchange, BankDetails.class);
        final BankDetails responseData = bankService.editBank(manager, requestData);
        sendJsonResponse(exchange, responseData);
    }
}
