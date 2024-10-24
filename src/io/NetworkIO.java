package io;

import common.ScannerSingletons;
import networking.ControlProtocol;
import networking.IServer;

import java.util.Map;
import java.util.Scanner;

public class NetworkIO implements IIOManager {
    private boolean dedicated;
    private IServer server;
    private Map<Integer, Integer> playerClientMap;

    public NetworkIO(boolean dedicated, IServer server, int port, Map<Integer, Integer> playerClientMap) {
        this.dedicated = dedicated;
        this.server = server;
        this.playerClientMap = playerClientMap;

        server.startServer(port);
    }

    @Override
    public void registerPlayer(int playerId) {
        if (playerClientMap.containsKey(playerId)){
            throw new IllegalArgumentException("Player ID already exists in playerClientMap");
        }
        if (!dedicated && playerClientMap.isEmpty()){
            playerClientMap.put(playerId, 0);
        } else {
            int clientId = server.acceptClient();
            playerClientMap.put(playerId, clientId);
        }
    }

    @Override
    public String getPlayerInput(int playerId) {
       if (!dedicated && playerId == 0){
           Scanner scanner = ScannerSingletons.getInstance(System.in);
           common.Util.flushSystemIn();
           return scanner.nextLine();
       }
        int clientId = playerClientMap.get(playerId);
        return server.getClientInput(clientId);
    }

    @Override
    public void sendMsg(int playerId, String msg) {
        if (!dedicated && playerId == 0){
            System.out.println(msg);
            return;
        }
        int clientId = playerClientMap.get(playerId);
        server.sendMsg(clientId, msg);
    }

    @Override
    public void broadcast(String msg) {
        if (dedicated){
            server.broadcast(msg);
        } else {
            for (int playerId : playerClientMap.keySet()){
                if (playerId == 0) {
                    System.out.println(msg);
                    continue;
                }
                server.sendMsg(playerClientMap.get(playerId), msg);
            }
        }
    }

    @Override
    public void endGame() {
        broadcast(ControlProtocol.GAME_OVER.getValue());
        server.stopServer();
    }

}
