package man.kuke.demo;

import man.kuke.nio.core.IListener;
import man.kuke.nio.core.NIOServer;

import java.io.IOException;
import java.util.Scanner;

public class ServerDemo implements IListener {
    public static void main(String[] args) throws IOException {
        NIOServer server = new NIOServer();
        server.addListener(new ServerDemo());

        Scanner in = new Scanner(System.in);
        boolean status = false;
        while(!status) {
            String cmd = in.nextLine();
            if (cmd.equalsIgnoreCase("ex")) {
                status = server.shutdown();
            } else if (cmd.equalsIgnoreCase("fd")) {
                server.forceDown();
            } else if(cmd.equalsIgnoreCase("st")) {
                server.fireWoke();
            }
        }

        System.out.println("finished");
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(message);
    }
}
