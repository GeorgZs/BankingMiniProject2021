package crushers.services.notifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import crushers.common.httpExceptions.*;
import crushers.common.models.*;
import crushers.services.accounts.AccountService;

public class NotificationService {

  private final JsonNotificationStorage storage;
  private final AccountService accountService;

  public NotificationService(AccountService accountService, JsonNotificationStorage storage) {
    this.storage = storage;
    this.accountService = accountService;
  }

  /**
   * @param loggedInManager
   * @param notification
   * @return the Notification that was sent to all Users of the Manager's Bank
   * @throws Exception if the Customer storage for the Bank is empty
   */
  public Notification sendNotificationToUsers(User loggedInManager, Notification notification) throws Exception {
    if (notification == null) {
      throw new BadRequestException("Notification cannot be null");
    }

    if (notification.getContent().isBlank()) {
      throw new BadRequestException("Notification content cannot be empty");
    }

    if (notification.getTime() == null) {
      notification.setTime(LocalDateTime.now());
    }

    notification.setSender(loggedInManager.getWorksAt());
    return storage.create(notification);
  }

   /**
   * @param customer who is logged-in
   * @return the LinkedHashMap of all Notifications of the logged-in Customer
   * @throws Exception
   */
  public Collection<Notification> getNotifications(User user) throws Exception {
    Set<Notification> notifications = new HashSet<>();

    if (user.isClerk()) {
      notifications = storage.getNotificationsOfBank(user.getWorksAt());
    }
    else {
      Collection<BankAccount> accounts = this.accountService.getOfCustomer(user);
      for (BankAccount account : accounts) {
        notifications.addAll(storage.getNotificationsOfBank(account.getBank()));
      }
    }

    List<Notification> sortedNotifications = new ArrayList<>();
    sortedNotifications.addAll(notifications);
    Collections.sort(sortedNotifications);
    return sortedNotifications;
  }

}
