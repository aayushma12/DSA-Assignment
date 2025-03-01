/*
Algorithm Explanation:
This program finds the lexicographically smallest pair of points (i, j) with the smallest Manhattan distance.
1. Compute the absolute difference in x-coordinates and y-coordinates.
2. Find the pair with the smallest sum of absolute differences.
3. If multiple pairs have the same distance, choose the lexicographically smallest one.
*/

import java.util.*;

public class ClosestPair {
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] result = new int[2];
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
                
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};
        System.out.println(Arrays.toString(findClosestPair(x_coords, y_coords))); // Output: [0, 3]
    }
}

/*
Testing Results:
Test Case: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3]
- Expected Output: [0, 3]
- Actual Output: [0, 3]

All test cases passed successfully.
*/
