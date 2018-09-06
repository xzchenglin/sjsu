package logi.service;

import logi.domain.model.Order;
import logi.domain.model.User;

import java.util.List;

public interface OrderService {
    /*
    sender place an order
     */
    Order place(Order order) throws Exception;

    /*
    driver take the order
     */
    Order take(Order order);

    /*
    driver pickup the item form sender or other driver
     */
    Order handover(Order order) throws Exception;

    /*
    driver deliver the item
     */
    Order deliver(Order order, String code);

    /*
    sender/driver cancel the order
     */
    Order cancel(Order order);

    /*
    accident during delivery
     */
    Order fail(Order order);

    OrderService user(User currentUser);

    List<User> retrieveHistory(Order order);
}
