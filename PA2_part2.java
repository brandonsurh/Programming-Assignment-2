
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

public class PA2_part2 {

	

	public static void main (String [] args) {

		Random rand = new Random();
		long start = System.currentTimeMillis();
		long end = start + 30*1000;
		

		Scanner sc = new Scanner(System.in);  
		System.out.println("Enter number of guests: ");
		int totalGuests = sc.nextInt();

		Guest [] guests = new Guest[totalGuests];

		// array to check if thread was already started or not
        Boolean [] started = new Boolean[totalGuests];
        Arrays.fill(started, Boolean.FALSE);
		SpinLock lock = new SpinLock();

		for (int i = 0; i < totalGuests; i++)
		{
			System.out.println("time is " + System.currentTimeMillis() + " and end is " + end);
			if (System.currentTimeMillis() > end)
				System.exit(0);
			
			guests[i] = new Guest(i, lock);
		}

		for (int i = 0; i < totalGuests; i++)
		{
			System.out.println("time is " + System.currentTimeMillis() + " and end is " + end);
			if (System.currentTimeMillis() > end)
				System.exit(0);
			
			guests[i].start();
		}

		while (true)
		{
			System.out.println("time is " + System.currentTimeMillis() + " and end is " + end);
			if (System.currentTimeMillis() > end)
				System.exit(0);
			guests[rand.nextInt(totalGuests - 1)].run();
		}
		
	}
}



class Guest extends Thread {

	//private boolean sawVase;
	private int threadNumber;
	private int queueChance;
	SpinLock lock;

	Random rand = new Random();

	// constructor
	public Guest (int threadNumber, SpinLock lock)
	{
		//this.sawVase = false;
		this.queueChance = 2;
		this.lock = lock;
		this.threadNumber = threadNumber;

	}

	public void run() 
	{
		
		// if random number 1-100 is more than queue chance times 100 (100, 50, 25, )
		//System.out.println("running thread " + threadNumber);
		if (rand.nextInt(100* queueChance) < 50 && queueChance < 1024)
		{
			//System.out.println("Guest " + threadNumber + " check room. Queue chance is " + queueChance);
			queueChance = queueChance * 2;
			// tries lock
			lock.lock();
			//System.out.println("Guest " + threadNumber + " locked room");
			//System.out.println("Hooray! Guest " + threadNumber + " is in the room. Next chance is " + queueChance);
			lock.unlock();
			//System.out.println("Guest " + threadNumber + " unlocked");
		}
		//else
			//System.out.println("did not pass check");

	}
}

class SpinLock extends ReentrantLock{

	public SpinLock() {
		super();
	}

	public void lock() {
		while (!super.tryLock())
		{
			//System.out.println("I am waiting");
			// Do Nothing
		}
	}

	public void unlock()
	{
		super.unlock();
	}
}
