import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class UpdateTimer extends TimerTask {
	private List<Router> routers;
	public UpdateTimer(List<Router> routers){
		this.routers = routers;
	}
    @Override
    public void run() {
    	for( Router r : routers){
			r.updateTable(routers);
			
			//System.out.println(r.getTable().toString());
			//System.out.println("\n");
		}
    
    }
}