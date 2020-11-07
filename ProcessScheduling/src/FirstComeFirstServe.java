import java.util.Collections;
import java.util.List;

public class FirstComeFirstServe extends Scheduler
{
	   @Override
	    public void process()
	    {        
	        Collections.sort(this.getProcess(), (Object o1, Object o2) -> {
	            if (((Process) o1).getArrivalTime() == ((Process) o2).getArrivalTime())
	            {
	                return 0;
	            }
	            else if (((Process) o1).getArrivalTime() < ((Process) o2).getArrivalTime())
	            {
	                return -1;
	            }
	            else
	            {
	                return 1;
	            }
	        });
	        
	        List<Event> timeline = this.getTimeline();
	        
	        for (Process prc : this.getProcess())
	        {
	            if (timeline.isEmpty())
	            {
	                timeline.add(new Event(prc.getPID(), prc.getProcessName(), prc.getArrivalTime(), prc.getArrivalTime() + prc.getBurstTime()));
	            }
	            else
	            {
	                Event event = timeline.get(timeline.size() - 1);
	                timeline.add(new Event(prc.getPID(), prc.getProcessName(), event.getFinishTime(), event.getFinishTime() + prc.getBurstTime()));
	            }
	        }
	        
	        for (Process prc : this.getProcess())
	        {
	        	prc.setWaitingTime(this.getEvent(prc).getStartTime() - prc.getArrivalTime());
	        	prc.setTurnaroundTime(prc.getWaitingTime() + prc.getBurstTime());
	        }
	    }

}
