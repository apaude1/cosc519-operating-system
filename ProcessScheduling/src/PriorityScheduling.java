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
		if (currentRunningProcess != null && scheduledProcess != null) {
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);		
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);	
			//context switched
			Helper.contextSwitchCount++;
			
			setUpRunningProcess(scheduledProcess);		
		}
		else if (currentRunningProcess != null && scheduledProcess == null) {
			//continue executing the current process
			scheduledProcess = currentRunningProcess;
		}
		else if (currentRunningProcess == null && scheduledProcess != null) {
			setUpRunningProcess(scheduledProcess);
		}
			
		int remainingBurstTime = scheduledProcess.getRemainingBurstTime();					
		remainingBurstTime--;			
		scheduledProcess.setRemainingBurstTime(remainingBurstTime);				
		if (remainingBurstTime == 0) {				
			scheduledProcess.setCompletionTime(Helper.currentTime);
			scheduledProcess.setTurnAroundTime(Helper.currentTime - scheduledProcess.getArrivalTime() + 1);
			scheduledProcess.setWaitTime(scheduledProcess.getTurnAroundTime() - scheduledProcess.getBurstTime());		
			scheduledProcess.setBurstEndTime(Helper.currentTime);			
			//set the state of the process to terminated
			scheduledProcess.setProcessState(ProcessStateEnum.TERMINATED);
			ganttChartQueue.enqueue(new ProcessControlBlock(scheduledProcess.getPID(), scheduledProcess.getBurstStartTime(), scheduledProcess.getBurstEndTime()));
		}
		Helper.currentTime++;
	}
	
	private ProcessControlBlock getProcessWithHighestPriorityAtCurrentTime() {
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock != null && processControlBlock.getArrivalTime() <= Helper.currentTime) {
			return processControlBlock;
		}		
		return null;
	}
	
	private ProcessControlBlock getProcessWithHigherPriorityAtCurrentTime(int priority) {
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock != null && processControlBlock.getArrivalTime() <= Helper.currentTime && processControlBlock.getPriority() < priority) {
			return processControlBlock;
		}		
		return null;
	}
}