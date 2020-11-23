import java.util.Queue;

// Implement Job Queue using Priority Queue
public class JobQueue extends ProcessQueue {
				
	public JobQueue(Queue<ProcessControlBlock> processQueue) {
		super(processQueue);
	}	
	
	@Override
	public int getAvailableCapacity() {
		return Helper.JOB_QUEUE_CAPACITY - processQueue.size();
	}
	
	@Override
	public boolean isBelowThresholdCapacity() {
		double percentageAvailableCapacity = processQueue.size() / (float)Helper.JOB_QUEUE_CAPACITY;
		if (percentageAvailableCapacity < Helper.THRESHOLD_CAPACITY)
			return true; 
		else
			return false;
	}
}
