package game.impl.turns;

import assets.ICard;
import assets.IGameBoard;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

import java.util.ArrayList;
import java.util.Map;

public class PointSaladHumanFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IServer server;
    private Map<Integer, Integer> playerClientMap;

    public PointSaladHumanFree(IGameBoard gameBoard, IServer server, Map<Integer, Integer> playerClientMap) {
        this.gameBoard = gameBoard;
        this.server = server;
        this.playerClientMap = playerClientMap;
    }

    @Override
    public void executeTurnAction(IPlayer player) {
        int clientId = playerClientMap.get(player.getId());

        ArrayList<ICard> flipAbleCards = new ArrayList<>();
        for (ICard card: player.getHand()){
            if (card.canFlip()){
                flipAbleCards.add(card);
            }
        }

        if (flipAbleCards.isEmpty()){
            server.sendMsg(clientId,
                    "You have no flipable cards, and thus cannot perform a free action.");
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("You have the following cards that can be flipped: \n");
        for (int i = 0; i < flipAbleCards.size(); i++){
            msg.append(i).append(": ").append(flipAbleCards.get(i).represent()).append("\n");
        }
        msg.append("To flip a card, type '<CARD_NUMBER>' (i.e. 1), or leave blank (press enter) to skip your free action.");

        String input;
        do {
            input = server.getClientInput(clientId);
            try {
                int cardIndex = Integer.parseInt(input);
                if (cardIndex < 1 || cardIndex >= flipAbleCards.size()){
                    server.sendMsg(clientId, "Invalid card number. Please try again.");
                } else {
                    flipAbleCards.get(cardIndex).flip();
                    break;
                }
            } catch (NumberFormatException e){
                if (input.equals("")){
                    break;
                } else {
                    server.sendMsg(clientId, "Invalid input. Please try again.");
                }
            }
        } while (true);
    }
}
