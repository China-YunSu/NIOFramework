package man.kuke.nio.core;

import com.mec.util.PropertiesParse;
import man.kuke.action.core.processer.DefaultActionProcess;

import java.io.IOException;

public class InitialObject {

    public static void invoke(Initializable object) {
        PropertiesParse parse = new PropertiesParse();
        try {
            parse.loadProperties("/config/NetConfig.properites");
            int port = Integer.parseInt(parse.value("port"));
            String ip = parse.value("ip");
            object.setIp(ip);
            object.setPort(port);
        } catch (IOException ignored) {
        }
        object.setActionProcess(new DefaultActionProcess());
    }
}
