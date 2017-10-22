package domain.s3;

import domain.model.s3.S3Object;

/**
 * Created by Lin Cheng on 2017/10/8.
 */
public interface S3Manager {
    S3Object browse(String path);
    String upload(String path);
    String delete(String path);

    String meta(String path);

    com.amazonaws.services.s3.model.S3Object download(String path);
}
