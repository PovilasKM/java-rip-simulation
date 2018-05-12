import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class TimerGarbageCollection extends TimerTask {
	private List<Router> routers = new ArrayList<Router>();
	public TimerGarbageCollection(List<Router> routers){
		this.routers = routers;
	}
    @Override
    public void run() {
    	List<Router> routersToRemove = new ArrayList<Router>();
    	for( Router r : routers){
    		if(r.getInactive()){
    			String address = r.getAddress();
    			for(Router router : routers){
    				int index = router.getTable().getDestinations().indexOf(address);
    				if(index != -1){
    					router.getTable().removeRecord(index);
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