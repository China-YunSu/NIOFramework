package man.kuke.nio.core;

public class NetMessage {
    private String message;
    private String action;
    private NetProtocol protocol;
    private String from;

    public NetMessage() {
    }

    public NetMessage(String message) {
        int index = message.indexOf("&");
        protocol = NetProtocol.valueOf(message.substring(0,index));

        String substr = message.substring(index + 1);
        index = substr.indexOf("&");

        from  = substr.substring(0,index);

        substr = substr.substring(index + 1);
        index = substr.indexOf("&");

        this.message = substr.substring(index + 1);
    }

    public String getMessage() {
        return message;
    }

    public NetMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getAction() {
        return action;
    }

    public NetMessage setAction(String action) {
        this.action = action;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public NetMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public NetProtocol getProtocol() {
        return protocol;
    }

    public NetMessage setProtocol(NetProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public String toString() {
        return protocol + "&" + from + "&" + action + "&" + message;
    }


}

