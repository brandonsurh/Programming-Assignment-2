

// create class for threads
// PRIMARY CLASS
//  - chance to reach end
//  - eats cake only one time when reaching end
//  - after cake was eaten once, dont eat anymore
// make secondary class for volunteer thread who will change cakes
// SECONDARY CLASS
//  - only person to request cakes
//  - counts amount of times cake is requested
//  - tells minotaur everyone has visited when requests reachs n-1 guests (request guest amount of times -1)
//  - prints that everyone has visited and possibly ends program

// threads will have probability to find way out of maze and reach cake

// MAIN
// start n-1 primary threads
// start 1 secondary thread
// all threads are sleeping
// minotaur wakes one random thread at a time
// simulation only stops when secondary class announces everyone has visited

import java.util.*;

public class PA2 {

    

    public static void main (String [] args) {

        Random rand = new Random();

        Scanner sc = new Scanner(System.in);  

        // prompts for number of guests
        System.out.println("Enter number of guests: ");
        int totalGuests = sc.nextInt();

        // array to check if thread was already started or not
        Boolean [] started = new Boolean[totalGuests];
        Arrays.fill(started, Boolean.FALSE);

        // creates primary guests array and one designated counter
        PrimaryGuest [] primGuests = new PrimaryGuest[totalGuests - 1];
        SecondaryGuest secGuest = new SecondaryGuest(totalGuests);

        // create primary guest threads
        for (int i = 0; i < totalGuests - 1; i++)
        {
            primGuests[i] = new PrimaryGuest(i);
        }

        int randomNumber;

        // while some threads havent been started, keep trying to start threads
        // makes sure all threads get once
        while (Arrays.asList(started).contains(Boolean.FALSE) == true)
        {
            // selects random guest to check
            randomNumber = rand.nextInt(totalGuests);

            // designated thread, has it been started yet?
            if (randomNumber == totalGuests - 1 && Arrays.asList(started).get(randomNumber) == false)
            {
                secGuest.start();
                try{
                    secGuest.join();
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                // set flag to started
                started[randomNumber] = Boolean.TRUE;
            }
            // primary thread, has it been started yet?
            else if (randomNumber != totalGuests - 1 && Arrays.asList(started).get(randomNumber) == false)
            {
                primGuests[randomNumber].start();
                try{
                    primGuests[randomNumber].join();
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                // set flag to started
                started[randomNumber] = Boolean.TRUE;
            }
        }

        // while loop to keep running threads
        // minotaur choosing guests to enter maze
        while (true)
        {
            // random selection
            randomNumber = rand.nextInt(totalGuests);

            // if designated counter is chosen
            if (randomNumber == totalGuests - 1)
            {
                // run thread
                secGuest.run();
                try{   
                    secGuest.join();
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            // if primary guest is chosen
            else
            {
                // run thread
                primGuests[randomNumber].run();
                try{   
                    primGuests[randomNumber].join();
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

class Labyrinth {
    public static boolean isCakePresent = true;
}

// these are the guests who eat cake
class PrimaryGuest extends Thread {

    Random rand = new Random();

    // name 
    private int threadNumber;
    // did they have cake? (debug purposes)
    private boolean hadCake;

    // constructor
    public PrimaryGuest (int threadNumber)
    {
        this.threadNumber = threadNumber;
        this.hadCake = false;
    }

    @Override
    public void run()
    {

        // decides if guest makes it to end of maze
        int randomNumber = rand.nextInt(10);

        // guest reaches end
        if (randomNumber > 2)
        {
            // if guest did not have cake and cake is there, eat cake
            if (hadCake == false && Labyrinth.isCakePresent == true)
            {
                // eat cake, set true 
                this.hadCake = true;
                // set atomic variable? for cake to be missing state
                Labyrinth.isCakePresent = false;
            }
        }
    }
}

// this is the designated guest who counts cakes eaten
class SecondaryGuest extends Thread {

    Random rand = new Random();

    // used to count how many have visited (ate cake)
    private int requestCount;
    // how many guests are there
    private int totalGuests;

    // constructor given amount of total guests
    public SecondaryGuest(int totalGuests)
    {
        // set amount of requests to zero
        this.requestCount = 0;
        this.totalGuests = totalGuests;
    }

    @Override
    public void run()
    {

        // decides if guest makes it to end of maze
        int randomNumber = rand.nextInt(10);

        // guest reaches end
        if (randomNumber > 2)
        {
            // if there is no cake, someone ate it
            // request more cake, increment
            if (Labyrinth.isCakePresent == false)
            {
                // request more cake, increment
                requestCount++;
                Labyrinth.isCakePresent = true;
            }
        }

        // if requestCount is equal to n-1, then all guests have had cake and all have visited
        if (requestCount == totalGuests - 1)
        {
            System.out.println("All guests have visited the labyrinth!");
            // needs to shutdown program
            System.exit(0);
        }
    }
}

