/*
Algorithm Explanation:
This program determines the minimum number of rewards needed to distribute to employees based on their performance ratings.
1. Every employee must receive at least one reward.
2. Employees with a higher rating than their adjacent colleagues must receive more rewards.
3. We use a two-pass approach:
   - First, traverse from left to right, ensuring each employee gets more rewards than the previous if their rating is higher.
   - Second, traverse from right to left, ensuring each employee gets more rewards than the next if their rating is higher.
4. The sum of all allocated rewards is the answer.
*/

import java.util.*;

public class EmployeeRewards {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); // Each employee gets at least one reward
        
        // Left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        return Arrays.stream(rewards).sum(); // Sum of all rewards
    }
    
    public static void main(String[] args) {
        // Example test cases
        int[] ratings1 = {1, 0, 2};
        System.out.println(minRewards(ratings1)); // Output: 5
        
        int[] ratings2 = {1, 2, 2};
        System.out.println(minRewards(ratings2)); // Output: 4
    }
}

/*
Testing Results:
Test Case 1: ratings = [1, 0, 2]
- Expected Output: 5
- Actual Output: 5

Test Case 2: ratings = [1, 2, 2]
- Expected Output: 4
- Actual Output: 4

Both test cases passed successfully.
*/
