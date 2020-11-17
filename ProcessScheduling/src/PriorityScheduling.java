import java.util.Iterator;

public class PriorityScheduling extends Scheduling {	
	
	public PriorityScheduling() {
		super(AlgorithmEnum.PS);
	}
		
	@Override
	protected ProcessControlBlock runCPUScheduler() {		
		ProcessControlBlock processControlBlock = processControlTable.getRunningProcessControlBlock();
		if (processControlBlock != null) {
			if (higherPriorityProcessAtCurrentTimeExists(processControlBlock.getPriority())) {
				return getHigherPriorityProcessAtCurrentTime(processControlBlock.getPriority());	
			}
			return null;
		}
		return getHighestPriorityProcessAtCurrentTime();
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock selectedProcess) {		
		//context switch if there is already executing process and new process is scheduled to run
		ProcessControlBlock currentRunningProcess = processControlTable.getRunningProcessControlBlock();
		ProcessControlBlock processControlBlock = null;
		if (currentRunningProcess != null && selectedProcess != null) {					
			//set the state of the process
			currentRunningProcess.setProcessState(ProcessStateEnum.READY);
			//put it back to the ready queue
			readyQueue.enqueue(currentRunningProcess);				
			System.out.println("Context switched pid: " + currentRunningProcess.getPID() + "; arrival time: " + currentRunningProcess.getArrivalTime() + "; priority: " + currentRunningProcess.getPriority() + "; burst time: " + currentRunningProcess.getBurstTime() + " partially finished at time: " + (Helper.currentTime - 1) + " with remaining burst time: " + currentRunningProcess.getRemainingBurstTime());
				
			processControlBlock = selectedProcess;
			//remove the selected process from the ready queue
			readyQueue.remove(processControlBlock);
			//set the state of the selected process to running
			processControlBlock.setProcessState(ProcessStateEnum.RUNNING);	
			System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; priority: " + processControlBlock.getPriority() + "; burst time: " + processControlBlock.getBurstTime() + " remaining burst time: " + processControlBlock.getRemainingBurstTime());
		}
		else if (currentRunningProcess != null && selectedProcess == null) {
			processControlBlock = currentRunningProcess;
		}
		else if (currentRunningProcess == null && selectedProcess != null) {
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
			updateAccountingInformation(processControlBlock, remainingBurstTime);
			System.out.println("pid: " + processControlBlock.getPID()+ "; finished at time: " + Helper.currentTime + "; arrival time: " + processControlBlock.getArrivalTime() + "; priority: " + processControlBlock.getPriority() + "; burst time: " + processControlBlock.getBurstTime());										
			//set the state of the process to terminated
			processControlBlock.setProcessState(ProcessStateEnum.TERMINATED);
		}
		Helper.currentTime++;
	}
	
	private boolean higherPriorityProcessAtCurrentTimeExists(int priority) {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();				
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();
			if (current.getPriority() < priority && current.getArrivalTime() == Helper.currentTime) {				
				return true;
			}
		}
		return false;
	}
	
	private ProcessControlBlock getHighestPriorityProcessAtCurrentTime() {
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();		
		int i = 0;
		ProcessControlBlock highestPriorityProcess = null;
		while (iterator.hasNext()) {
			ProcessControlBlock current = iterator.next();		
			if (i == 0) {
				highestPriorityProcess = current;
			}
			else {
				if (current.getArrivalTime() <= Helper.currentTime) {	
					if (current.getPriority() <= highestPriorityProcess.getPriority()) {
						highestPriorityProcess = current;
					}
				}
			}
			i++;
		}
		return highestPriorityProcess;
	}
	
	private ProcessControlBlock getHigherPriorityProcessAtCurrentTime(int priority) {
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