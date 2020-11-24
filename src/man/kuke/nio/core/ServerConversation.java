package man.kuke.nio.core;

import java.net.Socket;

class ServerConversation extends ServerCommuication{
    private final NIOServer server;
    private String clientId;
    private volatile boolean online;

    public ServerConversation(NIOServer server,Socket socket) {
        super(socket);
        this.server = server;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void dealMessage(NetMessage netMessage) {
        try {
            server.getActionProcess().dealRequst(netMessage.getAction()
                    ,netMessage.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dealPeerDown() {
        close();
        online = false;
        server.publishMessage("客户端 " +clientId + " 掉线");
    }

    public boolean isOnline() {
        return online;
    }

    public void receive() {
        super.receive();
    }


    public void sayHello() {
        sendMessage(new NetMessage().setProtocol(NetProtocol.HELLO).setFrom(server.SERVER));
    }

    @Override
    public void dealNetMessage(NetMessage netMessage) {
        server.getThreadPool().submit(new InnerTask(netMessage));
    }

    public void offline() {
        if (!online) {
            return;
        }
        sendMessage(new NetMessage().setProtocol(NetProtocol.FIN)
            .setFrom(server.SERVER));
        close();
        online=false;
    }

     void dealFin(NetMessage netMessage) {
        server.publishMessage("客户端[" + clientId + "]请求断线");
        close();
        online = false;
    }


    public void sendMessage(NetMessage netMessage) {
        super.sendMessage(netMessage);
    }

    public void close() {
        super.close();
        online = false;
    }

    public void sendMessage(String action, String message) {
        sendMessage(new NetMessage()
                .setProtocol(NetProtocol.MESSAGE)
                .setAction(action)
                .setFrom(server.SERVER)
                .setMessage(message));
    }

    private class InnerTask implements Runnable{
        private final NetMessage netMessage;

        public InnerTask(NetMessage netMessage) {
            this.netMessage = netMessage;
        }

        @Override
        public void run() {
            MethodScheduler.submit(ServerConversation.this,netMessage);
        }
    }
}
