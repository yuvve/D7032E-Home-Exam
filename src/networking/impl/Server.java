package networking.impl;

import common.ScannerSingletons;

import java.util.Scanner;

/**
 * A listen server (it can host a game and participate in it).
 */
public class Server extends DedicatedServer {
    private boolean acceptedListenClient;

    public Server() {
        super();
        acceptedListenClient = false;
    }

    @Override
    public int acceptClient() {
        if (!acceptedListenClient) {
            acceptedListenClient = true;
            return 0;
        }
        int clientId = clients.size()+1;
        return acceptClient(clientId);
    }

    @Override
    public String getClientInput(int clientId) {
        Scanner scanner = ScannerSingletons.getInstance(System.in);
        if (clientId == 0) {
            common.Util.flushSystemIn();
            return scanner.nextLine();
        }
        return super.getClientInput(clientId);
    }

    @Override
    public void removeClient(int clientId) {
        if (clientId == 0) {
            return;
        }
        super.removeClient(clientId);
    }

    @Override
    public int getClientCount() {
        return clients.size()+1;
    }

    @Override
    public void sendMsg(int clientId, String msg) {
        if (clientId == 0) {
            System.out.println(msg);
            return;
        }
        super.sendMsg(clientId, msg);
    }
}
