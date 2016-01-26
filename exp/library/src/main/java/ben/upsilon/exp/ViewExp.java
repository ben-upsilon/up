package ben.upsilon.exp;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by ben on 14/11/8.
 */
public final class ViewExp {


    private static final String LogTag=ViewExp.class.getCanonicalName();

    public  static void init(Activity _context){
        Field[] fields=_context.getClass().getDeclaredFields();
        if(null !=fields&&fields.length>0){
            for (Field field:fields){
                Log.d(LogTag,field.toGenericString());
                id _id=field.getAnnotation(id.class);
                if(null!=_id){
                   View view= _context.findViewById(_id.value());
                    if (null != view) {
                        field.setAccessible(true);
                        try {
                            field.set(_context,view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface id {
        int value() default -1;
    }
}
