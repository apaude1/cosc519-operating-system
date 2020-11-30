import java.util.Iterator;
import java.util.Queue;

// Implement Job Queue using Linked List
public class ReadyQueue extends ProcessQueue {
		
	public ReadyQueue(Queue<ProcessControlBlock> processQueue) {
		super(processQueue);
	}
			
	@Override
	public int getAvailableCapacity() {
		return Helper.READY_QUEUE_CAPACITY - processQueue.size();
	}
	
	@Override
	public boolean isBelowThresholdCapacity() {
		double percentageAvailableCapacity = processQueue.size() / (float)Helper.READY_QUEUE_CAPACITY;
		if (percentageAvailableCapacity < Helper.THRESHOLD_CAPACITY)
			return true; 
		else
			return false;
	}
	
	public void displayReadyQueue(SchedulerTypeEnum schedulerTypeEnum) {				
		Iterator<ProcessControlBlock> iterator = processQueue.iterator();
		int i = 0;
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				ProcessControlBlock processControlBlock = iterator.next();
				if (i == 0) {
					System.out.print(schedulerTypeEnum.toString() + " Ready Queue: |  P" + processControlBlock.getPID());
				}	
				else {
					System.out.print("  |  P" + processControlBlock.getPID());
				}
		        i++;
		    }
			System.out.println("  |");
		}
		else {
			System.out.println("Ready Queue: Empty");
		}
	}
}
