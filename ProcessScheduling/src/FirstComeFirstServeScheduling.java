import java.util.PriorityQueue;

public class FirstComeFirstServeScheduling extends Scheduling {	
	
	public FirstComeFirstServeScheduling(Metrics metrics, AlgorithmEnum algorithmEnum) {		
		super(new ReadyQueue(new PriorityQueue<ProcessControlBlock>(Helper.READY_QUEUE_CAPACITY, new ProcessArrivalTimeComparator())), metrics, algorithmEnum);
	}		
	
	@Override
	protected ProcessControlBlock runCPUScheduler() {	
		ProcessControlBlock processControlBlock = readyQueue.peek();
		if (processControlBlock.getArrivalTime() <= currentTime) {
			return processControlBlock;
		}		
		return null;
	}
	
	@Override
	protected void runDispatcher(ProcessControlBlock scheduledProcess) {	
		setUpRunningProcess(scheduledProcess);
			
		//processControlBlock.setProgramCounter();			
		int remainingBurstTime = scheduledProcess.getRemainingBurstTime();					
		currentTime += remainingBurstTime - 1;
		remainingBurstTime = 0;		
		scheduledProcess.setRemainingBurstTime(remainingBurstTime);	
		scheduledProcess.setBurstEndTime(currentTime);
		scheduledProcess.setCompletionTime(currentTime);
		scheduledProcess.setTurnAroundTime(currentTime - scheduledProcess.getArrivalTime() + 1);
		scheduledProcess.setWaitTime(scheduledProcess.getTurnAroundTime() - scheduledProcess.getBurstTime());
		ganttChartQueue.enqueue(new ProcessControlBlock(scheduledProcess.getPID(), scheduledProcess.getBurstStartTime(), scheduledProcess.getBurstEndTime()));
		
		//set the state of the process to terminated
		scheduledProcess.setProcessState(ProcessStateEnum.TERMINATED);
		currentTime++;
	}
}
