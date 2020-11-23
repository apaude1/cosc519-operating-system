import java.util.PriorityQueue;

public class PriorityScheduling extends Scheduling {	
	
	public PriorityScheduling() {
		super(new ReadyQueue(new PriorityQueue<ProcessControlBlock>(Helper.READY_QUEUE_CAPACITY, new ProcessArrivalTimePriorityComparator())));		
	}
		
	@Override
	protected ProcessControlBlock runCPUScheduler() {		
		ProcessControlBlock processControlBlock = processControlTable.getRunningProcessControlBlock();
		if (processControlBlock != null) {
			return getProcessWithHigherPriorityAtCurrentTime(processControlBlock.getPriority());			
		}
		return getProcessWithHighestPriorityAtCurrentTime();
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock scheduledProcess) {		
		//context switch if there is already executing process and new process is scheduled to run
		ProcessControlBlock currentRunningProcess = processControlTable.getRunningProcessControlBlock();
		ProcessControlBlock processControlBlock = null;
		if (currentRunningProcess != null && scheduledProcess != null) {
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);		
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);	
			//context switched
			
			//start executing the scheduled process			
			scheduledProcess.setBurstStartTime(Helper.currentTime);	
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);
			displayCurrentEvent();
			readyQueue.displayReadyQueue();		
			ganttChartQueue.displayGanttChartQueue();			
		}
		else if (currentRunningProcess != null && scheduledProcess == null) {
			//continue executing the current process
			processControlBlock = currentRunningProcess;
		}
		else if (currentRunningProcess == null && scheduledProcess != null) {
			//start executing the scheduled process			
			scheduledProcess.setBurstStartTime(Helper.currentTime);	
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			displayCurrentEvent();
			readyQueue.displayReadyQueue();		
			ganttChartQueue.displayGanttChartQueue();
		}
			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		remainingBurstTime--;			
		processControlBlock.setRemainingBurstTime(remainingBurstTime);				
		if (remainingBurstTime == 0) {				
			processControlBlock.setCompletionTime(Helper.currentTime);
			processControlBlock.setTurnAroundTime(Helper.currentTime - processControlBlock.getArrivalTime() + 1);
			processControlBlock.setWaitTime(processControlBlock.getTurnAroundTime() - processControlBlock.getBurstTime());		
			processControlBlock.setBurstEndTime(Helper.currentTime);			
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
			ganttChartQueue.enqueue(new ProcessControlBlock(processControlBlock.getPID(), processControlBlock.getBurstStartTime(), processControlBlock.getBurstEndTime()));
		}
		Helper.currentTime++;
	}
	
	private ProcessControlBlock getProcessWithHighestPriorityAtCurrentTime() {
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock != null && processControlBlock.getArrivalTime() <= Helper.currentTime) {
			return processControlBlock;
		}		
		return null;
//		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();		
//		int i = 0;
//		ProcessControlBlock processWithHighestPriority = null;
//		while (iterator.hasNext()) {
//			ProcessControlBlock current = iterator.next();		
//			if (i == 0) {
//				processWithHighestPriority = current;
//			}
//			else {
//				if (current.getArrivalTime() <= Helper.currentTime) {	
//					if (current.getPriority() <= processWithHighestPriority.getPriority()) {
//						processWithHighestPriority = current;
//					}
//				}
//			}
//			i++;
//		}
//		return processWithHighestPriority;
	}
	
	private ProcessControlBlock getProcessWithHigherPriorityAtCurrentTime(int priority) {
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock != null && processControlBlock.getArrivalTime() <= Helper.currentTime && processControlBlock.getPriority() < priority) {
			return processControlBlock;
		}		
		return null;
//		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();				
//		while (iterator.hasNext()) {
//			ProcessControlBlock current = iterator.next();
//			if (current.getPriority() < priority && current.getArrivalTime() <= Helper.currentTime) {				
//				return current;
//			}
//		}
//		return null;
	}
}