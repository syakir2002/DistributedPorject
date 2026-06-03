import java.util.*;
import java.util.concurrent.*;

/**
 * Main class used to run the distributed leaderboard simulation.
 */

public class main {
    public static void main(String[] args) throws Exception {

        // Create naming service
        // To test Flat Naming (Your original method):
        //NamingService namingService = new NamingService(NamingModelType.FLAT);

// OR 

// To test Structured Naming (The required hierarchical method):
        NamingService namingService = new NamingService(NamingModelType.STRUCTURED);

        // Create leaderboard nodes
        LeaderboardNode nodeA = new LeaderboardNode("Node-A");
        LeaderboardNode nodeB = new LeaderboardNode("Node-B");
        LeaderboardNode nodeC = new LeaderboardNode("Node-C");

        // Register nodes in naming service
        namingService.registerService("leaderboard.asia", nodeA);
        namingService.registerService("leaderboard.eu", nodeB);
        namingService.registerService("leaderboard.us", nodeC);

        // Create replication manager
        ReplicationManager replicationManager = new ReplicationManager();

        // Add nodes to replication manager
        replicationManager.addNode(nodeA);
        replicationManager.addNode(nodeB);
        replicationManager.addNode(nodeC);

        // Set consistency model
        replicationManager.setConsistencyModel(ConsistencyModel.SEQUENTIAL);

        // Connect replication manager to nodes
        nodeA.setReplicationManager(replicationManager);
        nodeB.setReplicationManager(replicationManager);
        nodeC.setReplicationManager(replicationManager);

        // Create player thread pool
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Simulate 20 players
        for (int i = 1; i <= 20; i++) {
            String playerName = "Player-" + i;

            // Resolve service using naming system
            LeaderboardNode targetNode = namingService.resolve("leaderboard.asia");

            // Create player client
            PlayerClient client = new PlayerClient(playerName, targetNode);

            executor.submit(client);
        }

        // Shutdown thread pool
        executor.shutdown();

        // Wait until all tasks complete
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // Wait for replication to complete
        Thread.sleep(5000);

        // Display leaderboards
        nodeA.printLeaderboard();
        nodeB.printLeaderboard();
        nodeC.printLeaderboard();

        // Simulate node failure
        System.out.println("\n===========================");
        System.out.println("SIMULATING NODE FAILURE");
        System.out.println("===========================\n");

        nodeB.setActive(false);

        // Additional players submit scores
        ExecutorService secondRound = Executors.newFixedThreadPool(5);

        for (int i = 21; i <= 30; i++) {
            String playerName = "Player-" + i;

            // FIX: Have half the players try to hit the dead Europe server
            String region = (i % 2 == 0) ? "leaderboard.asia" : "leaderboard.eu";
            
            LeaderboardNode targetNode = namingService.resolve(region);

            PlayerClient client = new PlayerClient(playerName, targetNode);

            secondRound.submit(client);
        }

        secondRound.shutdown();
        secondRound.awaitTermination(30, TimeUnit.SECONDS);

        Thread.sleep(5000);

        // Display leaderboards again
        nodeA.printLeaderboard();
        nodeB.printLeaderboard();
        nodeC.printLeaderboard();

        System.out.println("\nSimulation completed successfully.");
    }
}

