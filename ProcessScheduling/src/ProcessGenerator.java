import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ProcessGenerator {
	
	private int processCounter;
	Random random;
	private ProcessControlTable processControlTable;
		
	public ProcessGenerator(Random random, ProcessControlTable processControlTable) {
		this.processCounter = 1;
		this.random = random;
		this.processControlTable = processControlTable;
	}
		
	public int getProcessCounter() {
		return processCounter;
	}
	
	public void randomizeProcessArrivalInJobQueue(JobQueue jobQueue) {
		int capacity = jobQueue.getAvailableCapacity();
		//simulate different arrival time by randomly choosing to run
		int size = random.nextInt(capacity);
		if (size > 0) {
			ArrayList<ProcessControlBlock> processes = generate(size);		
			//populate the job queue
			Iterator<ProcessControlBlock> iterator = processes.iterator();
		    while (iterator.hasNext()) {
		    	ProcessControlBlock processControlBlock = iterator.next();		    	
		        jobQueue.enqueue(processControlBlock);
		        processControlTable.add(processControlBlock.getPID(), processControlBlock);
		    }
		}
	}
	
	private ArrayList<ProcessControlBlock> generate(int numberOfProcesses) {	
		ArrayList<ProcessControlBlock> processes = new ArrayList<ProcessControlBlock>();
		for (int i = 0; i < numberOfProcesses; i++) {
			ProcessControlBlock processControlBlock = new ProcessControlBlock(processCounter, ProcessStateEnum.NEW, 1);			
			processes.add(processControlBlock);
			if (processCounter == Helper.MAX_PROCESS) {
				break;
			}
			processCounter++;			
		}
		return processes;
	}
}
