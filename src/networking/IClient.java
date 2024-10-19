package networking;

/**
 * Interface for client-side networking.
 */
public interface IClient {

    /**
     * Connect to a server.
     * @param ip The IP address of the server.
     * @param port The port of the server.
     */
    void connectToServer(String ip, int port);

    /**
     * Send a message to the server.
     * @param msg The message to send.
     */
    void sendMessage(String msg);

    /**
     * Receive a message from the server.
     * @return The message received.
     */
    String receiveMessage();
}
