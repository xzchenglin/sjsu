package service.processor;

import common.JsonHelper;
import db.BaseDao;
import db.CustomerImpl;
import db.OrderImpl;
import db.ProductImpl;
import model.Customer;
import model.Order;
import model.Product;

/***
 *Created by Lin Cheng
 */
public class UpdateProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {
        BaseDao dao;

        switch (paramMap.get("type")){
            case "product":
                dao = new ProductImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, Product.class)));
            case "order":
                dao = new OrderImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, Order.class)));
            case "customer":
                dao = new CustomerImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, Customer.class)));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
