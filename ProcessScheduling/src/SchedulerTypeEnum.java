
public enum SchedulerTypeEnum {

	FIRST_COME_FIRST_SERVE(0),
	ROUND_ROBIN(1),
	SHORTEST_REMAINING_TIME_FIRST(2),
	PRIORITY(3);
	
	private int value;
	
	private SchedulerTypeEnum(int value) {
        this.value = value;
    }
	
	public int getValue() {
        return value;
    }
}
