//
public class Process {
	
	private int pid;
	private int arrivalTime;
	private int burstTime;
	private int priority;
	
	public Process(int pid, int arrivalTime, int burstTime, int priority) { 	      
        this.pid = pid; 
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority; 
    } 
      
	public int getPID(){
		return pid; //get Process ID
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
}
