package logi.controller;

import logi.comm.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/sec")
public class SecController {

    static Log logger = LogFactory.getLog(SecController.class);

    @Value("${app.path}")
    private String appPath;

    @GetMapping("/enc")
    public String enc(@RequestParam(value="name") String name,
                              @RequestParam(value="text") String text) throws Exception{

        return Utils.rsaEnc(text, appPath + "/keys/" + name + ".key");
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value="name") String name) throws Exception {

        Path filePath = Paths.get(appPath + "/keys/").resolve(name + ".key").normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if(!resource.exists()) {
            throw new RuntimeException("File not found " + name);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
