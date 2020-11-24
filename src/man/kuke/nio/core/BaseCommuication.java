package man.kuke.nio.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class BaseCommuication {
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;


    public BaseCommuication(Socket socket) {
        try {
            this.socket = socket;
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            dealPeerDown();
        }
    }

    public abstract void dealPeerDown();

    public void sendMessage(NetMessage netMessage) {
        try {
            dos.writeUTF(netMessage.toString());
        } catch (IOException e) {
            dealPeerDown();
        }
    }

    public abstract void dealNetMessage(NetMessage netMessage);

    public void close() {
        if(socket!= null) {
            if(!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }finally {
                    socket=null;
                }
            }
        }
        if(dis!=null) {
            try {
                dis.close();
            } catch (IOException ignored) {
            } finally {
                dis=null;
            }
        }
        if(dos!=null) {
            try {
                dos.close();
            } catch (IOException ignored) {
            }finally {
                dos=null;
            }
        }
    }

}
