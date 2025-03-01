/* 
Algorithm Explanation:
----------------------
Problem:
- We have a material with 'n' temperature levels and 'k' identical samples.
- The goal is to determine the critical temperature 'f' where the material changes its properties.
- We must find 'f' using the **minimum number of measurements**.

Approach:
1. **Dynamic Programming (DP) Approach**:
   - Define `dp[m][x]` as the **maximum number of temperature levels** that can be checked with:
     - `m` measurements 
     - `x` available samples.
   - The recurrence relation used:
     - `dp[m][x] = dp[m-1][x-1] + dp[m-1][x] + 1`
     - Explanation:
       - `dp[m-1][x-1]` → Case when the material **reacts** (we check **lower temperatures**).
       - `dp[m-1][x]` → Case when the material **does not react** (we check **higher temperatures**).
       - `+1` accounts for the **current measurement**.

2. **Finding the Minimum Measurements (m)**:
   - Start with `m = 0` (no measurements made).
   - Increase `m` until `dp[m][k] >= n`, ensuring all temperature levels are covered.
   - Return `m` as the **minimum number of measurements required**.

Time Complexity:
- The solution runs in **O(k log n)**, which is efficient for reasonable constraints.

*/

import java.util.Scanner;

public class CriticalTemperatureFinder {
    
    /**
     * Function to compute the minimum number of measurements needed
     * to determine the critical temperature 'f' using 'k' samples and 'n' temperature levels.
     * 
     * @param k Number of identical samples
     * @param n Number of temperature levels
     * @return Minimum number of measurements required
     */
    public static int minMeasurements(int k, int n) {
        // Edge case: If there are no temperature levels, no measurements are needed.
        if (n == 0) return 0;

        // Create a DP table:
        // dp[m][x] represents the maximum number of temperature levels that can be tested
        int[][] dp = new int[n + 1][k + 1];

        int m = 0; // Counter for the number of measurements
        
        // Keep testing until we can check at least 'n' temperature levels
        while (dp[m][k] < n) {
            m++; // Increase the number of measurements
            
            for (int x = 1; x <= k; x++) { // Iterate over available samples
                // Apply the recurrence relation:
                // If material reacts (sample breaks), check the lower temperatures: dp[m-1][x-1]
                // If material does not react (sample does not break), check the higher temperatures: dp[m-1][x]
                // +1 accounts for the current measurement
                dp[m][x] = dp[m - 1][x - 1] + dp[m - 1][x] + 1;
            }
        }
        
        return m; // Minimum number of measurements required
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Input: Number of samples (k)
        System.out.print("Enter the number of samples (k): ");
        int k = scanner.nextInt();
        
        // Input: Number of temperature levels (n)
        System.out.print("Enter the number of temperature levels (n): ");
        int n = scanner.nextInt();
        
        scanner.close(); // Close scanner to prevent resource leaks
        
        // Compute and display the minimum measurements required
        System.out.println("Minimum measurements required: " + minMeasurements(k, n));
    }
}

/*
Test Cases & Expected Output:
------------------------------
Input: k = 1, n = 2  
Output: 2  
Explanation: We check each level linearly, as only one sample is available.

Input: k = 2, n = 6  
Output: 3  
Explanation: Optimized search using 2 samples, similar to a binary search approach.

Input: k = 3, n = 14  
Output: 4  
Explanation: Efficiently finds f in fewer steps by dividing the search space.

Input: k = 3, n = 25  
Output: 5  
Explanation: Further optimized as we have more samples.

Input: k = 4, n = 40  
Output: 5  
Explanation: With more samples, fewer measurements are required.

*/
