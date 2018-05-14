package timers;

import network.Router;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class InactiveRemoverTimer extends TimerTask {
	private List<Router> routers = new ArrayList<>();
	public InactiveRemoverTimer(List<Router> routers){
		this.routers = routers;
	}
    @Override
    public void run() {
    	List<Router> routersToRemove = new ArrayList<>();
    	for( Router r : routers){
    		if(r.isInactive()){
    			String address = r.getAddress();
    			for(Router router : routers){
    				int index = router.getRoutingTable().getDestinations().indexOf(address);
    				if(index != -1){
    					router.getRoutingTable().removeRecord(index);
    				}
    			}
    			routersToRemove.add(r);
    			break;
    		}
    	}
    	for(Router r : routersToRemove){
    		routers.remove(r);
    	}
    }
}