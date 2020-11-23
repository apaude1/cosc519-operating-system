import java.util.Iterator;
import java.util.Queue;

//Implement Job Queue using Linked List
public class GanttChartQueue extends ProcessQueue {
				
	public GanttChartQueue(Queue<ProcessControlBlock> processQueue) {
		super(processQueue);
	}	
	
	@Override
	public int getAvailableCapacity() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isBelowThresholdCapacity() {
		double percentageAvailableCapacity = processQueue.size() / (float)Helper.JOB_QUEUE_CAPACITY;
		if (percentageAvailableCapacity < Helper.THRESHOLD_CAPACITY)
			return true; 
		else
			return false;
	}
	
	public void displayGanttChartQueue() {
		Iterator<ProcessControlBlock> iterator = processQueue.iterator();		
		int previousProcessBurstEndTime = 0;
		System.out.print("Gantt Chart: ");
		while (iterator.hasNext()) {
			ProcessControlBlock processControlBlock = iterator.next();
			int burstStartTime = processControlBlock.getBurstStartTime();			
			if (previousProcessBurstEndTime == burstStartTime) {
				System.out.print("|" + previousProcessBurstEndTime + "    P" + processControlBlock.getPID() + "    " + processControlBlock.getBurstEndTime());					
			}
			else {
				System.out.print("|" + previousProcessBurstEndTime + "  Idle  " + (processControlBlock.getBurstStartTime() - 1) + "|" + processControlBlock.getBurstStartTime() + "    P" + processControlBlock.getPID() + "    " + processControlBlock.getBurstEndTime());
			}
			previousProcessBurstEndTime = processControlBlock.getBurstEndTime() + 1;
	    }
		System.out.print("|");
		System.out.println();
	}
}
