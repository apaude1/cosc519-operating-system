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
			updateWaitTime(currentRunningProcess);
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);			
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);				
			System.out.println("Context switched pid: " + currentRunningProcess.getPID() + "; arrival time: " + currentRunningProcess.getArrivalTime() + "; burst time: " + currentRunningProcess.getBurstTime() + " partially finished at time: " + (Helper.currentTime - 1) + " with remaining burst time: " + currentRunningProcess.getRemainingBurstTime());
				
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
		remainingBurstTime--;			
		processControlBlock.setRemainingBurstTime(remainingBurstTime);				
		if (remainingBurstTime == 0) {
			updateWaitTime(processControlBlock);
			updateTurnAroundTimeAndCompletionTime(processControlBlock);
			System.out.println("pid: " + processControlBlock.getPID()+ "; finished at time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime());										
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
		}
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