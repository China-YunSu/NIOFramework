package man.kuke.nio.core;

import com.mec.didadida.core.ITimeWorker;
import com.mec.didadida.core.Timer;
import man.kuke.action.core.IActionProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServer extends InitializeAdpter implements ISpeaker, Runnable {
    private int port;
    private ServerSocket serverSocket;
    private final List<IListener> listeners;
    private volatile boolean goon;
    private ExecutorService threadPool;
    final String SERVER = "SERVER";
    private final ClientPool clientPool;
    private IActionProcess actionProcess;
    private Timer timer;

    {
        InitialObject.invoke(this);
        listeners = new LinkedList<>();
        clientPool = new ClientPool();
    }


    public NIOServer(int port) {
        this.port = port;
    }

    public NIOServer() {
    }
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public IActionProcess getActionProcess() {
        return actionProcess;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setActionProcess(IActionProcess actionProcess) {
        this.actionProcess = actionProcess;
    }

    public void fireWoke() throws IOException {
        if (goon) {
            publishMessage("服务器已启动。。。。");
            return;
        }

        if (!clientPool.isEmpty()) {
            publishMessage("尚有客户端连接。。。。");
            return;
        }

        if (actionProcess == null)

        publishMessage("服务器启动。。。。");
        serverSocket = new ServerSocket(port);

        goon = true;
        threadPool = Executors.newCachedThreadPool();
        timer = new Timer(new ClientAliveMonitor());
        timer.start();
        publishMessage("客户端连接监视器已启动");
        new Thread(new ClientMessageListener()).start();
        publishMessage("客户端消息侦听已启动");
        new Thread(this).start();
    }

    public void remove(ServerConversation client) {
        clientPool.remove(client);
    }

    public boolean shutdown() {
        if (!clientPool.isEmpty()) {
            publishMessage("尚有客户端连接");
            return false;
        }

        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    serverSocket = null;
                }
            }
        }
        goon = false;
        timer.stop();
        publishMessage("服务器已关闭");
        return true;
    }

    public void sendMessage(String id, String action,String message) {
        ServerConversation client = clientPool.getClient(id);
        if (client == null) {
            try {
                throw new NoSuchClientException("client : id");
            } catch (NoSuchClientException e) {
                e.printStackTrace();
            }
            return;
        }
        client.sendMessage(action,message);
    }

    public void sendMessage( String action,String message) {
       clientPool.doSomethingForClients(new IClientPoolAction() {
           @Override
           public void methodinvoke(ServerConversation client) {
               client.sendMessage(action,message);
           }
       });
    }

    public void sendMessage(List<String> ids, String action,String message) {
        for (String id : ids) {
            sendMessage(id,action,message);
        }
    }

    public void addClient(ServerConversation client) {
        clientPool.addClient(client);
    }

    public void forceDown() {
        clientPool.doSomethingForClients(new IClientPoolAction() {
            @Override
            public void methodinvoke(ServerConversation client) {
                client.offline();
            }
        });

        clientPool.removeAll();
        threadPool.shutdown();
        publishMessage("已断开所有客户端连接");
    }

    @Override
    public void addListener(IListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(IListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void publishMessage(String message) {
        for (IListener listener : listeners) {
            listener.receiveMessage(message);
        }
    }

    @Override
    public void run() {
            publishMessage("服务器开始接收客户端。。。。");
            while (goon) {
                try {
                    Socket socket = serverSocket.accept();
                    publishMessage("连接到客户端" + "[" + socket.getInetAddress().getHostAddress() + "]");
                    ServerConversation client = new ServerConversation(this,socket);
                    threadPool.submit(new Police(client, NIOServer.this));
                } catch (IOException e) {
                    //处理过于粗暴
                    goon=false;
                    forceDown();
                    shutdown();
                }
            }
    }

    class ClientAliveMonitor implements ITimeWorker {
        @Override
        public void runTask() {
            if(clientPool.isEmpty()) {
                Thread.yield();
            }

            clientPool.doSomethingForClients(new IClientPoolAction() {
                @Override
                public void methodinvoke(ServerConversation client) {
                    if (client.isOnline()) {
                        client.sayHello();
                    }
                }
            });

            clientPool.doSomethingForClients(new IClientPoolAction() {
                @Override
                public void methodinvoke(ServerConversation client) {
                    if (!client.isOnline()) {
                       clientPool.remove(client);
                    }
                }
            });
        }
    }

    class ClientMessageListener implements Runnable {
        IClientPoolAction clientAction =  new IClientPoolAction() {
            @Override
            public void methodinvoke(ServerConversation client) {
                if (!client.isOnline()) {
                    return;
                }
                client.receive();
            }
        };

        @SuppressWarnings("BusyWait")
        @Override
        public void run() {
            System.out.println("消息监听已开启");
            while (NIOServer.this.goon) {
                clientPool.doSomethingForClients(this.clientAction);
                // 这里值得思考。sleep()的目的是为了让其它线程能对clientPool
                // 进行操作。
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("消息监听已关闭");
        }
    }

}
