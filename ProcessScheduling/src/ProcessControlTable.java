import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class ProcessControlTable {
	
	private Hashtable<Integer, ProcessControlBlock> processControlTable;
	
	public ProcessControlTable() { 	      
		processControlTable = new Hashtable<>();
    } 
	
	public void add(int pid, ProcessControlBlock processControlBlock) {
		processControlTable.put(pid, processControlBlock);
	}
		
	public ProcessControlBlock getProcessControlBlockByProcessId(int pid) {
		return processControlTable.get(pid);
	}
	
	public ProcessControlBlock getRunningProcessControlBlock() {
		Set<Integer> keys = processControlTable.keySet();
		for(Integer key: keys) {
			ProcessControlBlock processControlBlock = processControlTable.get(key);
			if (processControlBlock.getProcessState() == ProcessState.RUNNING) {
				return processControlBlock;				
			}
		}
		return null;
	}
}
