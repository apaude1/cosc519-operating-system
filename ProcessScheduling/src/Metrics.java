import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Metrics {
	
	private int number = 1;
	private int numberOfThreads;
	
	public Metrics(int numberOfThreads) { 
		this.numberOfThreads = numberOfThreads;
	}

	public void displayAccountingInformation(ProcessControlTable processControlTable, int contextSwitchCount, int currentTime, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {
		synchronized(this) {
			while (number % numberOfThreads != threadSequence) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("******************************" + schedulerTypeEnum.toString() + "******************************");
			//System.out.println("Process ID | Arrival Time | Priority | Burst Units | Response Time | Response Ratio (RR) | Penalty (1/RR) | Start Time | Completion Time | Time Waiting | Turn Around Time");
			List<Entry<Integer, ProcessControlBlock>> entries = processControlTable.getListEntrySet();
			Iterator<Entry<Integer, ProcessControlBlock>> iterator = entries.iterator();
			int totalReponseTime = 0;
			int totalWaitTime = 0;
			int totalTurnAroundTime = 0;
			float totalResponseRatioTime = 0;
			float totalPenaltyRate = 0;
			while(iterator.hasNext()) {
				ProcessControlBlock processControlBlock = iterator.next().getValue();		
				totalReponseTime += processControlBlock.getResponseTime(); 
				totalWaitTime += processControlBlock.getWaitTime(); 
				totalTurnAroundTime += processControlBlock.getTurnAroundTime(); 
				totalResponseRatioTime += processControlBlock.getResponseRatioTime(); 
				totalPenaltyRate += (float)1.0 / processControlBlock.getResponseRatioTime(); 
			
//		        System.out.println("   P" + processControlBlock.getPID() 
//		        	+ "\t\t  " + processControlBlock.getArrivalTime()
//		        	+ "\t\t" + processControlBlock.getPriority()
//		        	+ "\t\t" + processControlBlock.getBurstTime()
//	        	    + "\t\t" + processControlBlock.getResponseTime() 
//	        	    + "\t\t" + (float)Math.round(processControlBlock.getResponseRatioTime() * 100.0) / 100.0
//	        	    + "\t\t" + (float)Math.round(((float)1.0 / processControlBlock.getResponseRatioTime()) * 100.0) / 100.0
//	        	    + "\t\t" + processControlBlock.getStartTime() 
//		        	+ "\t\t" + processControlBlock.getCompletionTime() 
//		        	+ "\t\t" + processControlBlock.getWaitTime() 
//		            + "\t\t" + processControlBlock.getTurnAroundTime()); 
			}
			System.out.println("Current Time: " + currentTime);	
			System.out.println("Average response time for       " + entries.size() + " processes: " + (float)Math.round(((float)totalReponseTime / (float)entries.size()) * 100.0) / 100.0); 
		    System.out.println("Average waiting time for        " + entries.size() + " processes: " + (float)Math.round(((float)totalWaitTime / (float)entries.size()) * 100.0) / 100.0); 
		    System.out.println("Average turn around time for    " + entries.size() + " processes: " + (float)Math.round(((float)totalTurnAroundTime / (float)entries.size()) * 100.0) / 100.0); 
		    System.out.println("Average response ratio time for " + entries.size() + " processes: " + (float)Math.round((((float)totalResponseRatioTime / (float)entries.size())) * 100) / 100.0);
		    System.out.println("Average penalty rate for        " + entries.size() + " processes: " + (float)Math.round((((float)totalPenaltyRate / (float)entries.size())) * 100) / 100.0);
		    System.out.println("# of context switches with      " + entries.size() + " processes: " + contextSwitchCount);

			number++;
			this.notifyAll();			
		}
	}
	
	public void displayCurrentEvent(ProcessControlTable processControlTable, int currentTime, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {
		synchronized(this) {
			while (number % numberOfThreads != threadSequence) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("******************************" + schedulerTypeEnum.toString() + "******************************");
			ProcessControlBlock runningProcessControlBlock = processControlTable.getRunningProcessControlBlock();
			System.out.println("Current Time: " + currentTime);		
			if (runningProcessControlBlock != null) {			
				System.out.println("Executing Process: P" + runningProcessControlBlock.getPID() + "; Arrival Time: " + runningProcessControlBlock.getArrivalTime() + "; Remaining Burst Time: " + runningProcessControlBlock.getRemainingBurstTime() + "; Priority: " + runningProcessControlBlock.getPriority());
			}
			else {
				System.out.println("Executing Process: Idle");
			}
			
			number++;
			this.notifyAll();			
		}
	}
	
	public void displayGanttChartQueue(GanttChartQueue ganttChartQueue, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {
		synchronized(this) {
			while (number % numberOfThreads != threadSequence) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			ganttChartQueue.displayGanttChartQueue(schedulerTypeEnum);
			
			number++;
			this.notifyAll();			
		}
	}
	
	public void displayReadyQueue(ReadyQueue readyQueue, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {
		synchronized(this) {
			while (number % numberOfThreads != threadSequence) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			readyQueue.displayReadyQueue(schedulerTypeEnum);
			
			number++;
			this.notifyAll();			
		}
	}
}
