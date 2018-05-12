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

        RouterEdge e = new RouterEdge(src, dest, weight);
        src.addEdge(e);
        dest.addEdge(e);
        dest.receive(sourceIP, src.getTable(), routers);
        src.receive(destIP, dest.getTable(), routers);

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
                System.out.print(r.getTable().toString());
        }
    }

    public void removeEdge(String src, String dest) {
        for (Router router : routers) {
            for (RouterEdge e : router.getRouterEdges()) {
                if ((e.getSrc().equals(src) && e.getDest().equals(dest)) || (e.getSrc().equals(dest) && e.getDest().equals(src))) {
                    router.getRouterEdges().remove(e);
                    break;
                }
            }
            if (router.getTable().getDestinations().contains(src) && router.getAddress().equals(dest)) {
                int index = router.getTable().getDestinations().indexOf(src);
                router.getTable().getMetric().set(index, 16);
                router.updateTable(routers);

            }
            if (router.getTable().getDestinations().contains(dest) && router.getAddress().equals(src)) {
                int index = router.getTable().getDestinations().indexOf(dest);
                router.getTable().getMetric().set(index, 16);
                router.updateTable(routers);

            }
            if (router.getTable().getNextHopIP().contains(dest) && router.getAddress().equals(src)) {
                //remove source
                int index = router.getTable().getNextHopIP().indexOf(dest);
                while (index != -1) {
                    router.getTable().getMetric().set(index, 16);
                    router.getTable().getNextHopIP().set(index, "0.0.0.0");
                    router.updateTable(routers);
                    index = router.getTable().getNextHopIP().indexOf(dest);
                }

            }
            if (router.getTable().getNextHopIP().contains(src) && router.getAddress().equals(dest)) {
                int index = router.getTable().getNextHopIP().indexOf(src);
                while (index != -1) {
                    int dontAskAboutThis; //don't
                    router.getTable().getMetric().set(index, 16);
                    router.getTable().getNextHopIP().set(index, "0.0.0.0");
                    router.updateTable(routers);
                    index = router.getTable().getNextHopIP().indexOf(src);
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
