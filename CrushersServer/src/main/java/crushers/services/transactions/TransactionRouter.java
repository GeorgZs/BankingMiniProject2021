package crushers.services.transactions;

import java.util.Collection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import crushers.models.exchangeInformation.Contact;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.Clerk;
import crushers.models.users.Customer;
import crushers.models.users.User;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

public class TransactionRouter extends Router<Transaction> {

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
                final int acccountId = Integer.parseInt(pathParams[pathParams.length - 1]);
          
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        getAllOfAccount(exchange, acccountId);
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

        server.createContext(basePath + "/suspicious/", (HttpExchange exchange) -> {
            try {
                final String[] pathParams = exchange.getRequestURI().getPath().split("/");
                final int acccountId = Integer.parseInt(pathParams[pathParams.length - 1]);

                switch (exchange.getRequestMethod()) {
                    case "POST":
                        markSusTransaction(exchange, acccountId);
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
                        getAllSusTransactions(exchange);
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
    protected void post(HttpExchange exchange) throws Exception{
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Transaction requestData = getJsonBodyData(exchange, Transaction.class);
        final Transaction responseData = transactionService.create(requestData, loggedInUser);
        sendJsonResponse(exchange, responseData);
    }

    @Override
    protected void get(HttpExchange exchange, int id) throws Exception{
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Transaction responseData = transactionService.get(loggedInUser, id);
        sendJsonResponse(exchange, responseData);
    }

    private void getAllOfAccount(HttpExchange exchange, int acccountId) throws Exception {
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Collection<Transaction> responseData = transactionService.getAllOfAccount(loggedInUser, acccountId);
        sendJsonResponse(exchange, responseData);
    }

    private void markSusTransaction(HttpExchange exchange, int transactionID) throws Exception{
        final Clerk clerk = Authenticator.instance.authClerk(exchange);
        final Transaction responseData = transactionService.markSusTransaction(clerk, transactionID);
        sendJsonResponse(exchange, responseData);
    }

    private void getAllSusTransactions(HttpExchange exchange) throws Exception{
        final Clerk clerk = Authenticator.instance.authClerk(exchange);
        final Collection<Transaction> responseData = transactionService.getAllSusTransaction(clerk);
        sendJsonResponse(exchange, responseData);
    }
}
