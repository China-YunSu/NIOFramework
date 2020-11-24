package man.kuke.nio.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientPool {
    private Map<String,ServerConversation> clientPool;

    {
        clientPool = new ConcurrentHashMap<>();
    }

    public void addClient(ServerConversation client) {
        if (!clientPool.containsKey(client.getClientId()))
        clientPool.put(client.getClientId(),client);
    }

    public ServerConversation getClient(String id) {
        return clientPool.get(id);
    }

    public void remove(ServerConversation client) {
       clientPool.remove(client.getClientId());
    }

    public void doSomethingForClients(IClientPoolAction action) {
        for (ServerConversation client : clientPool.values()) {
            action.methodinvoke(client);
        }
    }

    public void removeAll() {
        clientPool.clear();
    }

    public boolean isEmpty() {
        return clientPool.isEmpty();
    }
}
