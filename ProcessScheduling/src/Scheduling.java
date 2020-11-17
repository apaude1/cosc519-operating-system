import java.util.Iterator;
import java.util.Random;

public abstract class Scheduling {

	protected ProcessGenerator processGenerator;
	protected ProcessControlTable processControlTable;
	protected JobQueue jobQueue;
	protected ReadyQueue readyQueue;
	protected AlgorithmEnum algorithm;
	Random random;
	
	public Scheduling(AlgorithmEnum algorithm) {
		this.algorithm = algorithm;
		this.processGenerator = new ProcessGenerator();
		this.processControlTable = new ProcessControlTable();
		this.jobQueue = new JobQueue();
		this.readyQueue = new ReadyQueue();	
		random = new Random();
	}	
	
	protected abstract ProcessControlBlock runCPUScheduler();
	
	protected abstract void runDispatcher(ProcessControlBlock selectedProcess);
	
	protected void run() {				
		//simulate random arriving processes in the job queue
		randomizeProcessArrivalInJobQueue();					
		while(true) {			
			//run job scheduler to populate ready queue
			runJobScheduler();		
			while(!readyQueue.isEmpty()) {
				ProcessControlBlock processControlBlock = runCPUScheduler();				
				runDispatcher(processControlBlock);		
				//run job scheduler when ready queue falls below threshold
				if (readyQueue.isBelowThresholdCapacity()) {
					runJobScheduler();
				}
			}
			
			if (Helper.processCounter > Helper.MAX_PROCESS) {
				break;
			}
			
			//CPU idle					
			System.out.println("CPU is idle at time: " + Helper.currentTime);
			Helper.currentTime++;
			
			//simulate random arrival of processes
			//this would add new processes with a new arrival time when job scheduler runs
			randomizeProcessArrivalInJobQueue();	
		}
	}	
	
	protected void runJobScheduler() {
		int availableCapacity = readyQueue.getAvailableCapacity();				
		while (availableCapacity > 0 && !jobQueue.isEmpty()) {
			ProcessControlBlock processControlBlock = jobQueue.dequeue();
			int arrivalTime = Helper.currentTime;
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
	
	protected void updateAccountingInformation(ProcessControlBlock processControlBlock, int remainingBurstTime) {
		int turnAroundTime = Helper.currentTime - processControlBlock.getArrivalTime();
		int waitTime = turnAroundTime - processControlBlock.getBurstTime();
		processControlBlock.setRemainingBurstTime(remainingBurstTime);
		processControlBlock.setCompletionTime(Helper.currentTime);
		processControlBlock.setWaitTime(waitTime);
		processControlBlock.setTurnAroundTime(turnAroundTime);
	}
	
	protected void randomizeProcessArrivalInJobQueue() {		
		processGenerator.randomizeProcessArrivalInJobQueue(jobQueue);
		//populate the process control table with all the incoming jobs
		Iterator<ProcessControlBlock> iterator = jobQueue.getIterator();           
	    while (iterator.hasNext()) { 
	    	ProcessControlBlock processControlBlock = iterator.next();        	
	        processControlTable.add(processControlBlock.getPID(), processControlBlock);
	    }         					
	}
}
