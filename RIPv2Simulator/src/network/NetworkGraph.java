package network;

import timers.InvalidTimer;
import timers.InactiveRemoverTimer;
import timers.UpdateTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class NetworkGraph {

    private List<Router> routers = new ArrayList<>();
    private List<RouterEdge> routerEdges = new ArrayList<>();

    public NetworkGraph() {
        Timer timer = new Timer();
        timer.schedule(new UpdateTimer(routers), 30 * 1000, 30000);
        Timer timer2 = new Timer();
        timer2.schedule(new InvalidTimer(routers), 180 * 1000, 180000);
        Timer timer3 = new Timer();
        timer3.schedule(new InactiveRemoverTimer(routers), 10 * 1000, 1000);
    }

    public void addRouter(String ip) {
        routers.add(new Router(ip));
    }

    public List<Router> getRouters() {
        return routers;
    }

    public void addEdge(String sourceIP, String destIP, int weight) {
        Router src = null;
        Router dest = null;
        for (Router r : routers) {
            if (r.getAddress().equals(sourceIP)) {
                src = r;
                if (!r.getRoutingTable().getNeighbors().contains(destIP)) {
                    r.getRoutingTable().addNeighbor(destIP);
                }
            }
            if (r.getAddress().equals(destIP)) {
                if (!r.getRoutingTable().getNeighbors().contains(sourceIP)) {
                    r.getRoutingTable().addNeighbor(sourceIP);
                }
                dest = r;
            }
        }
        if (src == null || dest == null) {
            System.out.print("Source router or distance router does not exist");
            return;
        }

        RouterEdge e = new RouterEdge(src, dest, weight);
        src.addEdge(e);
        dest.addEdge(e);
        dest.receive(sourceIP, src.getRoutingTable(), routers);
        src.receive(destIP, dest.getRoutingTable(), routers);

        routerEdges.add(e);
    }

    public void printRouters() {
        for (Router r : routers) {
            System.out.println(r.getAddress());
        }
    }

    public void printEdges() {
        for (RouterEdge e : routerEdges) {
            System.out.println(e.getSrc() + " --- " + e.getDest() + " --- " + e.getWeight());
        }
    }

    public void printTable(String ip) {
        for (Router r : routers) {
            if (r.getAddress().equals(ip))
                System.out.print(r.getRoutingTable().toString());
        }
    }

    public void removeEdge(String src, String dest) {
        for (Router router : routers) {
            for (RouterEdge edge : router.getRouterEdges()) {
                if ((edge.getSrc().equals(src) && edge.getDest().equals(dest)) || (edge.getSrc().equals(dest) && edge.getDest().equals(src))) {
                    router.getRouterEdges().remove(edge);
                    routerEdges.remove(edge);
                    break;
                }
            }
            if (router.getRoutingTable().getDestinations().contains(src) && router.getAddress().equals(dest)) {
                int index = router.getRoutingTable().getDestinations().indexOf(src);
                router.getRoutingTable().getMetric().set(index, 16);
                router.updateTable(routers);

            }
            if (router.getRoutingTable().getDestinations().contains(dest) && router.getAddress().equals(src)) {
                int index = router.getRoutingTable().getDestinations().indexOf(dest);
                router.getRoutingTable().getMetric().set(index, 16);
                router.updateTable(routers);

            }
            if (router.getRoutingTable().getNextHopIP().contains(dest) && router.getAddress().equals(src)) {
                //remove source
                int index = router.getRoutingTable().getNextHopIP().indexOf(dest);
                while (index != -1) {
                    router.getRoutingTable().getMetric().set(index, 16);
                    router.getRoutingTable().getNextHopIP().set(index, "0.0.0.0");
                    router.updateTable(routers);
                    index = router.getRoutingTable().getNextHopIP().indexOf(dest);
                }

            }
            if (router.getRoutingTable().getNextHopIP().contains(src) && router.getAddress().equals(dest)) {
                int index = router.getRoutingTable().getNextHopIP().indexOf(src);
                while (index != -1) {
                    int dontAskAboutThis; //don't
                    router.getRoutingTable().getMetric().set(index, 16);
                    router.getRoutingTable().getNextHopIP().set(index, "0.0.0.0");
                    router.updateTable(routers);
                    index = router.getRoutingTable().getNextHopIP().indexOf(src);
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
