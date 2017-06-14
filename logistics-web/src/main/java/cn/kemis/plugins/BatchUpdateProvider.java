package cn.kemis.plugins;

import cn.kemis.tools.CamelUnderlineUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-11
 */
public class BatchUpdateProvider {
    public static String batchUpdateSelective(Map<String, Object> params) throws IllegalAccessException {
        // update sys_user set username = '1', password = 2 where id = 1;

        Collection collection = (Collection) params.get("collection");
        Class clazz = (Class) params.get("clazz");

        String tableName = CamelUnderlineUtils.camelToUnderline(clazz.getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer buffer = new StringBuffer();

        for (Object object : collection) {
            buffer.append("update `").append(tableName).append("` set ");
            Object id = null;
            String idColumn = null;
            for (Field field : fields) {
                if (field.getName().equals("serialVersionUID")) continue;
                field.setAccessible(true);
                Object o = field.get(object);
                idColumn = CamelUnderlineUtils.firstCharLowerCase(clazz.getSimpleName()) + "Id";

                if (field.getName().equals(idColumn)) {
                    id = o;
                    continue;
                }

                if (o != null) {
                    buffer.append("`").append(field.getName()).append("` = ");
                    if (o instanceof Boolean) {
                        buffer.append("b'");
                        buffer.append((Boolean) o ? 1 : 0);
                        buffer.append("',");
                    } else {
                        buffer.append("'").append(o).append("',");
                    }
                }
            }

            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append(" where `").append(idColumn).append("` = '").append(id).append("';");
            id = null;
        }

        return buffer.toString();
    }
}
