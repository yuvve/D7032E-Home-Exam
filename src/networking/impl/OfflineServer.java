package networking.impl;

import networking.IServer;

import java.util.Scanner;

/**
 * Offline server implementation for local play against bots only
 */
public class OfflineServer implements IServer {
    private Scanner scanner;

    public OfflineServer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void startServer(int port) {
        System.out.println("Offline server started");
    }

    @Override
    public void stopServer() {
        System.out.println("Offline server stopped");
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
        if (clientId != 0) {
            return;
        }
        System.out.println(msg);
    }

    @Override
    public String getClientInput(int clientId) {
        if (clientId != 0) {
            return null;
        }
        return scanner.nextLine();
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
