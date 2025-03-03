/**
 * Algorithm Explanation:
 * 1. The problem requires finding the lexicographically smallest pair (i, j) 
 *    such that the Manhattan distance between (x[i], y[i]) and (x[j], y[j]) is minimized.
 * 
 * 2. The Manhattan distance formula is:
 *       |x[i] - x[j]| + |y[i] - y[j]|
 *
 * 3. Approach:
 *    - We use a **brute-force** approach by checking all pairs (i, j) where i < j.
 *    - Compute the Manhattan distance for each pair.
 *    - Maintain a variable `minDistance` to track the smallest distance found.
 *    - If a new minimum is found, update the result.
 *    - If multiple pairs have the same minimum distance, select the lexicographically smallest one.
 *
 * 4. The final result is returned as an array `[i, j]`.
 * 
 * 5. Complexity Analysis:
 *    - **Time Complexity**: O(n²), since we are checking all possible pairs.
 *    - **Space Complexity**: O(1), as only a few variables are used.
 */

import java.util.Arrays;


public class ClosestPair {
    
    /**
     * Method to find the lexicographically smallest pair with the minimum Manhattan distance.
     * @param x_coords - Array of x-coordinates
     * @param y_coords - Array of y-coordinates
     * @return An array containing the indices of the closest pair.
     */
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Get the total number of points
        int minDistance = Integer.MAX_VALUE; // Initialize minDistance with a large value
        int[] result = new int[2]; // Array to store the indices of the closest pair

        // Iterate over all pairs (i, j) where i < j
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                
                // Calculate Manhattan distance between point i and point j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                /**
                 * Check if we should update the result:
                 * - If the current distance is smaller than minDistance, update.
                 * - If the distance is the same, check for the lexicographically smaller (i, j).
                 */
                if (distance < minDistance || 
                   (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    
                    minDistance = distance; // Update minDistance
                    result[0] = i; // Store the first index of the closest pair
                    result[1] = j; // Store the second index of the closest pair
                }
            }
        }
        
        return result; // Return the indices of the closest pair
    }

    public static void main(String[] args) {
        // Test Case 1
        int[] x_coords1 = {1, 2, 3, 2, 4};
        int[] y_coords1 = {2, 3, 1, 2, 3};
        System.out.println("Test Case 1 Output: " + Arrays.toString(findClosestPair(x_coords1, y_coords1)));
        // Expected Output: [0, 3]

        // Test Case 2
        int[] x_coords2 = {4, 1, 2, 3};
        int[] y_coords2 = {3, 2, 3, 1};
        System.out.println("Test Case 2 Output: " + Arrays.toString(findClosestPair(x_coords2, y_coords2)));
        // Expected Output: [1, 2]

        // Test Case 3
        int[] x_coords3 = {5, 6, 7, 8};
        int[] y_coords3 = {2, 2, 2, 2};
        System.out.println("Test Case 3 Output: " + Arrays.toString(findClosestPair(x_coords3, y_coords3)));
        // Expected Output: [0, 1]

        // Test Case 4 (Edge case: only two points)
        int[] x_coords4 = {0, 1};
        int[] y_coords4 = {0, 1};
        System.out.println("Test Case 4 Output: " + Arrays.toString(findClosestPair(x_coords4, y_coords4)));
        // Expected Output: [0, 1]

        // Test Case 5 (Edge case: all points at same location)
        int[] x_coords5 = {2, 2, 2, 2};
        int[] y_coords5 = {3, 3, 3, 3};
        System.out.println("Test Case 5 Output: " + Arrays.toString(findClosestPair(x_coords5, y_coords5)));
        // Expected Output: [0, 1]
    }
}

/**
 * Test Case Results:
 * -------------------------
 * Test Case 1:
 * Input:  x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3]
 * Output: [0, 3]
 * -------------------------
 * Test Case 2:
 * Input:  x_coords = [4, 1, 2, 3], y_coords = [3, 2, 3, 1]
 * Output: [1, 2]
 * -------------------------
 * Test Case 3:
 * Input:  x_coords = [5, 6, 7, 8], y_coords = [2, 2, 2, 2]
 * Output: [0, 1]
 * -------------------------
 * Test Case 4 (Edge Case: Only two points):
 * Input:  x_coords = [0, 1], y_coords = [0, 1]
 * Output: [0, 1]
 * -------------------------
 * Test Case 5 (Edge Case: All points same location):
 * Input:  x_coords = [2, 2, 2, 2], y_coords = [3, 3, 3, 3]
 * Output: [0, 1]
 */
