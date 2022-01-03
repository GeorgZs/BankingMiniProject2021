package crushers.services.transactions;

import java.util.Collection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import crushers.common.httpExceptions.HttpException;
import crushers.common.httpExceptions.MethodNotAllowedException;
import crushers.common.models.*;
import crushers.server.Authenticator;
import crushers.server.Router;

public class TransactionRouter extends Router {

    private final TransactionService transactionService;

    public TransactionRouter(TransactionService transactionService) {
        super("/transactions");
        this.transactionService = transactionService;
    }

    @Override
    public void addEndpoints(HttpServer server) throws Exception {
        super.addEndpoints(server);

        server.createContext(this.basePath + "/accounts/", (HttpExchange exchange) -> {
            try {
                final String[] pathParams = exchange.getRequestURI().getPath().split("/");
                final int accountId = Integer.parseInt(pathParams[pathParams.length - 1]);
          
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getAllOfAccount(exchange, accountId);
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

        server.createContext(basePath + "/interestRate/", (HttpExchange exchange) -> {
            try {
                final String[] pathParams = exchange.getRequestURI().getPath().split("/");
                final int accountId = Integer.parseInt(pathParams[pathParams.length - 1]);

                switch (exchange.getRequestMethod()) {
                    case "POST":
                        getInterest(exchange, accountId);
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

        server.createContext(basePath + "/suspicious", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getAllSuspiciousTransactions(exchange);
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

        server.createContext(basePath + "/recurring", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "POST":
                        createRecurringDeposit(exchange);
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

        server.createContext(basePath + "/labels/", (exchange) -> {
            try {
                final String[] pathParams = exchange.getRequestURI().getPath().split("/");
                final int transactionId = Integer.parseInt(pathParams[pathParams.length - 1]);

                switch (exchange.getRequestMethod()) {
                    case "PUT":
                        setLabels(exchange, transactionId);
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
     * creates a Transaction with HTTP body
     * @throws Exception
     */
    @Override
    protected void post(HttpExchange exchange) throws Exception{
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Transaction requestData = getJsonBodyData(exchange, Transaction.class);
        final Transaction responseData = transactionService.create(requestData, loggedInUser);
        sendJsonResponse(exchange, responseData, Transaction.class);
    }

    /**
     * @param exchange
     * @param id of the required Transaction
     * @return the Transaction with the ID as specified in method signature
     * @throws Exception
     */
    @Override
    protected void get(HttpExchange exchange, int id) throws Exception{
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Transaction responseData = transactionService.get(loggedInUser, id);
        sendJsonResponse(exchange, responseData, Transaction.class);
    }

    /**
     * @param exchange
     * @param accountId of Account containing Transactions
     * @return Collection of Transactions for the Account with ID specified in method signature
     * @throws Exception
     */
    private void getAllOfAccount(HttpExchange exchange, int accountId) throws Exception {
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Collection<Transaction> responseData = transactionService.getAllOfAccount(loggedInUser, accountId);
        sendJsonCollectionResponse(exchange, responseData, Transaction.class);
    }

    /**
     * @param exchange
     * @param accountId of Account
     * @return a Transaction of the Interest received
     * @throws Exception
     */
    private void getInterest(HttpExchange exchange, int accountId) throws Exception{
        final User customer = Authenticator.instance.authCustomer(exchange);
        final Transaction responseData = transactionService.getInterest(customer, accountId);
        sendJsonResponse(exchange, responseData, Transaction.class);
    }

    /**
     * @param exchange
     * @return Collection of all Transaction marked as suspicious
     * @throws Exception
     */
    private void getAllSuspiciousTransactions(HttpExchange exchange) throws Exception {
        final User clerk = Authenticator.instance.authClerk(exchange);
        final Collection<Transaction> responseData = transactionService.getAllSuspiciousTransactions(clerk.getWorksAt());
        sendJsonCollectionResponse(exchange, responseData, Transaction.class);
    }

    /**
     * @param exchange
     * @return Transaction that will be created based on the interval specified
     * @throws Exception
     */
    private void createRecurringDeposit(HttpExchange exchange) throws Exception {
        final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
        final Transaction requestData = getJsonBodyData(exchange, Transaction.class);
        final Transaction responseData = transactionService.createRecurringDeposit(loggedInCustomer, requestData);
        sendJsonResponse(exchange, responseData, Transaction.class);
    }

  /**
   * @param exchange
   * @return the Transaction with a Label specified
   * @throws Exception
   */
  private void setLabels(HttpExchange exchange, int transactionId) throws Exception{
    final User loggedInCustomer = Authenticator.instance.authCustomer(exchange);
    final TransactionLabels requestData = getJsonBodyData(exchange, TransactionLabels.class);
    final Transaction responseData = transactionService.setLabels(loggedInCustomer, transactionId, requestData);
    sendJsonResponse(exchange, responseData, Transaction.class);
  }
}
