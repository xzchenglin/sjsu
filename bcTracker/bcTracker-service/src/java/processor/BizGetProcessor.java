package processor;

import dao.BaseDao;
import dao.ItemImpl;
import dao.SiteImpl;
import helper.JSONHelper;
import bc.Chain;
import model.Item;
import model.Site;
import org.apache.camel.Exchange;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *Created by Lin Cheng
 */
public class BizGetProcessor extends GetProcessor {

    @Override
    Object handle(final Map<String, Object> map) throws Exception {

        BaseDao dao;

        switch (path){

            case "history":
                dao = new ItemImpl();
                String id = paramMap.get("id");
                Item item = ((ItemImpl) dao).find(id);

                List<String> keys = new ArrayList<>();
                if(item != null) {
                    Chain c = JSONHelper.fromJson2(item.getChain(), Chain.class);
                    if(c.verify()) {
                        keys = c.getBlocks().stream().map(b -> b.pukNext).collect(Collectors.toList());
                    } else {
                        throw new Exception("Failed to verify chain.");
                    }
                }

                return JSONHelper.toJson(keys);

            case "fetch":
                String pk = paramMap.get("pk");
                dao = new ItemImpl();
                List<Item> items = ((ItemImpl) dao).findByPk(pk);
                List<Item> ret = new ArrayList<>();
                if(items != null && items.size()>0){
                    ret = items.stream().map(item1 -> item1.applyHash()).collect(Collectors.toList());
                }

                return JSONHelper.toJson(ret);

            case "sites":
                dao = new SiteImpl();
                List<Site> sites = dao.list(null);
                return JSONHelper.toJson(sites);

            case "download":
                String filePath = "/opt/bc/" + paramMap.get("name") + ".key";
                map.put("Content-Disposition","attachment; filename=\"key\"");
                map.put(Exchange.CONTENT_TYPE, "application/force-download");
                File file = new File(filePath);
                return file;

            default:
                return "Not supported.";
        }

    }

}
