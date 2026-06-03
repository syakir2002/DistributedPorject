import java.util.ArrayList;
import java.util.List;

/**
 * Handles distributed replication between nodes.
 */
public class ReplicationManager {

    // Stores all distributed nodes
    private List<LeaderboardNode> nodes;

    // Current consistency model
    private ConsistencyModel consistencyModel;

    /**
     * Constructor initializes node list.
     */
    public ReplicationManager() {
        nodes = new ArrayList<>();
    }

    /**
     * Adds node to distributed system.
     *
     * @param node Leaderboard node
     */
    public void addNode(LeaderboardNode node) {
        nodes.add(node);
    }

    /**
     * Sets replication consistency model.
     *
     * @param consistencyModel Consistency strategy
     */
    public void setConsistencyModel(ConsistencyModel consistencyModel) {
        this.consistencyModel = consistencyModel;

        System.out.println("Consistency Model = " + consistencyModel);
    }

    /**
     * Replicates score update across nodes.
     *
     * Sequential Consistency:
     * - Immediate ordered replication
     *
     * Eventual Consistency:
     * - Delayed asynchronous replication
     *
     * @param playerName Player username
     * @param score Player score
     * @param sourceNode Node initiating replication
     */
    public void replicate(String playerName,
                          int score,
                          LeaderboardNode sourceNode) {

        for (LeaderboardNode node : nodes) {

            // Skip source node
            if (node == sourceNode) {
                continue;
            }

            // Sequential consistency
            if (consistencyModel == ConsistencyModel.SEQUENTIAL) {

                node.applyReplication(playerName, score);
            }

            // Eventual consistency
            else if (consistencyModel == ConsistencyModel.EVENTUAL) {

                new Thread(() -> {

                    try {

                        // Artificial network delay
                        Thread.sleep(2000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    node.applyReplication(playerName, score);

                }).start();
            }
        }
    }
}