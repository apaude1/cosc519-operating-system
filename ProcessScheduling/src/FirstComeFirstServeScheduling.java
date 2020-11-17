
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
		//remove the selected process from the ready queue
		readyQueue.remove(scheduledProcess);
		ProcessControlBlock processControlBlock = scheduledProcess;
		//set the state of the selected process to running
		processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
		System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime+ "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime());
			
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		Helper.currentTime += remainingBurstTime - 1;
		remainingBurstTime = 0;
		processControlBlock.setRemainingBurstTime(remainingBurstTime);	
		updateWaitTime(processControlBlock);
		updateTurnAroundTimeAndCompletionTime(processControlBlock);
		
		//set the state of the process to terminated
		processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
		System.out.println("pid: " + processControlBlock.getPID() + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime() + " finished at time: " + Helper.currentTime);
		Helper.currentTime++;
	}
}
