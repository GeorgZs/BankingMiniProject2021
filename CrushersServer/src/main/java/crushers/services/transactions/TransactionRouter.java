package crushers.services.transactions;

import java.util.Collection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import crushers.models.exchangeInformation.*;
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

        server.createContext(basePath + "/suspicious/", (HttpExchange exchange) -> {
            try {
                final String[] pathParams = exchange.getRequestURI().getPath().split("/");
                final int accountId = Integer.parseInt(pathParams[pathParams.length - 1]);

                switch (exchange.getRequestMethod()) {
                    case "POST":
                        markSusTransaction(exchange, accountId);
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

        server.createContext(basePath + "/loan", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "POST":
                        getLoan(exchange);
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

        server.createContext(basePath + "/payBackLoan", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "POST":
                        payBackLoan(exchange);
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

        server.createContext(basePath + "/label", (exchange) -> {
            try {
                switch (exchange.getRequestMethod()) {
                    case "PUT":
                        setLabel(exchange);
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

    private void getAllOfAccount(HttpExchange exchange, int accountId) throws Exception {
        final User loggedInUser = Authenticator.instance.authUser(exchange);
        final Collection<Transaction> responseData = transactionService.getAllOfAccount(loggedInUser, accountId);
        sendJsonResponse(exchange, responseData);
    }

    private void markSusTransaction(HttpExchange exchange, int transactionID) throws Exception{
        final Clerk clerk = Authenticator.instance.authClerk(exchange);
        final Transaction responseData = transactionService.markSusTransaction(clerk, transactionID);
        sendJsonResponse(exchange, responseData);
    }

    private void getInterest(HttpExchange exchange, int accountId) throws Exception{
        final Customer customer = Authenticator.instance.authCustomer(exchange);
        final Transaction responseData = transactionService.getInterest(customer, accountId);
        sendJsonResponse(exchange, responseData);
    }

    private void getAllSuspiciousTransactions(HttpExchange exchange) throws Exception {
        final Clerk clerk = Authenticator.instance.authClerk(exchange);
        final Collection<Transaction> responseData = transactionService.getAllSuspiciousTransactions(clerk);
        sendJsonResponse(exchange, responseData);
    }

    private void createRecurringDeposit(HttpExchange exchange) throws Exception {
        final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
        final RecurringTransaction requestData = getJsonBodyData(exchange, RecurringTransaction.class);
        final RecurringTransaction responseData = transactionService.createRecurringDeposit(loggedInCustomer, requestData);
        sendJsonResponse(exchange, responseData);
    }

    private void getLoan(HttpExchange exchange) throws Exception{
        final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
        final Loan requestData = getJsonBodyData(exchange, Loan.class);
        final Transaction responseData = transactionService.getLoan(loggedInCustomer, requestData);
        sendJsonResponse(exchange, responseData);
    }

    private void payBackLoan(HttpExchange exchange) throws Exception{
        final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
        final Transaction requestData = getJsonBodyData(exchange, Transaction.class);
        final Loan responseData = transactionService.payBackLoan(loggedInCustomer, requestData);
        sendJsonResponse(exchange, responseData);
    }

    private void setLabel(HttpExchange exchange) throws Exception{
        final Customer loggedInCustomer = Authenticator.instance.authCustomer(exchange);
        final TransactionLabel requestData = getJsonBodyData(exchange, TransactionLabel.class);
        final Transaction responseData = transactionService.setLabel(loggedInCustomer, requestData);
        sendJsonResponse(exchange, responseData);
    }
}
