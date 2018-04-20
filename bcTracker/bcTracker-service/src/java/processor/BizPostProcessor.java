package processor;

import dao.BaseDao;
import dao.ItemImpl;
import dao.SiteImpl;
import helper.JSONHelper;
import helper.Utils;
import model.Block;
import model.Chain;
import model.Item;
import model.Site;
import org.omg.CORBA.portable.ApplicationException;

/***
 *Created by Lin Cheng
 */
public class BizPostProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (path){

            case "regsite":
                Site site = JSONHelper.fromJson2(body, Site.class);
                dao = new SiteImpl();
                if(dao.find(site.getName()) != null){
                    throw new Exception("Site name already exists.");
                }
                String pk = Utils.generateKeypair(site.getName());
                site.setPubkey(pk);
                dao.create(site);

                return pk;

            case "regitem":
                Item item = JSONHelper.fromJson2(body, Item.class);
                dao = new ItemImpl();

                if(dao.find(item.getId()) != null){
                    throw new Exception("Item ID already exists.");
                }
                Block b0 = new Block(item.getId(), item.getNextpubkey(), item.getPayload());
                Chain chain = new Chain();
                chain.append(b0);
                item.setChain(JSONHelper.toJson(chain));
                dao.create(item);

                return Utils.md5(JSONHelper.toJson(chain.getLast()));

            case "append":
                Item item2 = JSONHelper.fromJson2(body, Item.class);
                dao = new ItemImpl();
                Item ex = ((ItemImpl) dao).find(item2.getId());
                if(ex == null){
                    throw new Exception("Chain not found.");
                }

                Chain c = JSONHelper.fromJson2(ex.getChain(), Chain.class);
                Block b = new Block(item2.getId(), item2.getNextpubkey(), item2.getPayload());
                c.append(b);
                ex.setChain(JSONHelper.toJson(c));
                ex.setNextpubkey(item2.getNextpubkey());
                dao.update(ex);

                return "Success";

            default:
                throw new Exception("Not supported.");
        }
    }
}
