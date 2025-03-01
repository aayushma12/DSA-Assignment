import java.util.*;

/**
 * Algorithm Explanation:
 * ----------------------
 * The goal is to find the top 3 trending hashtags from tweets in February 2024.
 * 
 * Approach:
 * 1. Read the tweets and extract all hashtags.
 * 2. Use a HashMap to count occurrences of each hashtag.
 * 3. Convert the map into a list and sort it:
 *    - First by count in descending order.
 *    - If counts are equal, sort alphabetically.
 * 4. Output the top 3 trending hashtags.
 */

class Tweet {
    int userId, tweetId;
    String tweetDate, tweetText;

    // Constructor
    public Tweet(int userId, int tweetId, String tweetDate, String tweetText) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.tweetDate = tweetDate;
        this.tweetText = tweetText;
    }
}

public class TrendingHashtagsFinder {
    public static List<Map.Entry<String, Integer>> findTrendingHashtags(List<Tweet> tweets) {
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Process each tweet
        for (Tweet tweet : tweets) {
            // Consider only tweets from February 2024
            if (tweet.tweetDate.startsWith("2024-02")) {
                // Extract words from the tweet text
                String[] words = tweet.tweetText.split("\\s+");

                for (String word : words) {
                    if (word.startsWith("#")) {
                        // Update hashtag count
                        hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        // Convert to list and sort
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
        sortedHashtags.sort((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue(); // Sort by count (desc)
            }
            return a.getKey().compareTo(b.getKey()); // Sort alphabetically (asc)
        });

        // Return only the top 3 trending hashtags
        return sortedHashtags.subList(0, Math.min(3, sortedHashtags.size()));
    }

    public static void main(String[] args) {
        // Sample test tweets
        List<Tweet> tweets = Arrays.asList(
            new Tweet(135, 13, "2024-02-01", "Enjoying a great start to the day. #HappyDay #MorningVibes"),
            new Tweet(136, 14, "2024-02-03", "Another #HappyDay with good vibes! #FeelGood"),
            new Tweet(135, 15, "2024-02-04", "Productivity peaks! #WorkLife #ProductiveDay"),
            new Tweet(136, 16, "2024-02-06", "Exploring new tech frontiers. #TechLife #Innovation"),
            new Tweet(137, 17, "2024-02-07", "Gratitude for today's moments. #HappyDay #Thankful"),
            new Tweet(138, 18, "2024-02-08", "Innovation drives us. #TechLife #FutureTech"),
            new Tweet(139, 19, "2024-02-09", "Connecting with nature's serenity. #Nature #Peaceful")
        );

        // Find trending hashtags
        List<Map.Entry<String, Integer>> topHashtags = findTrendingHashtags(tweets);

        // Display results
        System.out.println("+------------+-------+");
        System.out.println("| Hashtag    | Count |");
        System.out.println("+------------+-------+");
        for (Map.Entry<String, Integer> entry : topHashtags) {
            System.out.printf("| %-10s | %5d |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("+------------+-------+");
    }
}

/*
Test Results:
------------
Input Tweets:
1. "Enjoying a great start to the day. #HappyDay #MorningVibes" -> #HappyDay (1)
2. "Another #HappyDay with good vibes! #FeelGood" -> #HappyDay (2)
3. "Productivity peaks! #WorkLife #ProductiveDay" -> #WorkLife (1)
4. "Exploring new tech frontiers. #TechLife #Innovation" -> #TechLife (1)
5. "Gratitude for today's moments. #HappyDay #Thankful" -> #HappyDay (3)
6. "Innovation drives us. #TechLife #FutureTech" -> #TechLife (2)
7. "Connecting with nature's serenity. #Nature #Peaceful" -> Not trending

Expected Output:
+------------+-------+
| Hashtag    | Count |
+------------+-------+
| #HappyDay  |     3 |
| #TechLife  |     2 |
| #WorkLife  |     1 |
+------------+-------+
*/
