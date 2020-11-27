import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Metrics {
	
	public Metrics() {		
	}

	public void displayAccountingInformation(ProcessControlTable processControlTable, int contextSwitchCount, int currentTime, AlgorithmEnum algorithmEnum) {
		System.out.println("******************************" + algorithmEnum.toString() + "******************************");
		//System.out.println("Process ID | Arrival Time | Priority | Burst Units | Response Time | Completion Time | Time Waiting | Turn Around Time");
		List<Entry<Integer, ProcessControlBlock>> entries = processControlTable.getListEntrySet();
		Iterator<Entry<Integer, ProcessControlBlock>> iterator = entries.iterator();
		int totalReponseTime = 0;
		int totalWaitTime = 0;
		int totalTurnAroundTime = 0;
		int totalCompletedProcesses = 0;
		while(iterator.hasNext()) {
			ProcessControlBlock processControlBlock = iterator.next().getValue();		
			totalReponseTime += processControlBlock.getResponseTime(); 
			totalWaitTime += processControlBlock.getWaitTime(); 
			totalTurnAroundTime += processControlBlock.getTurnAroundTime(); 
			if (processControlBlock.getProcessState() == ProcessStateEnum.TERMINATED) {
				totalCompletedProcesses++;
			}
//	        System.out.println("   P" + processControlBlock.getPID() 
//	        	+ "\t\t  " + processControlBlock.getArrivalTime()
//	        	+ "\t\t" + processControlBlock.getPriority()
//	        	+ "\t\t" + processControlBlock.getBurstTime()
//        	    + "\t\t" + processControlBlock.getResponseTime() 
//	        	+ "\t\t" + processControlBlock.getCompletionTime() 
//	        	+ "\t\t" + processControlBlock.getWaitTime() 
//	            + "\t\t" + processControlBlock.getTurnAroundTime()); 
		}
		System.out.println("Current Time: " + currentTime);	
		System.out.println("Average response time of    " + entries.size() + " processes: " + (float)totalReponseTime / (float)entries.size()); 
	    System.out.println("Average waiting time of     " + entries.size() + " processes: " + (float)totalWaitTime / (float)entries.size()); 
	    System.out.println("Average turn around time    " + entries.size() + " processes: " + (float)totalTurnAroundTime / (float)entries.size());
	    System.out.println("CPU throughput for          " + entries.size() + " processes: " + (float)totalCompletedProcesses / (float)currentTime);
	    System.out.println("# of context switches with  " + entries.size() + " processes: " + contextSwitchCount);
	}
	
	public void displayCurrentEvent(ProcessControlTable processControlTable, int currentTime) {
		System.out.println("******************************");
		ProcessControlBlock runningProcessControlBlock = processControlTable.getRunningProcessControlBlock();
		System.out.println("Current Time: " + currentTime);		
		if (runningProcessControlBlock != null) {			
			System.out.println("Executing Process: P" + runningProcessControlBlock.getPID() + "; Arrival Time: " + runningProcessControlBlock.getArrivalTime() + "; Remaining Burst Time: " + runningProcessControlBlock.getRemainingBurstTime() + "; Priority: " + runningProcessControlBlock.getPriority());
		}
		else {
			System.out.println("Executing Process: Idle");
		}
	}
	
	public void displayGanttChartQueue(GanttChartQueue ganttChartQueue, AlgorithmEnum algorithmEnum) {
		ganttChartQueue.displayGanttChartQueue(algorithmEnum);
	}
	
	public void displayReadyQueue(ReadyQueue readyQueue, AlgorithmEnum algorithmEnum) {
		readyQueue.displayReadyQueue(algorithmEnum);
	}
}
