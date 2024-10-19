package networking;

/**
 * Interface for server-side networking.
 */
public interface IServer {

    /**
     * Start the server on the specified port.
     * @param port The port to start the server on.
     */
    void startServer(int port);

    /**
     * Wait for a client to connect.
     * @return The client's ID.
     */
    int acceptClient();

    /**
     * Broadcast a message to all clients.
     * @param msg The message to broadcast.
     */
    void broadcast(String msg);

    /**
     * Send a message to a specific client.
     * @param clientId The client's ID.
     * @param msg The message to send.
     */
    void sendMsg(int clientId, String msg);

    /**
     * Get the input from a specific client.
     * @param clientId The client's ID.
     * @return The input from the client.
     */
    String getClientInput(int clientId);

    /**
     * Remove a client from the server.
     * @param clientId The client's ID.
     */
    void removeClient(int clientId);

    /**
     * Get the number of clients connected to the server.
     * @return The number of clients connected to the server.
     */
    int getClientCount();
}
