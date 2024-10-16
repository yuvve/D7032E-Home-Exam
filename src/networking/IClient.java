package networking;

/// <summary>
/// Interface for client-side networking.
/// </summary>
public interface IClient {
    void connectToServer(String ip, int port);
    void sendMessage(String msg);
    String receiveMessage();
}
