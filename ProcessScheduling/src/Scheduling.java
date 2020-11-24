import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.PriorityQueue;

public abstract class Scheduling {
	
	private Random random;
	protected ProcessGenerator processGenerator;
	protected ProcessControlTable processControlTable;
	protected JobQueue jobQueue;
	protected ReadyQueue readyQueue;
	protected GanttChartQueue ganttChartQueue;	
	
	public Scheduling(ReadyQueue readyQueue) {		
		Helper.contextSwitchCount = 0;
		Helper.currentTime = 0;
		Helper.processCounter = 1;		
		this.random = new Random(Helper.randomSeed);
		this.processGenerator = new ProcessGenerator(random);
		this.processControlTable = new ProcessControlTable();
		this.jobQueue = new JobQueue(new PriorityQueue<ProcessControlBlock>(Helper.JOB_QUEUE_CAPACITY, new ProcessPIdComparator()));
		this.readyQueue = readyQueue;	
		this.ganttChartQueue = new GanttChartQueue(new LinkedList<ProcessControlBlock>());		
	}	
	
	protected abstract ProcessControlBlock runCPUScheduler();
	
	protected abstract void runDispatcher(ProcessControlBlock selectedProcess);
	
	protected void displayCurrentEvent() {
		System.out.println("******************************");
		ProcessControlBlock runningProcessControlBlock = processControlTable.getRunningProcessControlBlock();
		System.out.println("Current Time: " + Helper.currentTime);		
		if (runningProcessControlBlock != null) {			
			System.out.println("Executing Process: P" + runningProcessControlBlock.getPID() + "; Arrival Time: " + runningProcessControlBlock.getArrivalTime() + "; Remaining Burst Time: " + runningProcessControlBlock.getRemainingBurstTime() + "; Priority: " + runningProcessControlBlock.getPriority());
		}
		else {
			System.out.println("Executing Process: Idle");
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
			
			//displayCurrentEvent();
			//readyQueue.displayReadyQueue();
			//ganttChartQueue.displayGanttChartQueue();
			
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
		ganttChartQueue.displayGanttChartQueue();		
		processControlTable.displayAccountingInformation();
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
	
	protected void setUpRunningProcess(ProcessControlBlock scheduledProcess) {
		scheduledProcess.setBurstStartTime(Helper.currentTime);		
		//remove the selected process from the ready queue
		readyQueue.remove(scheduledProcess);		
		//set the state of the selected process to running
		scheduledProcess.setProcessState(ProcessStateEnum.RUNNING);
		//displayCurrentEvent();
		//readyQueue.displayReadyQueue();		
		//ganttChartQueue.displayGanttChartQueue();
	}
}
