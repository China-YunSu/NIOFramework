package man.kuke.nio.core;

import man.kuke.action.core.IActionProcess;

public interface Initializable {
    void setIp(String ip);
    void setPort(int port);
    void setActionProcess(IActionProcess actionProcess);
}
