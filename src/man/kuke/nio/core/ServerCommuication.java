package man.kuke.nio.core;

import java.io.IOException;
import java.net.Socket;

public abstract class ServerCommuication extends BaseCommuication {
    protected volatile boolean goon = false;

    public ServerCommuication(Socket socket) {
        super(socket);

    }

    public void close() {
        goon = false;
        super.close();
    }

    public void receive() {
        try {
            int count = dis.available();
            if (count < 1) {
                return;
            }
            String message =  dis.readUTF();
            dealNetMessage(new NetMessage(message));
        } catch (IOException e) {
            if (goon) {
                dealPeerDown();
            }
            close();
        }

    }


}
