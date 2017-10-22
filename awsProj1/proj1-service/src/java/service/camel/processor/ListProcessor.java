package service.camel.processor;

import common.JsonHelper;
import domain.model.s3.S3Object;
import domain.s3.S3Manager;
import domain.s3.S3ManagerBean;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {

    @Override
    String handle() throws Exception {
        S3Manager mgr = new S3ManagerBean();
        S3Object so = mgr.browse(paraMap.get("path"));
        return JsonHelper.toJson(so);
    }
}
