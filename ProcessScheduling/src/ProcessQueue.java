import java.util.Iterator;
import java.util.LinkedList;

public abstract class ProcessQueue {	
		
	protected LinkedList<ProcessControlBlock> processQueue;	 
	
	public ProcessQueue() {		
		this.processQueue = new LinkedList<ProcessControlBlock>();
	}
	
	protected abstract int getAvailableCapacity();
	
	public abstract boolean isBelowThresholdCapacity();
	
	public Iterator<ProcessControlBlock> getIterator() {
		return processQueue.iterator();
	}
	
	public int getSize() {
		return processQueue.size();
	}	
	
	public ProcessControlBlock peek() {
		return processQueue.peek();
	}
	
	public void enqueue(ProcessControlBlock processControlBlock) {
		processQueue.add(processControlBlock);	
	}
	
	public ProcessControlBlock dequeue() {
		return processQueue.poll();
	}
	
	public void remove(ProcessControlBlock processControlBlock) {
		processQueue.remove(processControlBlock);
	}
	
	public boolean isEmpty() {
		return processQueue.isEmpty();
	}
}
