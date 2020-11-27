import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.PriorityQueue;

public abstract class Scheduling implements Runnable {
		
	private Random random;
	protected int contextSwitchCount;
	protected int currentTime;
	protected ProcessGenerator processGenerator;
	protected ProcessControlTable processControlTable;
	protected JobQueue jobQueue;
	protected ReadyQueue readyQueue;
	protected GanttChartQueue ganttChartQueue;
	protected Metrics metrics;
	protected AlgorithmEnum algorithmEnum;
	
	public Scheduling(ReadyQueue readyQueue, Metrics metrics, AlgorithmEnum algorithmEnum) {		
		this.contextSwitchCount = 0;
		this.currentTime = 0;			
		this.random = new Random(Helper.RANDOM_SEED);
		this.processGenerator = new ProcessGenerator(random);
		this.processControlTable = new ProcessControlTable();
		this.jobQueue = new JobQueue(new PriorityQueue<ProcessControlBlock>(Helper.JOB_QUEUE_CAPACITY, new ProcessPIdComparator()));
		this.readyQueue = readyQueue;	
		this.ganttChartQueue = new GanttChartQueue(new LinkedList<ProcessControlBlock>());
		this.metrics = metrics;
		this.algorithmEnum = algorithmEnum;
	}
		
	protected abstract ProcessControlBlock runCPUScheduler();
	
	protected abstract void runDispatcher(ProcessControlBlock selectedProcess);	
		
	protected void randomizeProcessArrivalInJobQueue() {		
		processGenerator.randomizeProcessArrivalInJobQueue(jobQueue);
		//populate the process control table with all the incoming jobs
		Iterator<ProcessControlBlock> iterator = jobQueue.getIterator();           
	    while (iterator.hasNext()) { 
	    	ProcessControlBlock processControlBlock = iterator.next();        	
	        processControlTable.add(processControlBlock.getPID(), processControlBlock);
	    }         					
	}
	
	public void run() {		
		//simulate random arriving processes in the job queue
		randomizeProcessArrivalInJobQueue();					
		while(true) {								
			//run job scheduler to populate ready queue
			runJobScheduler();		
			while(!readyQueue.isEmpty() || processControlTable.getRunningProcessControlBlock() != null) {				
				ProcessControlBlock processControlBlock = runCPUScheduler();
				runDispatcher(processControlBlock);
				//run job scheduler when ready queue falls below threshold
				if (readyQueue.isBelowThresholdCapacity()) {
					runJobScheduler();
				}
			}
						
			synchronized(metrics) {
//				metrics.displayCurrentEvent(processControlTable, currentTime, algorithmEnum);
//				metrics.displayReadyQueue(readyQueue, algorithmEnum);
//				metrics.displayGanttChartQueue(ganttChartQueue, algorithmEnum);
				metrics.displayAccountingInformation(processControlTable, contextSwitchCount, currentTime, algorithmEnum);
			}
			
			if (processGenerator.getProcessCounter() == Helper.MAX_PROCESS) {
				break;
			}
			currentTime++;
			
			//simulate random arrival of processes
			//this would add new processes with a new arrival time when job scheduler runs
			randomizeProcessArrivalInJobQueue();	
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
		scheduledProcess.setBurstStartTime(currentTime);		
		//remove the selected process from the ready queue
		readyQueue.remove(scheduledProcess);		
		//set the state of the selected process to running
		scheduledProcess.setProcessState(ProcessStateEnum.RUNNING);
		if (scheduledProcess.getResponseTime() <= 0) { //amount of time it takes from when a request was submitted until the first response is produced.
			scheduledProcess.setResponseTime(currentTime - scheduledProcess.getArrivalTime());
		}
		
//		synchronized(metrics) {
//			metrics.displayCurrentEvent(processControlTable, currentTime, algorithmEnum);
//			metrics.displayReadyQueue(readyQueue, algorithmEnum);
//			metrics.displayGanttChartQueue(ganttChartQueue, algorithmEnum);
//		}
	}	
}
