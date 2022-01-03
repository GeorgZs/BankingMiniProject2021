package crushers.services.notifications;

import java.util.Collection;

import com.sun.net.httpserver.*;

import crushers.common.models.*;
import crushers.server.Router;
import crushers.server.Authenticator;

public class NotificationRouter extends Router {

  private final NotificationService service;

  public NotificationRouter(NotificationService service) {
    super("/notifications");
    this.service = service;
  }

  /**
   * @param exchange
   * sends a Notification to all Users registered to the Bank at which the Manager works at
   * @throws Exception
   */
  @Override
  public void post(HttpExchange exchange) throws Exception {
    final User loggedInManager = Authenticator.instance.authManager(exchange);
    final Notification requestData = getJsonBodyData(exchange, Notification.class);
    final Notification responseData = service.sendNotificationToUsers(loggedInManager, requestData);
    sendJsonResponse(exchange, responseData, Notification.class);
  }

  /**
   * @param exchange
   * @return a LinkedHashMap of Notifications that belong to the logged-in user
   * @throws Exception
   */
  @Override
  public void getAll(HttpExchange exchange) throws Exception{
    final User loggedInUser = Authenticator.instance.authUser(exchange);
    final Collection<Notification> responseData = service.getNotifications(loggedInUser);
    sendJsonCollectionResponse(exchange, responseData, Notification.class);
  }
}
