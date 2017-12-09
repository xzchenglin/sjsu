package service.processor;

import common.JsonHelper;
import dao.*;
import model.Customer;

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
