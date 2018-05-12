import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class NetworkGraph {

    private List<Router> routers = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public NetworkGraph() {
        Timer timer = new Timer();
        timer.schedule(new UpdateTimer(routers), 30 * 1000, 30000);
        Timer timer2 = new Timer();
        timer2.schedule(new TimeOutTimer(routers), 180 * 1000, 180000);
        Timer timer3 = new Timer();
        timer3.schedule(new TimerGarbageCollection(routers), 10 * 1000, 1000);
    }

    public void addRouter(String ip) {
        routers.add(new Router(ip));
    }

    public void addEdge(String sourceIP, String destIP, int weight) {
        Router src = null;
        Router dest = null;
        for (Router r : routers) {
            if (r.getAddress().equals(sourceIP)) {
                src = r;
                if (!r.getTable().getNeighbors().contains(destIP)) {
                    r.getTable().addNeighbor(destIP);
                }
            }
            if (r.getAddress().equals(destIP)) {
                if (!r.getTable().getNeighbors().contains(sourceIP)) {
                    r.getTable().addNeighbor(sourceIP);
                }
                dest = r;
            }
        }
        if (src == null || dest == null) {
            System.out.print("Source router or distance router does not exist");
            return;
        }

        Edge e = new Edge(src, dest, weight);
        src.addEdge(e);
        dest.addEdge(e);
        dest.receive(sourceIP, src.getTable(), routers);
        src.receive(destIP, dest.getTable(), routers);

        edges.add(e);
    }

    public Router getRouter(String ip) {
        for (Router r : routers) {
            if (r.getAddress().equals(ip))
                return r;
        }
        return null;
    }

    public void printRouters() {
        for (Router r : routers) {
            System.out.println(r.getAddress());
        }
    }

    public void printEdges() {
        for (Edge e : edges) {
            System.out.println(e.getSrc() + " " + e.getDest() + " " + e.getWeight());
        }
    }

    public void printTable(String ip) {
        for (Router r : routers) {
            if (r.getAddress().equals(ip))
                System.out.print(r.getTable().toString());
        }
    }

    public void startRouter(String ip) {
        for (Router r : routers) {
            if (r.getAddress().equals(ip)) {
                r.updateTable(routers);
            }
        }
    }

    public List<Router> getRouters() {
        return routers;
    }

    public void updateEdge(String src, String dest, int weight) {

        for (Router r : routers) {
            for (Edge e : r.getEdges()) {
                if ((e.getSrc().equals(src) && e.getDest().equals(dest)) || (e.getSrc().equals(dest) && e.getDest().equals(src))) {
                    e.setWeight(weight);
                }
            }
            if (r.getTable().getDestinations().contains(src) && r.getAddress().equals(dest)) {
                int index = r.getTable().getDestinations().indexOf(src);
                r.getTable().getMetric().set(index, weight);
                r.updateTable(routers);
            }
            if (r.getTable().getDestinations().contains(dest) && r.getAddress().equals(src)) {
                int index = r.getTable().getDestinations().indexOf(dest);
                r.getTable().getMetric().set(index, weight);
                r.updateTable(routers);
            }
        }
    }

    public void removeEdge(String src, String dest) {
        for (Router r : routers) {
            for (Edge e : r.getEdges()) {
                if ((e.getSrc().equals(src) && e.getDest().equals(dest)) || (e.getSrc().equals(dest) && e.getDest().equals(src))) {
                    r.getEdges().remove(e);
                    break;
                }
            }
            if (r.getTable().getDestinations().contains(src) && r.getAddress().equals(dest)) {
                int index = r.getTable().getDestinations().indexOf(src);
                r.getTable().getMetric().set(index, 16);
                r.updateTable(routers);

            }
            if (r.getTable().getDestinations().contains(dest) && r.getAddress().equals(src)) {
                int index = r.getTable().getDestinations().indexOf(dest);
                r.getTable().getMetric().set(index, 16);
                r.updateTable(routers);

            }
            if (r.getTable().getNextHopIP().contains(dest) && r.getAddress().equals(src)) {
                int index = r.getTable().getNextHopIP().indexOf(dest);
                while (index != -1) {
                    r.getTable().getMetric().set(index, 16);
                    r.getTable().getNextHopIP().set(index, "0.0.0.0");
                    r.updateTable(routers);
                    index = r.getTable().getNextHopIP().indexOf(dest);
                }

            }
            if (r.getTable().getNextHopIP().contains(src) && r.getAddress().equals(dest)) {
                int index = r.getTable().getNextHopIP().indexOf(src);
                while (index != -1) {
                    r.getTable().getMetric().set(index, 16);
                    r.getTable().getNextHopIP().set(index, "0.0.0.0");
                    r.updateTable(routers);
                    index = r.getTable().getNextHopIP().indexOf(src);
                }
            }
        }
    }

    public void sendPacket(String src, String dest) {
        Router source = null;
        Router destination = null;
        for (Router r : routers) {
            if (r.getAddress().equals(src))
                source = r;
            if (r.getAddress().equals(dest))
                destination = r;
        }
        if (source == null || destination == null)
            System.out.println("Destination is unreachable");
        Header header = new Header(src, dest);
        source.sendPacket(header, routers);
    }
}
