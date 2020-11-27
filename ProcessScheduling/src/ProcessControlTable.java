import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class ProcessControlTable {
	
	private LinkedHashMap<Integer, ProcessControlBlock> processControlTable;
	
	public ProcessControlTable() {		
		processControlTable = new LinkedHashMap<Integer, ProcessControlBlock>();
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
			if (processControlBlock.getProcessState() == ProcessStateEnum.RUNNING) {
				return processControlBlock;				
			}
		}
		return null;
	}	

	public List<Entry<Integer, ProcessControlBlock>> getListEntrySet() {
		List<Entry<Integer, ProcessControlBlock>> entries = new ArrayList<Entry<Integer, ProcessControlBlock>>(processControlTable.entrySet());
		Collections.sort(entries, new ProcessControlTableComparator());		
		return entries;
	}
}
