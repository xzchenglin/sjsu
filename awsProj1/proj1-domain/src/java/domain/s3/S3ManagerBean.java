package domain.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import common.JsonHelper;
import domain.helper.DbHelper;
import domain.model.s3.S3Object;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Lin Cheng on 2017/10/8.
 */
public class S3ManagerBean implements S3Manager {

    String bucket = DbHelper.runWithSingleResult("select value from config where key='bucket'");
    String cdn = DbHelper.runWithSingleResult("select value from config where key='cdn'");
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1").enableAccelerateMode().build();

    static final String FIRST_NAME = Headers.S3_USER_METADATA_PREFIX + "first_name";
    static final String LAST_NAME = Headers.S3_USER_METADATA_PREFIX + "last_name";
    static final String UPLOADED = Headers.S3_USER_METADATA_PREFIX + "upload";

    static final String root = "/opt/proj1/";

    @Override
    public S3Object browse(String path) {

        if (StringUtils.isNotBlank(path)) {
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ObjectListing ol = s3.listObjects(bucket, path);
//        s3.createBucket("just-to-get-id");
//        List list = s3.listBuckets();

        List<S3ObjectSummary> objects = ol.getObjectSummaries();

        S3Object root = new S3Object();
        root.setFolder(true);
        if (StringUtils.isEmpty(path)) {
            root.setName(bucket);
        } else {
            root.setName(path);
        }

        for (S3ObjectSummary os: objects) {
            String key = os.getKey();
            if(key.equals(path)){
                continue;
            }
            String subKey = path == null ? key: key.replace(path, "");
            String[] parts = subKey.split("/");
            if(parts.length != 1){
                continue;
            }
            S3Object f = new S3Object();
            f.setName(parts[parts.length-1]);
            f.setFolder(key.endsWith("/"));
            f.setFullPath(key);
            f.setUpdateTime(os.getLastModified().getTime());

            String metaStr = "Modified at:" + os.getLastModified().toString() + "<br>";

            if(key != null) {
                ObjectMetadata meta = s3.getObjectMetadata(bucket, key);


                Set<String> keySet = new HashSet<>();
                keySet.addAll(meta.getRawMetadata().keySet());
                keySet.addAll(meta.getUserMetadata().keySet());
                for (String k : keySet) {
                    String name = null;
                    switch (k) {
                        case "Content-Length":
                            name = "Size: ";
                            break;
                        case "x-amz-replication-status":
                            name = "Replicate Status: ";
                            break;
                        case "Content-Type":
                            name = "Type: ";
                            break;
                        case FIRST_NAME:
                            name = "First Name: ";
                            break;
                        case LAST_NAME:
                            name = "Last Name: ";
                            break;
                        case UPLOADED:
                            name = "Upload at: ";
                            break;
                    }
                    if (name != null) {
                        if(k.startsWith(Headers.S3_USER_METADATA_PREFIX)){
                            metaStr += name + meta.getUserMetadata().get(k) + "<br>";
                        } else {
                            metaStr += name + meta.getRawMetadata().get(k) + "<br>";
                        }
                    }
                }
            }
            f.setMeta(metaStr);

            root.addChild(f);
        }
        try {
            String x= JsonHelper.toJson(root);
            System.out.println(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public String upload(String path) {
        try {
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(new CanonicalGrantee("8df1e4cda6ef063de238495fd071e0716063fc689c198b3ad4fa19220aa24288"), Permission.FullControl);
            acl.grantPermission(GroupGrantee.AuthenticatedUsers, Permission.Read);
            File file = new File(root + "upload/" + path.split("/")[path.split("/").length-1]);
            ObjectMetadata meta = new ObjectMetadata();
            Map<String, String> md = new HashMap<>();
            //hard code for now before due to Cognito issue
            md.put(FIRST_NAME, "Lin");
            md.put(LAST_NAME, "Cheng");
            md.put(UPLOADED, new Date().toString());
            meta.setUserMetadata(md);
            s3.putObject(new PutObjectRequest(bucket, path, file).withAccessControlList(acl).withMetadata(meta));
            return path;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return null;
        }
    }

    @Override
    public String delete(String path) {
        try {
            s3.deleteObject(bucket, path);
            return path;
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return null;
        }
    }

    @Override
    public String meta(String path) {
        try {
            ObjectMetadata meta = s3.getObjectMetadata(bucket, path);
            return JsonHelper.toJson(meta);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return null;
        }
    }

    @Override
    public com.amazonaws.services.s3.model.S3Object download(String path) {
        com.amazonaws.services.s3.model.S3Object o = null;
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(
                    root + "download/" + path.split("/")[path.split("/").length-1]));
            byte[] buf = new byte[1024];
            int len = 0;

            URL url = new URL(cdn + "/" + path);
            try{
                inputStream = url.openStream();
                while ((len = inputStream.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                System.out.println("read from CDN.");
            } catch (Throwable e){
                o = s3.getObject(bucket, path);
                inputStream = o.getObjectContent();
                while ((len = inputStream.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                System.out.println("read from S3.");
            }
       } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fos);
        }
        return o;
    }
}
