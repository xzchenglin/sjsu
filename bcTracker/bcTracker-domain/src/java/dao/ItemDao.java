package dao;

import model.Item;

import java.util.List;

public interface ItemDao extends BaseDao<Item> {

    List<Item> findByPk(String pk) throws Exception;
}
