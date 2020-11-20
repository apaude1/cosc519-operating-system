// Implement Job Queue using Linked List
public class ReadyQueue extends ProcessQueue {
			
	public ReadyQueue() {
		super();
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
}
