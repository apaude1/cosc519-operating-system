import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class ProcessControlTable {
	
	private LinkedHashMap<Integer, ProcessControlBlock> processControlTable;
	
	public ProcessControlTable() { 	      
		processControlTable = new LinkedHashMap<Integer, ProcessControlBlock>();
    } 
	
	public void add(int pid, ProcessControlBlock processControlBlock) {
		processControlTable.put(pid, processControlBlock);
	}

	public void displayAccountingInformation() {
		System.out.println("Number of context switches: " + Helper.contextSwitchCount);
		System.out.println("******************************");
		System.out.println("Process ID | Arrival Time | Priority | Burst Units | Burst Start Time | Burst End Time | Time Waiting | Turn Around Time");
		List<Entry<Integer, ProcessControlBlock>> entries = getListEntrySet();
		Iterator<Entry<Integer, ProcessControlBlock>> iterator = entries.iterator();
		int totalWaitTime = 0;
		int totalTurnAroundTime = 0;
		while(iterator.hasNext()) {
			ProcessControlBlock processControlBlock = iterator.next().getValue();		
			totalWaitTime += processControlBlock.getWaitTime(); 
			totalTurnAroundTime += processControlBlock.getTurnAroundTime(); 
	        System.out.println("   P" + processControlBlock.getPID() 
	        	+ "\t\t  " + processControlBlock.getArrivalTime()
	        	+ "\t\t" + processControlBlock.getPriority()
	        	+ "\t\t" + processControlBlock.getBurstTime() 
	        	+ "\t\t" + processControlBlock.getBurstStartTime() 
	        	+ "\t\t" + processControlBlock.getBurstEndTime() 
	        	+ "\t\t" + processControlBlock.getWaitTime() 
	            + "\t\t" + processControlBlock.getTurnAroundTime()); 
		}
		
	    System.out.println("Average waiting time = " + (float)totalWaitTime / (float)entries.size()); 
	    System.out.println("Average turn around time = " + (float)totalTurnAroundTime / (float)entries.size()); 
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

	private List<Entry<Integer, ProcessControlBlock>> getListEntrySet() {
		List<Entry<Integer, ProcessControlBlock>> entries = new ArrayList<Entry<Integer, ProcessControlBlock>>(processControlTable.entrySet());
		Collections.sort(entries, new ProcessControlTableComparator());		
		return entries;
	}
}
