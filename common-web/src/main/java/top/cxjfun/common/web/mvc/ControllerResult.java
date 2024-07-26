package top.cxjfun.common.web.mvc;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ControllerResult {

    private String message;

    private Object data;

    private ControllerResultType type = ControllerResultType.SUCCESS;
}
