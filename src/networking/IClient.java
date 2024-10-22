package networking;

import exceptions.NetworkingException;

/**
 * Interface for client-side networking.
 */
public interface IClient {

    /**
     * Connect to a server.
     * @param ip The IP address of the server.
     * @param port The port of the server.
     */
    void connectToServer(String ip, int port) throws NetworkingException;

    /**
     * Send a message to the server.
     * @param msg The message to send.
     */
    void sendMessage(String msg) throws NetworkingException;

    /**
     * Receive a message from the server.
     * @return The message received.
     */
    String receiveMessage() throws NetworkingException;

    /**
     * Checks if it's the client's turn to send a message.
     * @return True if it's the client's turn to send a message, false otherwise.
     */
    boolean serverWaitingForInput();

    /**
     * Check if the connection is still alive.
     * @return True if the connection is still alive, false otherwise.
     */
    boolean isConnectionAlive();
}
