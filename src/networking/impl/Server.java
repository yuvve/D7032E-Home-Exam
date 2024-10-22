package networking.impl;

import common.ScannerSingletons;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Server extends DedicatedServer {
    private boolean returnedSelf = false;

    public Server() {
        super();
    }

    @Override
    public int acceptClient() {
        if (!returnedSelf) {
            returnedSelf = true;
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
