import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {
	private final List<Process> prcs;
	 private final List<Event> prc_timeline;
    private int timeQuantum;
    
    public Scheduler()
    {
    	prcs = new ArrayList<Process>();
    	prc_timeline = new ArrayList<Event>();
        timeQuantum = 1;
    }
    
    public boolean add(Process prc)
    {
        return prcs.add(prc);
    }
    
    public void setTimeQuantum(int timeQuantum)
    {
        this.timeQuantum = timeQuantum;
    }
    
    public int getTimeQuantum()
    {
        return timeQuantum;
    }
    
    public double getAverageWaitingTime()
    {
        double avg = 0.0;
        
        for (Process prc : prcs)
        {
            avg += prc.getWaitingTime();
        }
        
        return avg / prcs.size();
    }
    
    public double getAverageTurnAroundTime()
    {
        double avg = 0.0;
        
        for (Process prc : prcs)
        {
            avg += prc.getTurnaroundTime();
        }
        
        return avg / prcs.size();
    }
    
    public Event getEvent(Process prc)
    {
        for (Event event : prc_timeline)
        {
            if (prc.getProcessName().equals(event.getProcessName()))
            {
                return event;
            }
        }
        
        return null;
    }
    
    public Process getProcess(String process)
    {
        for (Process prc : prcs)
        {
        	 if (prc.getProcessName().equals(process))
            {
                return prc;
            }
        }
        
        return null;
    }
    
    public List<Process> getProcess()
    {
        return prcs;
    }
    
    public List<Event> getTimeline()
    {
        return prc_timeline;
    }
    
    
    
    public abstract void process();
}
