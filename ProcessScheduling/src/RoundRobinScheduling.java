import java.util.LinkedList;

public class RoundRobinScheduling extends Scheduling {	
		
	public RoundRobinScheduling() {
		super(new ReadyQueue(new LinkedList<ProcessControlBlock>()));
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
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);				
			//context switched
				
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			displayCurrentEvent();
			readyQueue.displayReadyQueue();		
			ganttChartQueue.displayGanttChartQueue();
		}
		else if (currentRunningProcess == null && scheduledProcess != null) {			
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			displayCurrentEvent();
			readyQueue.displayReadyQueue();		
			ganttChartQueue.displayGanttChartQueue();
		}
		
		//start executing the scheduled process
		scheduledProcess.setBurstStartTime(Helper.currentTime);
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
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);	
		}
		processControlBlock.setRemainingBurstTime(remainingBurstTime);
		processControlBlock.setBurstEndTime(Helper.currentTime);
		ganttChartQueue.enqueue(new ProcessControlBlock(processControlBlock.getPID(), processControlBlock.getBurstStartTime(), processControlBlock.getBurstEndTime()));
		Helper.currentTime++;
	}
}