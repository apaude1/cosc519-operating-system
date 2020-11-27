import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ProcessGenerator {
	
	private int processCounter;
	Random random;
		
	public ProcessGenerator(Random random) {
		this.processCounter = 1;
		this.random = random;
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
		    }
		}
	}
	
	private ArrayList<ProcessControlBlock> generate(int numberOfProcesses) {	
		ArrayList<ProcessControlBlock> processes = new ArrayList<ProcessControlBlock>();
		for (int i = 0; i < numberOfProcesses; i++) {
			processes.add(new ProcessControlBlock(processCounter, ProcessStateEnum.NEW, 1));
			if (processCounter == Helper.MAX_PROCESS) {
				break;
			}
			processCounter++;			
		}
		return processes;
	}
}
