package org.flexpay.common.test;

import java.lang.reflect.Field;

public class TestHelper {

    public static void setPrivateField(Object o, String fieldName, Object fieldValue)
			throws NoSuchFieldException, IllegalAccessException {

        Field field = o.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(o, fieldValue);
    }

}
