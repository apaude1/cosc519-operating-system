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
		//simulate random process generation
		int size = random.nextInt(capacity);
		if (size > 0) {
			ArrayList<ProcessControlBlock> processes = generate(capacity);		
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
		
		/*TEST Code
		 * ProcessControlBlock processControlBlock = new ProcessControlBlock(1,
		 * ProcessStateEnum.NEW, 1); processControlBlock.setArrivalTime(0);
		 * processControlBlock.setBurstTime(7);
		 * processControlBlock.setRemainingBurstTime(7);
		 * processControlBlock.setPriority(7); processes.add(processControlBlock);
		 * processCounter++;
		 * 
		 * processControlBlock = new ProcessControlBlock(2, ProcessStateEnum.NEW, 1);
		 * processControlBlock.setArrivalTime(4); processControlBlock.setBurstTime(4);
		 * processControlBlock.setRemainingBurstTime(4);
		 * processControlBlock.setPriority(4); processes.add(processControlBlock);
		 * processCounter++;
		 * 
		 * processControlBlock = new ProcessControlBlock(3, ProcessStateEnum.NEW, 1);
		 * processControlBlock.setArrivalTime(5); processControlBlock.setBurstTime(1);
		 * processControlBlock.setRemainingBurstTime(1);
		 * processControlBlock.setPriority(1); processes.add(processControlBlock);
		 * processCounter++;
		 * 
		 * processControlBlock = new ProcessControlBlock(4, ProcessStateEnum.NEW, 1);
		 * processControlBlock.setArrivalTime(9); processControlBlock.setBurstTime(1);
		 * processControlBlock.setRemainingBurstTime(1);
		 * processControlBlock.setPriority(1); processes.add(processControlBlock);
		 * processCounter++;
		 * 
		 * processControlBlock = new ProcessControlBlock(5, ProcessStateEnum.NEW, 1);
		 * processControlBlock.setArrivalTime(12); processControlBlock.setBurstTime(3);
		 * processControlBlock.setRemainingBurstTime(3);
		 * processControlBlock.setPriority(3); processes.add(processControlBlock);
		 * processCounter++;
		 */
		
		return processes;
	}
}
