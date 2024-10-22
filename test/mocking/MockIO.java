package mocking;

import io.IIOManager;

import java.util.ArrayList;

public class MockIO implements IIOManager {
    private ArrayList<String> actions;
    private ArrayList<String> messages;

    public MockIO(ArrayList<String> actions) {
        this.actions = actions;
        this.messages = new ArrayList<>();
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    @Override
    public void registerPlayer(int playerId) {

    }

    @Override
    public String getPlayerInput(int Playerid) {
        return actions.removeFirst();
    }

    @Override
    public void sendMsg(int Playerid, String msg) {
        messages.add(msg);
    }

    @Override
    public void broadcast(String msg) {

    }

    @Override
    public void endGame() {

    }
}
