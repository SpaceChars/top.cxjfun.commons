package top.cxjfun.common.oss;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Data
@Accessors(chain = true)
public class OssPutFile {

    private String ossId;

    private String bucketName;

    private InputStream inputStream;

    private String contentType;
}
