import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Router {

    private String ipAddress;
    private final String subnetMask = "255.255.255.0";
    private String type;
    private Set<Edge> edges = new HashSet<>();
    private boolean sentUpdate = false;
    private boolean inactive = false;
    private RoutingTable table;

    public Router(String ipAddress) {
        this.ipAddress = ipAddress;
        table = new RoutingTable(this);
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

    public boolean getInactive() {
        return inactive;
    }

    public String getAddress() {
        return ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public String getType() {
        return type;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public RoutingTable getTable() {
        return table;
    }

    public void updateTable(List<Router> routers) {
        table.update(this, routers);
    }

    public void receive(String ip, RoutingTable newTable, List<Router> network) {
        for (int i = 0; i < newTable.getDestinations().size(); i++) {
            int weight = 0;
            for (Edge e : edges) {
                String srcAdress = e.getSrc();
                String destAdress = e.getDest();

                if ((ip.equals(srcAdress) && ipAddress.equals(destAdress))
                        || (ip.equals(destAdress) && ipAddress.equals(srcAdress))) {
                    weight = e.getWeight();
                    break;
                }
            }
            if (!table.getDestinations().contains(newTable.getDestinations().get(i)) && !newTable.getDestinations().get(i).equals(this.ipAddress)) {
                table.addRecord(newTable.getDestinations().get(i), newTable.getMetric().get(i) + weight, ip);
                //System.out.println("new\n"+newTable.getDestinations().get(i)+" "+ipAddress);
                sendTableToNeighbors(ip, network);
            } else {
                int index = table.getDestinations().indexOf(newTable.getDestinations().get(i));
                if (table.getNextHopIP().get(index).equals(ip) && table.getMetric().get(index) != newTable.getMetric().get(i) + weight) {

                    table.getMetric().set(index, newTable.getMetric().get(i) + weight);
                    if (newTable.getMetric().get(i) + weight <= 16)
                        sendTableToNeighbors(ip, network);
                    else
                        table.getMetric().set(index, 16);

                }
                if (table.getMetric().get(index) > newTable.getMetric().get(i) + weight) {
                    table.getMetric().set(index, newTable.getMetric().get(i) + weight);
                    table.getNextHopIP().set(index, ip);
                    sendTableToNeighbors(ip, network);
                }
            }
        }
    }

    public void sendTableToNeighbors(String ip, List<Router> network) {
        for (Edge curEdge : edges) {
            String srcAdress = curEdge.getSrc();
            String destAdress = curEdge.getDest();

            String neighbor;
            if (this.getAddress().equals(srcAdress)) {
                neighbor = destAdress;
            } else {
                neighbor = srcAdress;
            }

            for (Router r : network) {
                if (r.getAddress().equals(neighbor)) {
                    RoutingTable rTable = new RoutingTable(table);
                    try {
                        for (int i = 0; i < rTable.getDestinations().size(); i++) {
                            if (rTable.getNextHopIP().get(i).equals(r.getAddress())) {
                                rTable.getMetric().set(i, 16);
                            }
                        }
                        r.receive(this.getAddress(), rTable, network);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sendPacket(Header header, List<Router> network) {
        System.out.print(this.getAddress());
        if (this.getAddress().equals(header.getRoutingDomain())) {
            System.out.println("Packet arrived!");
            return;
        }
        if (table.getDestinations().contains(header.getRoutingDomain())) {
            int index = table.getDestinations().indexOf(header.getRoutingDomain());
            if (table.getMetric().get(index) >= 16) {
                System.out.println("Destination unreachable");
            }

            for (Router r : network) {
                if (r.getAddress().equals(table.getNextHopIP().get(index))) {
                    System.out.print("->");
                    r.sendPacket(header, network);
                    break;
                }
            }
        } else
            System.out.println("Destination unreachable!");

    }

}
