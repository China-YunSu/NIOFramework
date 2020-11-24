package man.kuke.nio.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodScheduler {
    public static void submit(Object object, NetMessage netMessage) {
        try {
            String protocol = netMessage.getProtocol().toString();
            String[] split = protocol.split("_");
            StringBuilder methodName = new StringBuilder("deal");
            for (String part : split) {
                methodName.append(part.substring(0, 1)).append(part.substring(1).toLowerCase());
            }
            Method method = object.getClass().getDeclaredMethod(methodName.toString(), NetMessage.class);
            method.invoke(object, netMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
