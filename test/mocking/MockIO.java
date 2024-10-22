package mocking;

import io.IIOManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MockIO implements IIOManager {
    private Map<Integer, ArrayList<String>> playerToActions;
    private Map<Integer, ArrayList<String>> playerToMessagesReceived;

    public MockIO(
            Map<Integer, ArrayList<String>> playerToActions) {
        this.playerToActions = playerToActions;
        this.playerToMessagesReceived = new HashMap<>();
    }

    public ArrayList<String> getMessages(int playerId) {
        return playerToMessagesReceived.get(playerId);
    }

    @Override
    public void registerPlayer(int playerId) {
        playerToMessagesReceived.put(playerId, new ArrayList<>());
    }

    @Override
    public String getPlayerInput(int playerId) {
        return playerToActions.get(playerId).removeFirst();
    }

    @Override
    public void sendMsg(int playerId, String msg) {
        playerToMessagesReceived.get(playerId).add(msg);
    }

    @Override
    public void broadcast(String msg) {

    }

    @Override
    public void endGame() {

    }
}
