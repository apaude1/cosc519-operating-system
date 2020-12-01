import java.util.concurrent.PriorityBlockingQueue;

public class FirstComeFirstServeScheduling extends Scheduling {	
	
	public FirstComeFirstServeScheduling(Metrics metrics, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {		
		super(new ReadyQueue(new PriorityBlockingQueue<ProcessControlBlock>(Helper.READY_QUEUE_CAPACITY, new ProcessArrivalTimeComparator())), metrics, threadSequence, schedulerTypeEnum);
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
		if (scheduledProcess != null) {
			setUpRunningProcess(scheduledProcess);
			//processControlBlock.setProgramCounter();			
			int remainingBurstTime = scheduledProcess.getRemainingBurstTime();					
			currentTime += remainingBurstTime - 1;
			remainingBurstTime = 0;		
			scheduledProcess.setRemainingBurstTime(remainingBurstTime);
			scheduledProcess.setCompletionTime(currentTime);
			scheduledProcess.setBurstEndTime(currentTime);	
			scheduledProcess.setTurnAroundTime(currentTime - scheduledProcess.getArrivalTime() + 1);
			scheduledProcess.setWaitTime(scheduledProcess.getTurnAroundTime() - scheduledProcess.getBurstTime());
			scheduledProcess.setResponseRatioTime((float)scheduledProcess.getBurstTime() / (float)scheduledProcess.getTurnAroundTime());							
			//set the state of the process to terminated
			scheduledProcess.setProcessState(ProcessStateEnum.TERMINATED);
			ganttChartQueue.enqueue(new ProcessControlBlock(scheduledProcess.getPID(), scheduledProcess.getBurstStartTime(), scheduledProcess.getBurstEndTime()));			
		}
		currentTime++;
	}
}
