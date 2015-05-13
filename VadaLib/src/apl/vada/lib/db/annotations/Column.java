package apl.vada.lib.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import apl.vada.lib.db.util.Constants;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column {

    boolean isPrimaryKey() default false;

    boolean isAutoincrement() default false;

    String name() default Constants.EMPTY;

    String type() default Constants.AUTO_ASSIGN;

}
