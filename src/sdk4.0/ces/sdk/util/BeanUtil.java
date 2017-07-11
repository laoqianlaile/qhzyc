package ces.sdk.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ces.sdk.system.annotation.FieldIgnore;

public class BeanUtil {

	public static <T>Object[] getDeclaredFields(Class<T> clazz){
		List<Field> fields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
		try {
			for (int i = fields.size() - 1 ; i >= 0 ; i--) {
				String fieldName = fields.get(i).getName();
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				String getMethodName = "get" + firstLetter + fieldName.substring(1);
				Method method = clazz.getMethod(getMethodName, new Class[] {});
				if (method.isAnnotationPresent(FieldIgnore.class)) {
					fields.remove(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fields.toArray();
	}
}
