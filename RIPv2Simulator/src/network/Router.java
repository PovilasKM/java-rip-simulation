package network;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Router {

    private String ipAddress;
    private final String subnetMask = "255.255.255.0";
    private Set<RouterEdge> routerEdges = new HashSet<>();
    private boolean sentUpdate = false;
    private boolean inactive = false;
    private RoutingTable routingTable;

    public Router(String ipAddress) {
        this.ipAddress = ipAddress;
        routingTable = new RoutingTable(this);
    }

    public void setSentUpdate(boolean status) {
        sentUpdate = status;
    }

    public boolean getSentUpdate() {
        return this.sentUpdate;
    }

    public void setInactive(boolean status) {
        this.inactive = status;
    }

    public boolean isInactive() {
        return inactive;
    }

    public String getAddress() {
        return ipAddress;
    }

    public Set<RouterEdge> getRouterEdges() {
        return routerEdges;
    }

    public void addEdge(RouterEdge routerEdge) {
        routerEdges.add(routerEdge);
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void updateTable(List<Router> routers) {
        routingTable.update(this, routers);
    }

    public void receive(String ip, RoutingTable newTable, List<Router> network) {
        for (int i = 0; i < newTable.getDestinations().size(); i++) {
            int weight = 0;
            for (RouterEdge e : routerEdges) {
                String srcAdress = e.getSrc();
                String destAdress = e.getDest();

                if ((ip.equals(srcAdress) && ipAddress.equals(destAdress))
                        || (ip.equals(destAdress) && ipAddress.equals(srcAdress))) {
                    weight = e.getWeight();
                    break;
                }
            }

            if (!routingTable.getDestinations().contains(newTable.getDestinations().get(i)) && !newTable.getDestinations().get(i).equals(this.ipAddress)) {
                routingTable.addRecord(newTable.getDestinations().get(i), newTable.getMetric().get(i) + weight, ip);
                sendTableToNeighbors(ip, network);
            } else {
                int index = routingTable.getDestinations().indexOf(newTable.getDestinations().get(i));
                if (routingTable.getNextHopIP().get(index).equals(ip) && routingTable.getMetric().get(index) != newTable.getMetric().get(i) + weight) {

                    routingTable.getMetric().set(index, newTable.getMetric().get(i) + weight);
                    if (newTable.getMetric().get(i) + weight <= 16)
                        sendTableToNeighbors(ip, network);
                    else
                        routingTable.getMetric().set(index, 16);

                }

                if (routingTable.getMetric().get(index) > newTable.getMetric().get(i) + weight) {
                    routingTable.getMetric().set(index, newTable.getMetric().get(i) + weight);
                    routingTable.getNextHopIP().set(index, ip);
                    sendTableToNeighbors(ip, network);
                }
            }
        }
    }

    public void sendTableToNeighbors(String ip, List<Router> network) {
        for (RouterEdge curRouterEdge : routerEdges) {
            String srcAdress = curRouterEdge.getSrc();
            String destAdress = curRouterEdge.getDest();

            String neighborAddress;
            if (this.getAddress().equals(srcAdress)) {
                neighborAddress = destAdress;
            } else {
                neighborAddress = srcAdress;
            }

            for (Router router : network) {
                if (router.getAddress().equals(neighborAddress)) {
                    RoutingTable rTable = new RoutingTable(routingTable);
                    try {
                        for (int i = 0; i < rTable.getDestinations().size(); i++) {
                            if (rTable.getNextHopIP().get(i).equals(router.getAddress())) {
                                rTable.getMetric().set(i, 16);
                            }
                        }
                        router.receive(this.getAddress(), rTable, network);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sendPacket(Header header, List<Router> network) {
        if (header == null || network == null) {
            System.out.println("Error sending packet");
            return;
        }

        System.out.print(this.getAddress());
        if (this.getAddress().equals(header.getRoutingDomain())) {
            System.out.println(" : The packet has arrived!");
            return;
        }

        if (routingTable.getDestinations().contains(header.getRoutingDomain())) {
            int index = routingTable.getDestinations().indexOf(header.getRoutingDomain());
            if (routingTable.getMetric().get(index) >= 16) {
                System.out.println("Destination is unreachable");
            }

            for (Router router : network) {
                if (router.getAddress().equals(routingTable.getNextHopIP().get(index))) {
                    System.out.print("-->");
                    router.sendPacket(header, network);
                    break;
                }
            }
        } else
            System.out.println("Destination unreachable!");

    }

}
