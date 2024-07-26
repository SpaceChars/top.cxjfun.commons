package top.cxjfun.common.datasource.nosql.core.annotation;

import top.cxjfun.common.datasource.nosql.core.enums.FieldFill;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NosqlTableField {

    String value() default "";

    FieldFill fill() default FieldFill.DEFAULT;

}
