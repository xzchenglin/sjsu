package service.processor;

import common.JsonHelper;
import db.*;
import model.Customer;
import model.Order;
import model.Product;

/***
 *Created by Lin Cheng
 */
public class ExloginProcessor extends PostProcessor {

    @Override
    String handle() throws Exception {

        switch (paramMap.get("type")){
            case "customer":
                CustomerDao dao = new CustomerImpl();
                return JsonHelper.toJson(dao.createAndGetByExId(JsonHelper.fromJson(body, Customer.class)));
            default:
                return JsonHelper.toJson("Not supported.");
        }

    }
}
