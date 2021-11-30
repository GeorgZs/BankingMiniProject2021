package crushers.services.transactions;

import com.sun.net.httpserver.HttpExchange;
import crushers.models.exchangeInformation.Transaction;
import crushers.models.users.User;
import crushers.server.Authenticator;
import crushers.server.Router;

public class TransactionRouter extends Router<Transaction> {

    private final TransactionService transactionService;

    public TransactionRouter(TransactionService transactionService) {
        super("/transactions");
        this.transactionService = transactionService;
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

    }

    @Override
    protected void getAll(HttpExchange exchange) throws Exception{

    }
}
