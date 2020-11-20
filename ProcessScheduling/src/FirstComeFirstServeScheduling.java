
public class FirstComeFirstServeScheduling extends Scheduling {	
	
	public FirstComeFirstServeScheduling() {
		super();
	}		
	
	@Override
	protected ProcessControlBlock runCPUScheduler() {	
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock.getArrivalTime() <= Helper.currentTime) {
			return processControlBlock;
		}		
		return null;
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock scheduledProcess) {		
		//start executing the scheduled process
		startTime = Helper.currentTime;
		scheduledProcess.setBurstStartTime(startTime);		
		//remove the selected process from the ready queue
		readyQueue.remove(scheduledProcess);
		ProcessControlBlock processControlBlock = scheduledProcess;
		//set the state of the selected process to running
		processControlBlock.setProcessState(ProcessStateEnum.RUNNING);
		displayCurrentEvent();
		displayReadyQueue();		
		displayGanttChartQueue();
			
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		Helper.currentTime += remainingBurstTime - 1;
		remainingBurstTime = 0;
		processControlBlock.setRemainingBurstTime(remainingBurstTime);	
		processControlBlock.setBurstEndTime(Helper.currentTime);
		processControlBlock.setCompletionTime(Helper.currentTime);
		processControlBlock.setTurnAroundTime(Helper.currentTime - processControlBlock.getArrivalTime() + 1);
		processControlBlock.setWaitTime(processControlBlock.getTurnAroundTime() - processControlBlock.getBurstTime());
		ganttChartQueue.enqueue(processControlBlock);
		
		
		//set the state of the process to terminated
		processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
		Helper.currentTime++;
	}
}
