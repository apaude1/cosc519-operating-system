
public class Main {
		
	public static void main(String[] args) throws InterruptedException {		
		
		Metrics metrics = new Metrics();
		Thread a = new Thread(new FirstComeFirstServeScheduling(metrics, AlgorithmEnum.FIRST_COME_FIRST_SERVE));
		a.start();
		Thread b = new Thread(new RoundRobinScheduling(metrics, AlgorithmEnum.ROUND_ROBIN));
		b.start();
		Thread c = new Thread(new ShortestRemainingTimeFirstScheduling(metrics, AlgorithmEnum.SHORTEST_REMAINING_TIME_FIRST));
		c.start();
		Thread d = new Thread(new PriorityScheduling(metrics, AlgorithmEnum.PRIORITY));
		d.start();
		
		a.join();
		b.join();
		c.join();
		d.join();		
	}
}
