import java.util.LinkedList;
import java.util.Random;
import java.util.PriorityQueue;

public abstract class Scheduling implements Runnable {
		
	protected int contextSwitchCount;
	protected int currentTime;
	protected int threadSequence;
	private Random random;	
	protected ProcessGenerator processGenerator;
	protected ProcessControlTable processControlTable;
	protected JobQueue jobQueue;
	protected ReadyQueue readyQueue;
	protected GanttChartQueue ganttChartQueue;
	protected Metrics metrics;	
	protected SchedulerTypeEnum schedulerTypeEnum;
	
	public Scheduling(ReadyQueue readyQueue, Metrics metrics, int threadSequence, SchedulerTypeEnum schedulerTypeEnum) {		
		this.contextSwitchCount = 0;
		this.currentTime = 0;	
		this.threadSequence = threadSequence;
		this.random = new Random(Helper.RANDOM_SEED);
		this.processControlTable = new ProcessControlTable();
		this.processGenerator = new ProcessGenerator(random, processControlTable);	
		this.jobQueue = new JobQueue(new PriorityQueue<ProcessControlBlock>(Helper.JOB_QUEUE_CAPACITY, new ProcessPIdComparator()));
		this.readyQueue = readyQueue;	
		this.ganttChartQueue = new GanttChartQueue(new LinkedList<ProcessControlBlock>());
		this.metrics = metrics;		
		this.schedulerTypeEnum = schedulerTypeEnum;
	}
		
	protected abstract ProcessControlBlock runCPUScheduler();
	
	protected abstract void runDispatcher(ProcessControlBlock selectedProcess);	
		
	public void run() {		
		//simulate random arriving processes in the job queue
		processGenerator.randomizeProcessArrivalInJobQueue(jobQueue, currentTime);					
		while(true) {								
			//run job scheduler to populate ready queue
			runJobScheduler();		
			while(!readyQueue.isEmpty() || processControlTable.getRunningProcessControlBlock() != null) {				
				ProcessControlBlock processControlBlock = runCPUScheduler();
				runDispatcher(processControlBlock);
				//TODO: isBelowThreshold is causing different arrival times
				//run job scheduler when ready queue falls below threshold
				if (readyQueue.isEmpty()) {
					runJobScheduler();
				}
			}
			
//			metrics.displayCurrentEvent(processControlTable, currentTime, threadSequence, schedulerTypeEnum);
//			metrics.displayReadyQueue(readyQueue, threadSequence, schedulerTypeEnum);
//			metrics.displayGanttChartQueue(ganttChartQueue, threadSequence, schedulerTypeEnum);
			metrics.displayAccountingInformation(processControlTable, contextSwitchCount, currentTime, threadSequence, schedulerTypeEnum);
				
			
			if (processGenerator.getProcessCounter() == Helper.MAX_PROCESS) {
				break;
			}
			currentTime++;
			
			//simulate random arrival of processes
			//this would add new processes with a new arrival time when job scheduler runs
			processGenerator.randomizeProcessArrivalInJobQueue(jobQueue, currentTime);
		}
	}
	
	
	protected void runJobScheduler() {
		int availableCapacity = readyQueue.getAvailableCapacity();				
		while (availableCapacity > 0 && !jobQueue.isEmpty()) {
			ProcessControlBlock processControlBlock = jobQueue.dequeue();
			int arrivalTime = currentTime;
			int burstTime = 1 + random.nextInt(Helper.MAX_BURST_TIME);
			int priority = 1 + random.nextInt(Helper.MAX_PRIORITY);		
			processControlBlock.setArrivalTime(arrivalTime);
			processControlBlock.setBurstTime(burstTime);			
			processControlBlock.setRemainingBurstTime(burstTime);
			processControlBlock.setPriority(priority);
			readyQueue.enqueue(processControlBlock);
			availableCapacity--;
		}		
	}
	
	protected void setUpRunningProcess(ProcessControlBlock scheduledProcess) {
		//set the start time once
		if (scheduledProcess.getBurstTime() == scheduledProcess.getRemainingBurstTime()) {
			scheduledProcess.setStartTime(currentTime);
		}
		scheduledProcess.setBurstStartTime(currentTime);		
		//remove the selected process from the ready queue
		readyQueue.remove(scheduledProcess);		
		//set the state of the selected process to running
		scheduledProcess.setProcessState(ProcessStateEnum.RUNNING);
		if (scheduledProcess.getResponseTime() <= 0) { //amount of time it takes from when a request was submitted until the first response is produced.
			scheduledProcess.setResponseTime(currentTime - scheduledProcess.getArrivalTime());
		}
		
//		metrics.displayCurrentEvent(processControlTable, currentTime, threadSequence, schedulerTypeEnum);
//		metrics.displayReadyQueue(readyQueue, threadSequence, schedulerTypeEnum);
//		metrics.displayGanttChartQueue(ganttChartQueue, threadSequence, schedulerTypeEnum);
	}	
}
