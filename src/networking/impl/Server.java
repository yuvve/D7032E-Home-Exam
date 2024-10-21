package networking.impl;

import java.io.IOException;
import java.util.Scanner;

public class Server extends DedicatedServer {
    private Scanner scanner;
    private boolean returnedSelf = false;

    public Server(Scanner scanner) {
        super();
        this.scanner = scanner;
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
        if (clientId == 0) {
            // Flush scanner from all garbage players type when it's not their turn
            while (scanner.hasNextLine()) {
                scanner.nextLine();
            }
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
