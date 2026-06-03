import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents one distributed leaderboard node.
 *
 * Each node stores replicated leaderboard data.
 */
public class LeaderboardNode {

    // Node identifier
    private String nodeName;

    // Replicated leaderboard storage
    private ConcurrentHashMap<String, Integer> leaderboard;

    // Replication manager reference
    private ReplicationManager replicationManager;

    // Node active status
    private boolean active;

    /**
     * Constructor initializes node.
     *
     * @param nodeName Name of node
     */
    public LeaderboardNode(String nodeName) {
        this.nodeName = nodeName;
        this.leaderboard = new ConcurrentHashMap<>();
        this.active = true;
    }

    /**
     * Assigns replication manager to node.
     *
     * @param replicationManager Replication controller
     */
    public void setReplicationManager(ReplicationManager replicationManager) {
        this.replicationManager = replicationManager;
    }

    /**
     * Enables or disables node.
     *
     * @param active true if active
     */
    public void setActive(boolean active) {
        this.active = active;

        System.out.println(nodeName + " active status = " + active);
    }

    /**
     * Returns node name.
     *
     * @return node name
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Checks whether node is active.
     *
     * @return active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Handles player score submission.
     *
     * @param playerName Player username
     * @param score Player score
     */
    public synchronized void submitScore(String playerName, int score) {

        // Ignore requests if node failed
        if (!active) {
            System.out.println(nodeName + " is DOWN. Cannot process request.");
            return;
        }

        // Save locally
        leaderboard.put(playerName, score);

        System.out.println("[" + nodeName + "] " + playerName +
                " submitted score " + score);

        // Replicate update to other nodes
        replicationManager.replicate(playerName, score, this);
    }

    /**
     * Applies replicated update from another node.
     *
     * @param playerName Player username
     * @param score Player score
     */
    public synchronized void applyReplication(String playerName, int score) {

        // Ignore replication if node failed
        if (!active) {
            return;
        }

        leaderboard.put(playerName, score);

        System.out.println("[" + nodeName + "] replicated score for "
                + playerName + " = " + score);
    }

    /**
     * Displays leaderboard contents.
     */
    public void printLeaderboard() {

        System.out.println("\n===========================");
        System.out.println("Leaderboard at " + nodeName);
        System.out.println("===========================");

        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}