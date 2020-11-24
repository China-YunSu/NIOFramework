package man.kuke.nio.core;

public interface ISpeaker {
    void addListener(IListener listener);
    void removeListener(IListener listener);
    void publishMessage(String message);
}
