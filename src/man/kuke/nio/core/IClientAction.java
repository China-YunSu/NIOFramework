package man.kuke.nio.core;

public interface IClientAction {
    boolean ensureOffline();
    void serverForceDown();
    void afterOffline();
    void serverAbnormalDrop();
    void connectOutTime();
}
