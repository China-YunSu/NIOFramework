package man.kuke.nio.core;

import man.kuke.action.core.IActionProcess;

import java.io.IOException;
import java.net.Socket;

public class NIOClient extends InitializeAdpter{
    private int port;
    private String ip;
    private ClientConversation clientConversation;
    final String CLIENT = "CLIENT";
    private IActionProcess actionProcess;
    private IClientAction clientAction;

    {
        InitialObject.invoke(this);
    }

    public NIOClient() {
    }

    public String getId() {
        return clientConversation.getId();
    }

    public NIOClient(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    public IActionProcess getActionProcess() {
        return actionProcess;
    }


    public IClientAction getClientAction() {
        return clientAction;
    }

    public void connectTOServer() throws IOException {
        if (clientAction == null) {
            throw new NoSetClientActionException();
        }
        Socket socket = new Socket(ip, port);
        clientConversation = new ClientConversation(this,socket);
        clientConversation.start();
    }

    public void offline() {
        if(clientAction.ensureOffline()) {
            clientConversation.offline();
        }
        clientAction.afterOffline();
    }

    public void sendMessage(String message) {
        clientConversation.sendMessage(new NetMessage());
    }

    public void close() {
        clientConversation.close();
    }

    public void sendMessage(String action, String parameter) {
        this.clientConversation.sendMessage(action, parameter);
    }
    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setActionProcess(IActionProcess actionProcess) {
        this.actionProcess = actionProcess;
    }
}
