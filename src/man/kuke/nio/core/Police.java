package man.kuke.nio.core;

import java.io.IOException;

public class Police implements Runnable {
    private final ServerConversation client;
    private final NIOServer server;

    public Police(ServerConversation client, NIOServer server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        client.sendMessage(new NetMessage().setProtocol(NetProtocol.WHO_ARE_YOU));
        synchronized (this) {
            try {
                wait(50);
                if (client.dis.available() < 1) {
                    client.sendMessage(new NetMessage().setProtocol(NetProtocol.CONNECT_OUT_TIME));
                    client.close();
                    return;
                }

                String message = client.dis.readUTF();
                dealNetMessage(new NetMessage(message));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkClient(String message) {
        try {
            String[] strs = message.split("#");
            String time = strs[1];
            String encode = strs[2];
            String clientIp = client.socket.getInetAddress().getHostAddress();
            String code = clientIp + "#" + time;
            return Integer.parseInt(encode) == code.hashCode();
        } catch (Exception ignored) {
        }
        return false;
    }


    public void dealIAm(NetMessage netMessage) {
        String message = netMessage.getMessage();
        if (checkClient(message)) {
            String clientId = "" + client.socket.getInetAddress().getHostAddress().hashCode()
                    + System.currentTimeMillis();
            client.sendMessage(new NetMessage().setProtocol(NetProtocol.APPROVED)
                    .setMessage(clientId)
                    .setFrom(server.SERVER));
            client.setClientId(clientId);
            server.addClient(client);
            client.setOnline(true);
            server.publishMessage("客户端" + "[" + client.socket.getInetAddress().getHostAddress() + "]"
                    + "已通过验证");
            return;
        }

        client.close();
    }

    private void dealNetMessage(NetMessage netMessage) {
        if (netMessage.getProtocol() != NetProtocol.I_AM) {
            client.close();
        }
        MethodScheduler.submit(this, netMessage);
    }
}
