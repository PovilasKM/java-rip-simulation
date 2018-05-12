package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoutingTable implements Cloneable {
    private List<String> destinationIP = new ArrayList<>();
    private List<Integer> metric = new ArrayList<>();
    private List<String> nextHopIP = new ArrayList<>();
    private List<String> neighbors = new ArrayList<>();
    private String subnetMask = "255.255.255.0";
    private Router curRouter;

    public RoutingTable(RoutingTable table) {
        this.destinationIP = new ArrayList<>(table.destinationIP);
        this.metric = new ArrayList<>(table.metric);
        this.nextHopIP = new ArrayList<>(table.nextHopIP);
        this.neighbors = new ArrayList<>(table.neighbors);
        this.curRouter = table.curRouter;

    }

    public RoutingTable(Router r) {
        curRouter = r;
        addRecord(curRouter.getAddress(), 0, "0.0.0.0");
    }

    public void addNeighbor(String ip) {
        neighbors.add(ip);
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public List<String> getDestinations() {
        return destinationIP;
    }

    public List<Integer> getMetric() {
        return metric;
    }

    public List<String> getNextHopIP() {
        return nextHopIP;
    }

    public void addRecord(String destination, int r_metric, String nextrouter) {
        destinationIP.add(destination);
        metric.add(r_metric);
        nextHopIP.add(nextrouter);
    }

    public void removeRecord(int index) {
        destinationIP.remove(index);
        metric.remove(index);
        nextHopIP.remove(index);
    }

    public String toString() {
        String s = "Destination\tNext hop\tMetric\n";
        for (int i = 0; i < destinationIP.size(); i++) {
            s += destinationIP.get(i) + "\t" + nextHopIP.get(i) + "\t" + metric.get(i) + "\n";
        }
        return s;
    }

    public void update(Router router, List<Router> network) {
        curRouter = router;
        router.setSentUpdate(false);

        Set<RouterEdge> neighborsRouterEdges = curRouter.getRouterEdges();
        for (RouterEdge curRouterEdge : neighborsRouterEdges) {
            String srcAdress = curRouterEdge.getSrc();
            String destAdress = curRouterEdge.getDest();

            String neighbor;
            if (router.getAddress().equals(srcAdress)) {
                neighbor = destAdress;
            } else {
                neighbor = srcAdress;
            }

            for (Router r : network) {
                if (r.getAddress().equals(neighbor)) {
                    router.setSentUpdate(true);
                    r.receive(curRouter.getAddress(), this, network);
                    break;
                }
            }
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

