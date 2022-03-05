
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

public class PA2_part2 {

	public static void main (String [] args) {

		// create random var
		Random rand = new Random();
		// set timer to stop simulation, prevents infinite runtime
		// will exit after roughly 30 seconds
		long start = System.currentTimeMillis();
		long end = start + 30*1000;
		
		Scanner sc = new Scanner(System.in);  
		// prompts for number of guests
		System.out.println("Enter number of guests: ");
		int totalGuests = sc.nextInt();

		// dec for array of guest threads
		Guest [] guests = new Guest[totalGuests];

		// array to check if thread was already started or not
        Boolean [] started = new Boolean[totalGuests];
        Arrays.fill(started, Boolean.FALSE);

		// create spinlock for threads
		SpinLock lock = new SpinLock();

		// create the guest threads, also checks time
		for (int i = 0; i < totalGuests; i++)
		{
			if (System.currentTimeMillis() > end)
				System.exit(0);
			
			guests[i] = new Guest(i, lock);
		}

		// start the guest threads, also checks time
		for (int i = 0; i < totalGuests; i++)
		{
			if (System.currentTimeMillis() > end)
				System.exit(0);
			
			guests[i].start();
		}

		// continuously run the guest threads, also checks time
		while (true)
		{
			if (System.currentTimeMillis() > end)
				System.exit(0);
			guests[rand.nextInt(totalGuests - 1)].run();
		}
	}
}


// guest class
class Guest extends Thread {

	private int threadNumber;
	private int queueChance;
	SpinLock lock;

	// creates random var
	Random rand = new Random();

	// constructor
	public Guest (int threadNumber, SpinLock lock)
	{
		this.queueChance = 2;
		this.lock = lock;
		this.threadNumber = threadNumber;
	}

	public void run() 
	{
		// if random number 1-100 is more than queue chance times 100 (100, 50, 25, )
		// this prevents threads that have seen the vase from repeatedly joining
		if (rand.nextInt(100* queueChance) < 50 && queueChance < 1024)
		{
			queueChance = queueChance * 2;
			// tries lock
			lock.lock();
			System.out.println("Guest " + threadNumber + " saw the vase!");
			lock.unlock();
		}
	}
}

class SpinLock extends ReentrantLock{

	// constructor for lock
	public SpinLock() {
		super();
	}

	// sets up spinlock to keep checking if lock is available
	public void lock() {
		while (!super.tryLock())
		{
			// Do Nothing
		}
	}

	// unlocks the lock
	public void unlock()
	{
		super.unlock();
	}
}
