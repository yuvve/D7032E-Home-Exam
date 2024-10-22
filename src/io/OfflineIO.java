package io;

import common.ScannerSingletons;
import common.Util;

import java.util.Scanner;

public class OfflineIO implements IIOManager {
    @Override
    public void registerPlayer(int playerId) {

    }

    @Override
    public String getPlayerInput(int playerId) {
        Scanner scanner = ScannerSingletons.getInstance(System.in);
        Util.flushSystemIn();
        return scanner.nextLine();
    }

    @Override
    public void sendMsg(int playerId, String msg) {
        if (playerId == 0) {
            System.out.println(msg);
        }
    }

    @Override
    public void broadcast(String msg) {
        System.out.println(msg);
    }

    @Override
    public void endGame() {
        System.out.println("Game Over!");
    }
}
