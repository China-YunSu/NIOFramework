package man.kuke.nio.core;

import java.net.Socket;

class ClientConversation extends ClientCommuication {
    private final NIOClient client;
    private String id;

    ClientConversation(NIOClient client, Socket socket) {
        super(socket);
        this.client = client;
    }


    String getId() {
        return id;
    }

    @Override
    public void dealPeerDown() {
        close();
        client.getClientAction().serverAbnormalDrop();
    }

    public void start() {
        super.start();
    }


    @Override
    public void dealNetMessage(NetMessage netMessage) {
        MethodScheduler.submit(this, netMessage);
    }

    public void offline() {
        sendMessage(new NetMessage().setProtocol(NetProtocol.FIN)
                .setFrom(client.CLIENT));
        close();
    }

    public void dealHello(NetMessage netMessage) {
    }

    public void dealConnectOutTime(NetMessage netMessage) {
        close();
        client.getClientAction().connectOutTime();
    }

    public void dealFin(NetMessage netMessage) {
        close();
        client.getClientAction().serverForceDown();
    }

    void dealApproved(NetMessage netMessage) {
        id = netMessage.getMessage();
    }

    void dealWhoAreYou(NetMessage netMessage) {
        String ip = socket.getLocalAddress().getHostAddress();
        String code = ip + "#" + System.currentTimeMillis();
        String encode = code + "#" + code.hashCode();
        sendMessage(new NetMessage().setProtocol(NetProtocol.I_AM)
                .setMessage(encode)
                .setFrom(client.CLIENT));
    }

    public void close() {
        super.close();
    }

    public void dealMessage(NetMessage netMessage) {
        try {
            client.getActionProcess().dealResponse(netMessage.getAction()
                        ,netMessage.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String action, String parameter) {
        sendMessage(new NetMessage()
                .setProtocol(NetProtocol.MESSAGE)
                .setFrom(client.CLIENT)
                .setAction(action)
                .setMessage(parameter));
    }
}
