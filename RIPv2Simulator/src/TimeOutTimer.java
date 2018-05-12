import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class TimeOutTimer extends TimerTask {
	private List<Router> routers = new ArrayList<>();
	public TimeOutTimer(List<Router> routers){
		this.routers = routers;
	}
    @Override
    public void run() {
    	for( Router r : routers){
			if(!r.getSentUpdate())
				r.setInactive(true);
    	}
    
    }
}