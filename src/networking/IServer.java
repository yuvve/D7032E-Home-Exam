package networking;

import exceptions.NetworkingException;

/**
 * Interface for server-side networking.
 */
public interface IServer {

    /**
     * Start the server on the specified port.
     * @param port The port to start the server on.
     */
    void startServer(int port) throws NetworkingException;

    /**
     * Wait for a client to connect.
     * @return The client's ID.
     */
    int acceptClient() throws NetworkingException;

    /**
     * Broadcast a message to all clients.
     * @param msg The message to broadcast.
     */
    void broadcast(String msg) throws NetworkingException;

    /**
     * Send a message to a specific client.
     * @param clientId The client's ID.
     * @param msg The message to send.
     */
    void sendMsg(int clientId, String msg) throws NetworkingException;

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
