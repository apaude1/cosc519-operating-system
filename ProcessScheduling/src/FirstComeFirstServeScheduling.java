
public class FirstComeFirstServeScheduling extends Scheduling {	
	
	public FirstComeFirstServeScheduling() {
		super(Algorithm.FCFS);
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
	protected void runDispatcher(ProcessControlBlock selectedProcess) {		
		//remove the selected process from the ready queue
		readyQueue.remove(selectedProcess);
		//get the selected process info from the process control block
		ProcessControlBlock processControlBlock = processControlTable.getProcessControlBlockByProcessId(selectedProcess.getPID());
		//set the state of the selected process to running
		processControlBlock.setProcessState(ProcessState.RUNNING);	
		System.out.println("pid: " + processControlBlock.getPID() + "; start time: " + Helper.currentTime+ "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime());
			
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = processControlBlock.getRemainingBurstTime();					
		Helper.currentTime += remainingBurstTime - 1;
		remainingBurstTime = 0;
		updateAccountingInformation(processControlBlock, remainingBurstTime);
		
		//set the state of the process to terminated
		processControlBlock.setProcessState(ProcessState.TERMINATED);
		System.out.println("pid: " + processControlBlock.getPID() + "; arrival time: " + processControlBlock.getArrivalTime() + "; burst time: " + processControlBlock.getBurstTime() + " finished at time: " + Helper.currentTime);
		Helper.currentTime++;
	}
}
