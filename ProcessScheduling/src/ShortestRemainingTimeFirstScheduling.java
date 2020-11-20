import java.util.Iterator;

public class ShortestRemainingTimeFirstScheduling extends Scheduling {	
		
	public ShortestRemainingTimeFirstScheduling() {
		super();
	}
		
	@Override
	protected ProcessControlBlock runCPUScheduler() {		
		ProcessControlBlock processControlBlock = processControlTable.getRunningProcessControlBlock();
		if (processControlBlock != null) {
			return getProcessWithLowerRemainingBurstTimeAtCurrentTime(processControlBlock.getRemainingBurstTime());
		}
		return getProcessWithLowestRemainingBurstTimeAtCurrentTime();
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock scheduledProcess) {		
		//context switch if there is already executing process and new process is scheduled to run
		ProcessControlBlock currentRunningProcess = processControlTable.getRunningProcessControlBlock();
		ProcessControlBlock processControlBlock = null;		
		if (currentRunningProcess != null && scheduledProcess != null) {	
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);		
			currentRunningProcess.setBurstEndTime(Helper.currentTime);
			ganttChartQueue.enqueue(currentRunningProcess);
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);
				
			//start executing the scheduled process
			startTime = Helper.currentTime;
			scheduledProcess.setBurstStartTime(startTime);	
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);
			displayCurrentEvent();
			displayReadyQueue();		
			displayGanttChartQueue();
		}
		else if (currentRunningProcess != null && scheduledProcess == null) {
			//continue executing the current process
			processControlBlock = currentRunningProcess;
		}
		else if (currentRunningProcess == null && scheduledProcess != null) {
			//start executing the scheduled process
			startTime = Helper.currentTime;
			scheduledProcess.setBurstStartTime(startTime);	
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);			
			displayCurrentEvent();
			displayReadyQueue();		
			displayGanttChartQueue();
		}
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		remainingBurstTime--;
		if (remainingBurstTime == 0) {			
			processControlBlock.setCompletionTime(Helper.currentTime);
			processControlBlock.setTurnAroundTime(Helper.currentTime - processControlBlock.getArrivalTime() + 1);
			processControlBlock.setWaitTime(processControlBlock.getTurnAroundTime() - processControlBlock.getBurstTime());
			processControlBlock.setBurstEndTime(Helper.currentTime);
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
			ganttChartQueue.enqueue(processControlBlock);
		}
		processControlBlock.setRemainingBurstTime(remainingBurstTime);	
		Helper.currentTime++;
	}
		
	private ProcessControlBlock getProcessWithLowestRemainingBurstTimeAtCurrentTime() {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();		
		int i = 0;
		ProcessControlBlock processWithLowestRemainingBurstTimeAtCurrentTime = null;
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();		
			if (i == 0) {
				processWithLowestRemainingBurstTimeAtCurrentTime = current;
			}
			else {
				if (current.getArrivalTime() <= Helper.currentTime) {	
					if (current.getRemainingBurstTime() <= processWithLowestRemainingBurstTimeAtCurrentTime.getRemainingBurstTime()) {
						processWithLowestRemainingBurstTimeAtCurrentTime = current;
					}
				}
			}
			i++;
		}
		return processWithLowestRemainingBurstTimeAtCurrentTime;
	}
	
	private ProcessControlBlock getProcessWithLowerRemainingBurstTimeAtCurrentTime(int remainingBurstTime) {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();				
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();
			if (current.getRemainingBurstTime() < remainingBurstTime && current.getArrivalTime() <= Helper.currentTime) {				
				return current;
			}
		}
		return null;
	}
}