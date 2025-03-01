 /*
    Algorithm Explanation:
    ----------------------
    Problem:
    - Given two **sorted** arrays of investment returns.
    - We need to find the **k-th smallest** combined return by multiplying elements from both arrays.

    Approach:
    1. **Min-Heap (Priority Queue) Approach**:
       - Use a **min-heap** to store the smallest possible products of elements from both arrays.
       - Each heap element consists of:
         - `returns1[i] * returns2[j]` → the product
         - `i` → index from `returns1`
         - `j` → index from `returns2`
    
    2. **Initialization**:
       - Push initial products (each element of `returns1` multiplied by the **first** element of `returns2`).
       - This ensures we always process the smallest elements first.

    3. **Heap Processing**:
       - Extract the smallest product from the heap **k times**.
       - After extracting an element `(i, j)`, push the **next product** `(i, j+1)` into the heap.
       - Continue until we extract `k` elements.

    4. **Time Complexity**:
       - **O(k log n)**, where `n` is the size of the heap (at most `min(n1, k)`).

    */
    
import java.util.PriorityQueue;
import java.util.Scanner;

public class KthSmallestInvestmentReturn {

    public static int kthSmallestInvestment(int[] returns1, int[] returns2, int k) {
        // Min-Heap to store (product, i, j)
        // The heap stores an array where:
        // - First element: Product of returns1[i] and returns2[j]
        // - Second element: Index i (from returns1)
        // - Third element: Index j (from returns2)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Step 1: Push initial elements into the heap.
        for (int i = 0; i < returns1.length; i++) {
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        int result = 0; // Variable to store the k-th smallest product.

        // Step 2: Extract the smallest element from the heap k times.
        while (k-- > 0) {
            int[] current = minHeap.poll(); // Extract the smallest product from the heap.
            result = current[0]; // Store the extracted value.

            int i = current[1]; // Get the index i from returns1.
            int j = current[2]; // Get the index j from returns2.

            // Step 3: Push the next possible product from the same row in returns2.
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{returns1[i] * returns2[j + 1], i, j + 1});
            }
        }

        return result; // The k-th smallest investment return.
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: First sorted array
        System.out.print("Enter size of first array: ");
        int n1 = scanner.nextInt();
        int[] returns1 = new int[n1];

        System.out.println("Enter elements of first sorted array:");
        for (int i = 0; i < n1; i++) {
            returns1[i] = scanner.nextInt();
        }

        // Input: Second sorted array
        System.out.print("Enter size of second array: ");
        int n2 = scanner.nextInt();
        int[] returns2 = new int[n2];

        System.out.println("Enter elements of second sorted array:");
        for (int i = 0; i < n2; i++) {
            returns2[i] = scanner.nextInt();
        }

        // Input: k (target index)
        System.out.print("Enter k: ");
        int k = scanner.nextInt();

        scanner.close(); // Close scanner to prevent resource leaks

        // Compute and display the kth smallest investment return
        int result = kthSmallestInvestment(returns1, returns2, k);
        System.out.println("The " + k + "th smallest combined return: " + result);
    }
}

/*
Test Cases & Expected Output:
------------------------------
Input:
returns1 = [2, 3, 5]
returns2 = [4, 6, 8]
k = 3
Output:
8
Explanation:
The sorted products are [2×4, 2×6, 3×4, 2×8, 3×6, 5×4, 3×8, 5×6, 5×8] → [8, 12, 12, 16, 18, 20, 24, 30, 40].
The 3rd smallest is 8.

Input:
returns1 = [1, 2, 3]
returns2 = [3, 4, 5]
k = 4
Output:
6
Explanation:
Sorted products: [1×3, 1×4, 2×3, 1×5, 2×4, 3×3, 2×5, 3×4, 3×5] → [3, 4, 6, 5, 8, 9, 10, 12, 15].
The 4th smallest is 6.

Input:
returns1 = [1, 2, 3, 4]
returns2 = [2, 3, 4, 5]
k = 5
Output:
6
Explanation:
Sorted products: [1×2, 1×3, 2×2, 1×4, 2×3, 3×2, 1×5, 2×4, 3×3, 4×2, 2×5, 3×4, 4×3, 3×5, 4×4, 4×5].
The 5th smallest is 6.
*/
