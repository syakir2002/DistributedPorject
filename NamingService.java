import java.util.HashMap;
import java.util.Map;

/**
 * Enum to distinguish between Naming Models required by the project.
 */
enum NamingModelType {
    FLAT,
    STRUCTURED
}

/**
 * NamingService handles both Flat (Hash Table) and Structured (Hierarchical/DNS) 
 * resolution as required by the assignment guidelines.
 */
public class NamingService {

    private NamingModelType activeModel;

    // 1. Flat Naming Storage (Simple Hash Table)
    private Map<String, LeaderboardNode> flatRegistry;

    // 2. Structured Naming Storage (Hierarchical Tree Structure for DNS)
    // Structure: Top-Level Domain (e.g., "asia") -> Node/Service Map
    private Map<String, Map<String, LeaderboardNode>> structuredRegistry;

    /**
     * Constructor initializes both naming structures.
     */
    public NamingService(NamingModelType model) {
        this.activeModel = model;
        this.flatRegistry = new HashMap<>();
        this.structuredRegistry = new HashMap<>();
        System.out.println("[NamingService] Initialized using " + model + " model.");
    }

    /**
     * Registers a service name based on the active naming model strategy.
     */
    public void registerService(String serviceName, LeaderboardNode node) {
        if (activeModel == NamingModelType.FLAT) {
            // Flat strategy: Direct mapping
            flatRegistry.put(serviceName, node);
            System.out.println("[Flat Naming] Registered: " + serviceName);
        } 
        else if (activeModel == NamingModelType.STRUCTURED) {
            // Structured strategy: Break down "leaderboard.asia" into ["leaderboard", "asia"]
            String[] parts = serviceName.split("\\.");
            if (parts.length == 2) {
                String hostname = parts[0]; // "leaderboard"
                String tld = parts[1];      // "asia"

                // If Top-Level Domain doesn't exist, create its zone map
                structuredRegistry.putIfAbsent(tld, new HashMap<>());
                // Add hostname into that specific TLD zone
                structuredRegistry.get(tld).put(hostname, node);
                
                System.out.println("[Structured DNS] Registered: Host '" + hostname + "' inside Domain '." + tld + "'");
            } else {
                // Fallback if formatting doesn't use dots
                flatRegistry.put(serviceName, node);
            }
        }
    }

    /**
     * Resolves a service name into a node using flat or hierarchical lookups.
     */
    public LeaderboardNode resolve(String serviceName) {
        System.out.println("[NamingService] Resolving: " + serviceName);

        if (activeModel == NamingModelType.FLAT) {
            // Flat lookup: O(1) single map retrieval
            return flatRegistry.get(serviceName);
        } 
        else {
            // Structured lookup: Simulating hierarchical tree traversal
            String[] parts = serviceName.split("\\.");
            if (parts.length == 2) {
                String hostname = parts[0];
                String tld = parts[1];

                if (structuredRegistry.containsKey(tld)) {
                    System.out.println("[Structured DNS] Traversing tree: Root -> ." + tld + " -> " + hostname);
                    return structuredRegistry.get(tld).get(hostname);
                }
            }
            return flatRegistry.get(serviceName); // Fallback
        }
    }
}