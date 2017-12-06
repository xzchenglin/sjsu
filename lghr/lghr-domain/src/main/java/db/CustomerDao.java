package db;

import model.Customer;

public interface CustomerDao extends BaseDao<Customer> {

    Customer createAndGetByExId(Customer cust) throws Exception;
}
