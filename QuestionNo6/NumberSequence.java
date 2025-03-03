/*
Algorithm Explanation:
1. We have three threads: 
   - ZeroThread: Prints "0".
   - EvenThread: Prints even numbers.
   - OddThread: Prints odd numbers.
2. We use Semaphores to synchronize the threads:
   - zeroSemaphore (starts unlocked) allows the ZeroThread to print first.
   - oddSemaphore and evenSemaphore start locked and are released alternately.
3. Execution Order:
   - ZeroThread prints "0", then releases either evenSemaphore or oddSemaphore.
   - The corresponding thread prints the number and releases zeroSemaphore again.
   - This ensures correct interleaved execution: "0102030405..."
*/

import java.util.concurrent.Semaphore; // Import Semaphore for thread synchronization

// Class responsible for printing numbers
class NumberPrinter {
    public void printZero() {
        System.out.print(0); // Prints "0"
    }

    public void printEven(int num) {
        System.out.print(num); // Prints even numbers
    }

    public void printOdd(int num) {
        System.out.print(num); // Prints odd numbers
    }
}

// Controller class to manage threads
class ThreadController {
    private int n; // Upper limit for sequence
    private Semaphore zeroSemaphore = new Semaphore(1); // Starts unlocked for ZeroThread
    private Semaphore oddSemaphore = new Semaphore(0);  // Starts locked, unlocks for OddThread
    private Semaphore evenSemaphore = new Semaphore(0); // Starts locked, unlocks for EvenThread
    private NumberPrinter printer; // Instance of NumberPrinter

    // Constructor to initialize values
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    // Thread for printing 0 before each number
    public void zeroThread() {
        try {
            for (int i = 1; i <= n; i++) { 
                zeroSemaphore.acquire();  // Wait for permission to print "0"
                printer.printZero();      // Print "0"
                if (i % 2 == 0) {
                    evenSemaphore.release(); // Unlock EvenThread
                } else {
                    oddSemaphore.release(); // Unlock OddThread
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Thread for printing even numbers
    public void evenThread() {
        try {
            for (int i = 2; i <= n; i += 2) { 
                evenSemaphore.acquire(); // Wait until evenSemaphore is unlocked
                printer.printEven(i);    // Print even number
                zeroSemaphore.release(); // Unlock ZeroThread for next iteration
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Thread for printing odd numbers
    public void oddThread() {
        try {
            for (int i = 1; i <= n; i += 2) { 
                oddSemaphore.acquire(); // Wait until oddSemaphore is unlocked
                printer.printOdd(i);    // Print odd number
                zeroSemaphore.release(); // Unlock ZeroThread for next iteration
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Main class to execute the program
public class NumberSequence {
    public static void main(String[] args) {
        int n = 5; // Test case: Print sequence up to 5
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);

        // Creating threads
        Thread zeroThread = new Thread(controller::zeroThread);
        Thread evenThread = new Thread(controller::evenThread);
        Thread oddThread = new Thread(controller::oddThread);

        // Starting threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}

/*
Test Case:
Input: n = 5
Expected Output: 0102030405

How It Works:
1. ZeroThread starts and prints "0".
2. OddThread prints "1".
3. ZeroThread prints "0" again.
4. EvenThread prints "2".
5. ZeroThread prints "0".
6. OddThread prints "3".
7. ZeroThread prints "0".
8. EvenThread prints "4".
9. ZeroThread prints "0".
10. OddThread prints "5".
Final output: 0102030405
*/
