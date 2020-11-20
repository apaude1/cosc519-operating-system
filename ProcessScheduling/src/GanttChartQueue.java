//Implement Job Queue using Linked List
public class GanttChartQueue extends ProcessQueue {
				
	public GanttChartQueue() {
		super();
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
