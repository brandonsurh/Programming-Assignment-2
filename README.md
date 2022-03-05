# Programming-Assignment-2

Problem 1:
My solution uses two main types of threads. I named them primary and secondary. The primary thread accounts for all of the guests except for one. These types of guests will eat the cake once but never twice. The secondary thread will count how many times it sees that there is a missing cake and request another. When the secondary thread sees that it has requested totalGuests - 1, then it will know that all of the guests have eaten cake and that all of the guests have been in the maze. Then it will announce this to the minotaur and exit the program. To simulate this further, I have added a chance functionality to see if the guest has made it to the end of the maze and a chance functionality to figure out which thread the minotaur picks to enter the maze.






Which of these three strategies should the guests choose? Please discuss the advantages and disadvantages.

For the second problem I chose to use the first approach. This approach is not very efficient on large sets but it is simple. It is not efficient with a large amount of threads since spinlocks waste resources on busy waiting as opposed to being able to process things elsewhere. Due to continuously checking to see if the lock is free, threads are kept busy and these checks are usually unnecessary. The advantage to this approach is that it is very simple and with small wait times it can be efficient.

The second approach is that there is no busy waiting from continuous checking like the first approach. Instead, flags can be used to indicated when the lock is free allowing threads to be useful during the waiting times. This approach is useful when wait times are long since resources won't be wasted due to waiting. The downside to this is that if wait times are short then it won't be as quick as the first approach since threads won't be at the ready to grab the lock.

The third approach is the most adaptable due to its queue trait. With this approach, threads can be prepared to grab the lock when the other is done and so time isn't wasted waiting for a thread to grab the lock. This also saves processing power and resources by not checking continuously to see if the lock is free like the first approach does. This is the most complex approach of the three but allows for good performance when dealing with medium to large sets of threads and works well with medium to long wait times.
