
public class RoundRobinScheduling extends Scheduling {	
	
	public RoundRobinScheduling() {
		super();
	}		
		
	@Override
	protected ProcessControlBlock runCPUScheduler() {
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock != null && processControlBlock.getArrivalTime() <= Helper.currentTime) {				
			return processControlBlock;
		}
		return null;
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
			//context switched
				
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
		if (remainingBurstTime > Helper.QUANTUM) {
			Helper.currentTime += Helper.QUANTUM - 1;
			remainingBurstTime -= Helper.QUANTUM;
		} 
		else {
			Helper.currentTime += remainingBurstTime - 1;
			remainingBurstTime = 0;			
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
}