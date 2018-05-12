package network;

public class Header {
    private int command;
    private String version = "RIPv2";
    private String address_family = "AF_INET";
    private String ipAddress;
    private String routingDomain;

    public Header(String ipAddress, String routingDomain) {
        this.routingDomain = routingDomain;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getRoutingDomain() {
        return routingDomain;
    }
}
