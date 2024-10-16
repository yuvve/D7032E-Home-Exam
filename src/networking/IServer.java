package networking;

import java.net.Socket;

/// <summary>
/// Interface for server-side networking.
/// </summary>
public interface IServer {
    void startServer(int port);
    void broadcast(String msg);
    void sendMsg(Socket client, String msg);
    String getClientInput(Socket client);
    void removeClient(Socket client);
    int getClientCount();
}
