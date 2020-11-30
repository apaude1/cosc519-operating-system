
public class Main {
		
	public static void main(String[] args) throws InterruptedException {		
		
		Metrics metrics = new Metrics(4);
		Thread a = new Thread(new FirstComeFirstServeScheduling(metrics, 1, SchedulerTypeEnum.FIRST_COME_FIRST_SERVE));
		a.start();
		Thread b = new Thread(new RoundRobinScheduling(metrics, 2, SchedulerTypeEnum.ROUND_ROBIN));
		b.start();
		Thread c = new Thread(new ShortestRemainingTimeFirstScheduling(metrics, 3, SchedulerTypeEnum.SHORTEST_REMAINING_TIME_FIRST));
		c.start();
		Thread d = new Thread(new PriorityScheduling(metrics, 0, SchedulerTypeEnum.PRIORITY));
		d.start();
		
		a.join();
		b.join();
		c.join();
		d.join();		
	}
}
