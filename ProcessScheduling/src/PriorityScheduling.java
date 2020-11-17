import java.util.Iterator;

public class PriorityScheduling extends Scheduling {	
	
	public PriorityScheduling() {
		super();
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
	protected void runDispatcher(ProcessControlBlock selectedProcess) {		
		//context switch if there is already executing process and new process is scheduled to run
		ProcessControlBlock currentRunningProcess = processControlTable.getRunningProcessControlBlock();
		ProcessControlBlock processControlBlock = null;
		if (currentRunningProcess != null && selectedProcess != null) {	
			updateWaitTime(currentRunningProcess);
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);				
			System.out.println("Context switched pid: " + currentRunningProcess.getPID() + "; arrival time: " + currentRunningProcess.getArrivalTime() + "; priority: " + currentRunningProcess.getPriority() + "; burst time: " + currentRunningProcess.getBurstTime() + " partially finished at time: " + (Helper.currentTime - 1) + " with remaining burst time: " + currentRunningProcess.getRemainingBurstTime());
				
			//start executing the scheduled process
			startTime = Helper.currentTime;
			processControlBlock = selectedProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; priority: " + processControlBlock.getPriority() + "; burst time: " + processControlBlock.getBurstTime() + " remaining burst time: " + processControlBlock.getRemainingBurstTime());
		}
		else if (currentRunningProcess != null && selectedProcess == null) {
			//continue executing the current process
			processControlBlock = currentRunningProcess;
		}
		else if (currentRunningProcess == null && selectedProcess != null) {
			//start executing the scheduled process
			startTime = Helper.currentTime;
			processControlBlock = selectedProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; priority: " + processControlBlock.getPriority() + "; burst time: " + processControlBlock.getBurstTime() + " remaining burst time: " + processControlBlock.getRemainingBurstTime());
		}
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		remainingBurstTime--;			
		processControlBlock.setRemainingBurstTime(remainingBurstTime);				
		if (remainingBurstTime == 0) {					
			updateWaitTime(processControlBlock);
			updateTurnAroundTimeAndCompletionTime(processControlBlock);
			System.out.println("pid: " + processControlBlock.getPID()+ "; finished at time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; priority: " + processControlBlock.getPriority() + "; burst time: " + processControlBlock.getBurstTime());										
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
		}
		Helper.currentTime++;
	}
	
	private ProcessControlBlock getProcessWithHighestPriorityAtCurrentTime() {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();		
		int i = 0;
		ProcessControlBlock processWithHighestPriority = null;
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();		
			if (i == 0) {
				processWithHighestPriority = current;
			}
			else {
				if (current.getArrivalTime() <= Helper.currentTime) {	
					if (current.getPriority() <= processWithHighestPriority.getPriority()) {
						processWithHighestPriority = current;
					}
				}
			}
			i++;
		}
		return processWithHighestPriority;
	}
	
	private ProcessControlBlock getProcessWithHigherPriorityAtCurrentTime(int priority) {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();				
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();
			if (current.getPriority() < priority && current.getArrivalTime() <= Helper.currentTime) {				
				return current;
			}
		}
		return null;
	}
}