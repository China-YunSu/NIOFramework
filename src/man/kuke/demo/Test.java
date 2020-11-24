package man.kuke.demo;

import man.kuke.nio.core.NetMessage;
import man.kuke.nio.core.NetProtocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
//    public boolean checkClient(String message) {
//        String[] strs = message.split("#");
//        String ip = strs[0];
//        String time = strs[1];
//        String encode = strs[2];
//        String clientIp  = "127.0.0.1";
//        String code = clientIp + "#" +time;
//        return Integer.parseInt(encode) == code.hashCode();
//    }

    @org.junit.Test
    public void demo() {
        NetMessage netMessage = new NetMessage().setProtocol(NetProtocol.APPROVED);
        methodProcessor(netMessage);
    }

    @org.junit.Test
    public void demo2() {
        NetMessage netMessage = new NetMessage().setProtocol(NetProtocol.APPROVED).setFrom("Server").setMessage("dsa")
                .setAction("dwa");
        System.out.println(netMessage.getProtocol());
        System.out.println(netMessage.getFrom());
        System.out.println(netMessage.getMessage());
        System.out.println(netMessage.getAction());
        System.out.println(netMessage);
        //methodProcessor(netMessage);
    }

    public void methodProcessor(NetMessage netMessage) {

        try {
            String protocol = netMessage.getProtocol().toString();
            String[] split = protocol.split("_");
            StringBuilder methodName = new StringBuilder("deal");
            for (String part : split) {
                methodName.append(part.substring(0, 1)).append(part.substring(1).toLowerCase());
            }

            Method method = this.getClass().getDeclaredMethod(methodName.toString(), NetMessage.class);
            method.invoke(this, netMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String message = NetProtocol.HELLO + "&" + "userlogin" + "&" + "bala";
//                new Test().checkClient("127.0.0.1#1605517529218#2143477704");


    }
}
