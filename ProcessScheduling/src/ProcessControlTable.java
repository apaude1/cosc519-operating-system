import java.util.LinkedHashMap;
import java.util.Set;

public class ProcessControlTable {
	
	private LinkedHashMap<Integer, ProcessControlBlock> processControlTable;
	
	public ProcessControlTable() { 	      
		processControlTable = new LinkedHashMap<Integer, ProcessControlBlock>();
    } 
	
	public void add(int pid, ProcessControlBlock processControlBlock) {
		processControlTable.put(pid, processControlBlock);
	}
		
	public ProcessControlBlock getProcessControlBlockByProcessId(int pid) {
		return processControlTable.get(pid);
	}
	
	public ProcessControlBlock getRunningProcessControlBlock() {
		Set<Integer> keys = processControlTable.keySet();
		for(Integer key: keys) {
			ProcessControlBlock processControlBlock = processControlTable.get(key);
			if (processControlBlock.getProcessState() == ProcessStateEnum.RUNNING) {
				return processControlBlock;				
			}
		}
		return null;
	}
	
	public void displayAccountingInformation() {
		System.out.println("Process ID | Arrival Time | Completion Time | Burst Time | Waiting Time | Turn Around Time"); 
		Set<Integer> keys = processControlTable.keySet();
		int totalWaitTime = 0;
		int totalTurnAroundTime = 0;
		for(Integer key: keys) {
			ProcessControlBlock processControlBlock = processControlTable.get(key);			
			totalWaitTime += processControlBlock.getWaitTime(); 
			totalTurnAroundTime += processControlBlock.getTurnAroundTime(); 
	        System.out.println(" " + processControlBlock.getPID() 
	        	+ "\t\t" + processControlBlock.getArrivalTime()
	        	+ "\t\t" + processControlBlock.getCompletionTime()
	        	+ "\t\t" + processControlBlock.getBurstTime() 
	        	+ "\t\t" + processControlBlock.getWaitTime() 
	            + "\t\t" + processControlBlock.getTurnAroundTime()); 
		}
		
	    System.out.println("Average waiting time = " + (float)totalWaitTime / (float)keys.size()); 
	    System.out.println("Average turn around time = " + (float)totalTurnAroundTime / (float)keys.size()); 
	}
}
