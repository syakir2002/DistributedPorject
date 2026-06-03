/**
 * Enum representing supported consistency models.
 */
public enum ConsistencyModel {

    // All replicas updated immediately in order
    SEQUENTIAL,

    // Replicas updated asynchronously with delay
    EVENTUAL
}