package man.kuke.nio.core;

import java.io.IOException;
import java.net.Socket;

public abstract class ClientCommuication extends BaseCommuication implements Runnable {
    protected volatile boolean goon;

    public ClientCommuication(Socket socket) {
        super(socket);
        goon = true;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(goon) {
            try {
                String message =  dis.readUTF();
                dealNetMessage(new NetMessage(message));
            } catch (IOException e) {
                if (goon) {
                    dealPeerDown();
                }
                close();
            }
        }
        close();
    }

    public void close() {
        goon = false;
        super.close();
    }

}
