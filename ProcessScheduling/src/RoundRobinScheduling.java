
public class RoundRobinScheduling extends Scheduling {	
	
	public RoundRobinScheduling() {
		super();
	}		
		
	@Override
	protected ProcessControlBlock runCPUScheduler() {
		return readyQueue.peek();
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock scheduledProcess) {	
		//context switch if there is already executing process and new process is scheduled to run
		ProcessControlBlock currentRunningProcess = processControlTable.getRunningProcessControlBlock();
		ProcessControlBlock processControlBlock = null;
		if (currentRunningProcess != null && scheduledProcess != null) {		
			updateWaitTime(currentRunningProcess);
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);				
			System.out.println("Context switched pid: " + currentRunningProcess.getPID() + "; arrival time: " + currentRunningProcess.getArrivalTime() + "; burst time: " + currentRunningProcess.getBurstTime() + " finished its quantum at time: " + (Helper.currentTime - 1) + " with remaining burst time: " + currentRunningProcess.getRemainingBurstTime());
				
			//start executing the scheduled process
			startTime = Helper.currentTime;
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime() + " remaining burst time: " + processControlBlock.getRemainingBurstTime());
		}
		else if (currentRunningProcess != null && scheduledProcess == null) {
			//continue executing the current process
			processControlBlock = currentRunningProcess;
		}
		else if (currentRunningProcess == null && scheduledProcess != null) {
			//start executing the scheduled process
			startTime = Helper.currentTime;
			processControlBlock = scheduledProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime() + " remaining burst time: " + processControlBlock.getRemainingBurstTime());
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
			updateTurnAroundTimeAndCompletionTime(processControlBlock);
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
			System.out.println("pid: " + processControlBlock.getPID() + "; finished at: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime());				
		}
		processControlBlock.setRemainingBurstTime(remainingBurstTime);
		updateWaitTime(processControlBlock);
		Helper.currentTime++;
	}
}