package crushers.services.transactions;

import com.sun.net.httpserver.HttpExchange;
import crushers.models.exchangeInformation.Transaction;
import crushers.server.Router;

public class TransactionRouter extends Router<Transaction> {

    private final TransactionService transactionService;

    public TransactionRouter(TransactionService transactionService) {
        super("/transactions");
        this.transactionService = transactionService;
    }

    @Override
    protected void post(HttpExchange exchange) throws Exception{

    }

    @Override
    protected void get(HttpExchange exchange, int id) throws Exception{

    }

    @Override
    protected void getAll(HttpExchange exchange) throws Exception{

    }
}
