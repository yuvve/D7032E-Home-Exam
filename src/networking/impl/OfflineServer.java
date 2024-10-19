package networking.impl;

import networking.IServer;

/**
 * Offline server implementation for local play against bots only
 */
public class OfflineServer implements IServer {

    @Override
    public void startServer(int port) {
        System.out.println("Offline server started");
    }

    @Override
    public int acceptClient() {
        return 0;
    }

    @Override
    public void broadcast(String msg) {
        System.out.println(msg);
    }

    @Override
    public void sendMsg(int clientId, String msg) {
        System.out.println(msg);
    }

    @Override
    public String getClientInput(int clientId) {
        return System.in.toString();
    }

    @Override
    public void removeClient(int clientId) {
        System.out.println("Offline server cannot remove clients");
    }

    @Override
    public int getClientCount() {
        return 0;
    }
}
