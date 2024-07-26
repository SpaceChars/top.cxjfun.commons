package top.cxjfun.common.web.mvc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;


@RestControllerAdvice
public class RequestControllerAdvice implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(BindException.class)
    public ControllerResult validException(BindException e) {
        StaticLog.error(e,"请求参数验证失败异常:");
        return new ControllerResult().setType(ControllerResultType.ERROR).setMessage(e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ControllerResult controllerException(AccessDeniedException e) {
        return new ControllerResult().setType(ControllerResultType.ERROR).setMessage("暂无操作权限！");
    }

    @ExceptionHandler(Exception.class)
    public ControllerResult controllerException(Exception e) {
        StaticLog.error(e,"运行时异常:");
        return new ControllerResult().setType(ControllerResultType.ERROR).setMessage(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ControllerResult controllerException(IllegalArgumentException e) {
        StaticLog.error(e,"执行方法参数验证异常:");
        return new ControllerResult().setType(ControllerResultType.ERROR).setMessage(e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //判断是否为swagger openapiJson返回数据
        if(returnType.getMethod().getName().equals("openapiJson")){
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String name = returnType.getParameterType().getName();
        ControllerResult result =new ControllerResult();

        //判断是否是Servlet输出
        if(returnType.getParameterType().isAssignableFrom(ResponseEntity.class)){
            LinkedHashMap http = (LinkedHashMap) body;
            result.setType(ControllerResultType.ERROR);
            result.setMessage(http.get("path")+" "+http.get("status")+" "+http.get("error"));
        }else{
            if(!name.equals(ControllerResult.class.getName())){
                result.setData(body);
            }else{
                if(!ObjectUtil.isEmpty(body)){
                    result=(ControllerResult) body;
                }
            }
        }

        //判断转换器类型
        if (selectedConverterType.isAssignableFrom(StringHttpMessageConverter.class)) {
            return JSONUtil.toJsonStr(result);
        }else{
            return result;
        }
    }
}
