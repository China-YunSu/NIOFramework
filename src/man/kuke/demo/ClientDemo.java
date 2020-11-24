package man.kuke.demo;

import man.kuke.nio.core.NIOClient;

import java.io.IOException;

public class ClientDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        NIOClient cilent = new NIOClient();
        cilent.connectTOServer();
        Thread.sleep(100);
        cilent.sendMessage("null","dasd");
        cilent.offline();
    }
}
