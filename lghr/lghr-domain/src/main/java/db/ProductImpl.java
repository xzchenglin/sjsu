package db;

import model.Product;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Lin Cheng
 */
public class ProductImpl extends BasePOJO implements ProductDao{

    @Override
    public Product create(Product o) throws Exception{
        SqlSession s = client.openSession(true);
        s.insert("ns.product.create", o);
        s.close();
        return o;
    }

    @Override
    public Product update(Product o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.product.update", o);
        s.close();
        return o;
    }

    @Override
    public Product getById(String id) throws Exception{
        SqlSession s = client.openSession(true);
        Product o = s.selectOne("ns.product.getById", id);
        s.close();
        return o;
    }

    @Override
    public List<Product> list(String kw) throws Exception {
        List<Product> ret;
        SqlSession s = client.openSession(true);
        if(StringUtils.isBlank(kw)) {
            ret = s.selectList("ns.product.list");
        } else {
            Product p = new Product();
            kw = "%" + kw.toLowerCase() + "%";
            p.setProductName(kw);
            p.setProductLine(kw);
            p.setProductDescription(kw);
            p.setProductVendor(kw);

            ret = s.selectList("ns.product.list", p);
        }
        s.close();
        return ret;
    }

    @Override
    public void deleteById(String id) throws Exception {
        SqlSession s = client.openSession(true);
        s.delete("ns.product.deleteById", id);
        s.close();
    }

}
