import java.util.Random;

/**
 * Simulates a game player.
 *
 * Each player runs as a separate thread.
 */
public class PlayerClient implements Runnable {

    // Player username
    private String playerName;

    // Target leaderboard node
    private LeaderboardNode targetNode;

    // Random score generator
    private Random random;

    /**
     * Constructor initializes player.
     *
     * @param playerName Username
     * @param targetNode Server node
     */
    public PlayerClient(String playerName,
                        LeaderboardNode targetNode) {

        this.playerName = playerName;
        this.targetNode = targetNode;
        this.random = new Random();
    }

    /**
     * Executes player simulation.
     *
     * Player generates random score and submits it.
     */
    @Override
    public void run() {

        try {

            // Simulate gameplay delay
            Thread.sleep(random.nextInt(3000));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Generate score
        int score = random.nextInt(10000);

        // Submit score
        targetNode.submitScore(playerName, score);
    }
}
