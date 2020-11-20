import java.util.Iterator;
import java.util.Random;

public abstract class Scheduling {

	protected ProcessGenerator processGenerator;
	protected ProcessControlTable processControlTable;
	protected JobQueue jobQueue;
	protected ReadyQueue readyQueue;
	protected GanttChartQueue ganttChartQueue;
	Random random;
	protected int startTime = 0;
	
	public Scheduling() {
		this.processGenerator = new ProcessGenerator();
		this.processControlTable = new ProcessControlTable();
		this.jobQueue = new JobQueue();
		this.readyQueue = new ReadyQueue();	
		this.ganttChartQueue = new GanttChartQueue();
		random = new Random();
	}	
	
	protected abstract ProcessControlBlock runCPUScheduler();
	
	protected abstract void runDispatcher(ProcessControlBlock selectedProcess);
	
	private void displayAccountingInformation() {
		System.out.println("******************************");
		processControlTable.displayAccountingInformation();
	}
	
	protected void displayCurrentEvent() {
		System.out.println("******************************");
		ProcessControlBlock runningProcessControlBlock = processControlTable.getRunningProcessControlBlock();
		System.out.println("Current Time: " + Helper.currentTime);		
		if (runningProcessControlBlock != null) {			
			System.out.println("Executing Process: P" + runningProcessControlBlock.getPID() + "; Remaining Burst Time: " + runningProcessControlBlock.getRemainingBurstTime() + "; Priority: " + runningProcessControlBlock.getPriority());
		}
		else {
			System.out.println("Executing Process: Idle");
		}
	}
	
	protected void displayGanttChartQueue() {
		Iterator<ProcessControlBlock> iterator = ganttChartQueue.getIterator();		
		int previousProcessBurstEndTime = 0;
		System.out.print("Gantt Chart: ");
		while (iterator.hasNext()) {
			ProcessControlBlock processControlBlock = iterator.next();
			int burstStartTime = processControlBlock.getBurstStartTime();			
			if (previousProcessBurstEndTime == burstStartTime) {
				System.out.print("|" + previousProcessBurstEndTime + "    P" + processControlBlock.getPID() + "    " + processControlBlock.getBurstEndTime());					
			}
			else {
				System.out.print("|" + previousProcessBurstEndTime + "  Idle  " + (processControlBlock.getBurstStartTime() - 1) + "|" + processControlBlock.getBurstStartTime() + "    P" + processControlBlock.getPID() + "    " + processControlBlock.getBurstEndTime());
			}
			previousProcessBurstEndTime = processControlBlock.getBurstEndTime() + 1;
	    }
		System.out.print("|");
		System.out.println();
	}
	
	protected void displayReadyQueue() {				
		Iterator<ProcessControlBlock> iterator = readyQueue.getIterator();
		int i = 0;
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				ProcessControlBlock processControlBlock = iterator.next();
				if (i == 0) {
					System.out.print("Ready Queue: |  P" + processControlBlock.getPID());
				}	
				else {
					System.out.print("  |  P" + processControlBlock.getPID());
				}
		        i++;
		    }
			System.out.println("  |");
		}
		else {
			System.out.println("Ready Queue: Empty");
		}
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
	
	protected void run() {				
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
			
			displayCurrentEvent();
			displayReadyQueue();
			displayGanttChartQueue();
			
			if (Helper.processCounter > Helper.MAX_PROCESS) {
				break;
			}
			Helper.currentTime++;
			
			//simulate random arrival of processes
			//this would add new processes with a new arrival time when job scheduler runs
			randomizeProcessArrivalInJobQueue();	
		}
		System.out.println("******************************");
		System.out.print("Final Gantt Chart: ");
		displayGanttChartQueue();		
		displayAccountingInformation();
	}	
	
	protected void runJobScheduler() {
		System.out.println("******************************");		
		System.out.println("Current Time: " + Helper.currentTime);	
		System.out.println("Running Job Scheduler");
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
	
	//turn around time is the total amount of time spent by the process from coming in the ready state for the first time to its completion
	protected void updateTurnAroundTimeAndCompletionTime(ProcessControlBlock processControlBlock) {
		int turnAroundTime = processControlBlock.getBurstTime() + processControlBlock.getWaitTime();
		processControlBlock.setTurnAroundTime(turnAroundTime);
		processControlBlock.setCompletionTime(Helper.currentTime);		
	}	
	
	//waiting time is the total time taken by the process in the ready queue
	protected void updateWaitTime(ProcessControlBlock processControlBlock) {		
		int waitTime = processControlBlock.getWaitTime();
		waitTime += processControlBlock.getBurstStartTime() - processControlBlock.getBurstEndTime();
		processControlBlock.setWaitTime(waitTime);
	}	
}
