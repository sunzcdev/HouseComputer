package net.sunzc.housecomputer.db.inject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import net.sunzc.housecomputer.MyLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InjectUtils {

    private static final String TAG = "InjectUtils";

    public static List<Field> queryAllAnnotationField(Class clazz) {
        List<Field> anoFields = new LinkedList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column ano = field.getAnnotation(Column.class);
            if (ano != null) {
                anoFields.add(field);
            }
        }
        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        for (Field field : superFields) {
            Column ano = field.getAnnotation(Column.class);
            if (ano != null) {
                anoFields.add(field);
            }
        }
        return anoFields;
    }

    public static String getTableName(Class<?> clazz) {
        Table ano = clazz.getAnnotation(Table.class);
        if (ano != null) {
            return clazz.getSimpleName();
        }
        throw new SQLiteException(clazz.getSimpleName() + "没有加table注解");
    }

    public static String dropTable(Class clazz) {
        String dropTableSql = "DROP TABLE IF EXISTS " + getTableName(clazz);
        MyLog.d(TAG, dropTableSql);
        return dropTableSql;
    }

    public static String createTable(Class clazz) {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        String tableName = getTableName(clazz);
        sb.append(tableName).append(" ( ");
        List<Field> fields = queryAllAnnotationField(clazz);
        for (Field field : fields) {
            sb.append(field.getName());
            Class<?> type = field.getType();
            switch (type.getSimpleName()) {
                case "long":
                case "Long":
                case "int":
                case "Integer":
                case "byte":
                case "Byte":
                case "short":
                case "Short":
                    sb.append(" INTEGER ");
                    break;
                default:
                    sb.append(" TEXT ");
                    break;
            }
            Column column = field.getAnnotation(Column.class);
            if (column.primaryKey()) {
                sb.append("PRIMARY KEY ");
                if (column.autoIncrement()) {
                    sb.append("AUTOINCREMENT ");
                }
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(",")).append(");");
        MyLog.d(TAG, "createTable:" + sb.toString());
        return sb.toString();
    }

    public static <T> ContentValues toContentValues(T t) {
        ContentValues cv = new ContentValues();
        List<Field> fields = queryAllAnnotationField(t.getClass());
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column.autoIncrement()) {//自增主键不需要赋值
                continue;
            }
            switch (field.getType().getSimpleName()) {
                case "int":
                    cv.put(field.getName(), (int) getter(t, field.getName()));
                    break;
                case "Integer":
                    cv.put(field.getName(), (Integer) getter(t, field.getName()));
                    break;
                case "String":
                    cv.put(field.getName(), (String) getter(t, field.getName()));
                    break;
                case "double":
                    cv.put(field.getName(), (double) getter(t, field.getName()));
                    break;
                case "Double":
                    cv.put(field.getName(), (Double) getter(t, field.getName()));
                    break;
                case "long":
                    cv.put(field.getName(), (long) getter(t, field.getName()));
                    break;
                case "Long":
                    cv.put(field.getName(), (Long) getter(t, field.getName()));
                    break;
                case "float":
                    cv.put(field.getName(), (float) getter(t, field.getName()));
                    break;
                case "Float":
                    cv.put(field.getName(), (Float) getter(t, field.getName()));
                    break;
                case "boolean":
                    cv.put(field.getName(), (boolean) isser(t, field.getName()));
                    break;
                case "Boolean":
                    cv.put(field.getName(), (Boolean) isser(t, field.getName()));
                    break;
                case "short":
                    cv.put(field.getName(), (short) getter(t, field.getName()));
                    break;
                case "Short":
                    cv.put(field.getName(), (Short) getter(t, field.getName()));
                    break;
                case "byte":
                    cv.put(field.getName(), (byte) getter(t, field.getName()));
                    break;
                case "Byte":
                    cv.put(field.getName(), (Byte) getter(t, field.getName()));
                    break;
                default:
                    cv.put(field.getName(), getter(t, field.getName()).toString());
                    break;
            }
        }
        return cv;
    }

    public static <T> T generateDataBean(Cursor cursor, Class<T> tClass) {
        List<Field> fields = queryAllAnnotationField(tClass);
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (t == null) return null;
        for (Field field : fields) {
            switch (field.getType().getSimpleName()) {
                case "int":
                    int anint = cursor.getInt(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), anint, int.class);
                    break;
                case "Integer":
                    int anInt = cursor.getInt(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), anInt, Integer.class);
                    break;
                case "String":
                    String s = cursor.getString(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), s, String.class);
                    break;
                case "boolean":
                    boolean b = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(field.getName())));
                    setter(t, field.getName(), b, boolean.class);
                    break;
                case "Boolean":
                    boolean B = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(field.getName())));
                    setter(t, field.getName(), B, Boolean.class);
                    break;
                case "float":
                    float f = cursor.getFloat(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), f, float.class);
                    break;
                case "Float":
                    float F = cursor.getFloat(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), F, Float.class);
                    break;
                case "double":
                    double d = cursor.getDouble(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), d, double.class);
                    break;
                case "Double":
                    double D = cursor.getDouble(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), D, Double.class);
                    break;
                case "short":
                    int shortV = cursor.getShort(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), (short) shortV, short.class);
                    break;
                case "Short":
                    int ShortV = cursor.getShort(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), (short) ShortV, Short.class);
                    break;
                case "byte":
                    int by = cursor.getInt(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), (byte) by, byte.class);
                    break;
                case "Byte":
                    int By = cursor.getInt(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), (byte) By, Byte.class);
                    break;
                default:
                    String text = cursor.getString(cursor.getColumnIndex(field.getName()));
                    setter(t, field.getName(), text, String.class);
                    break;
            }
        }
        MyLog.d(TAG, "generateDataBean:" + t.toString());
        return t;
    }

    /*
     *@param obj 操作的对象
     *@param att 操作的属性
     *@param value 设置的值
     *@param type 参数的类型
     */
    private static void setter(Object obj, String att, Object value, Class<?> type) {
        try {
            Method met = obj.getClass().getMethod("set" + initStr(att), type);
            met.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object isser(Object obj, String att) {
        try {
            Method met = obj.getClass().getMethod("is" + initStr(att));
            return met.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static Object getter(Object obj, String att) {
        try {
            Method met = obj.getClass().getMethod("get" + initStr(att));
            return met.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static String initStr(String old) {   // 将单词的首字母大写
        return old.substring(0, 1).toUpperCase() + old.substring(1);
    }

    public static String queryAnnotationClassName(Object object) {
        return ((Class) ((ParameterizedType) (object.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]).getSimpleName();
    }

    public static <K, V> List<K> getKeyByValue(Map<K, V> map, V v) {
        List<K> ks = new ArrayList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(v)) {
                ks.add(entry.getKey());
            }
        }
        return ks;
    }

}
