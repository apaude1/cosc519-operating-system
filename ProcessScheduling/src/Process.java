//
public class Process {
	
	private int pid;
	private String processName;
	private int arrivalTime;
	private int burstTime;
	private int priority;
    private int waitingTime;
    private int turnaroundTime;
	
	public Process(int pid, String processName, int arrivalTime, int burstTime, int priority) { 	      
        this.pid = pid; 
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority; 
    } 
	
    public Process(int pid, String processName, int arrivalTime, int burstTime)
    {
        this(pid, processName, arrivalTime, burstTime, 0);
    }
      
	public int getPID(){
		return pid; //get Process ID
	}	
	
    public String getProcessName()
    {
        return this.processName;
    }
	
	public int getArrivalTime(){
		return arrivalTime; //get arrival time of process
	}
	
	public int getBurstTime(){
		return burstTime; //get burst time of process
	}
	
	public int getPriority(){
		return priority; //get priority of process
	}
	
    public int getWaitingTime()
    {
        return this.waitingTime; //get waiting time
    }
    
    public int getTurnaroundTime()
    {
        return this.turnaroundTime; //get turn around time
    }
    
    public void setBurstTime(int burstTime)
    {
        this.burstTime = burstTime; //set burstTime
    }
    
    public void setWaitingTime(int waitingTime)
    {
        this.waitingTime = waitingTime; //setWaiting Time
    }
    
    public void setTurnaroundTime(int turnaroundTime)
    {
        this.turnaroundTime = turnaroundTime; //Set turn around time
    }
}
