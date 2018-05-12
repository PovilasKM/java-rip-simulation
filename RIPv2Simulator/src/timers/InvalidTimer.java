package timers;

import network.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class InvalidTimer extends TimerTask {
    private List<Router> routers = new ArrayList<>();

    public InvalidTimer(List<Router> routers) {
        this.routers = routers;
    }

    @Override
    public void run() {
        for (Router router : routers) {
            if (!router.getSentUpdate())
                router.setInactive(true);
        }

    }
}