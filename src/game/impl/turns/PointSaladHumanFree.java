package game.impl.turns;

import assets.ICard;
import assets.IGameBoard;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

import java.util.ArrayList;
import java.util.Map;

/**
 * The human action strategy representing a Point Salad free action.
 */
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

        ArrayList<ICard> criteriaCards = player.getCriteriaCards();

        if (criteriaCards.isEmpty()){
            server.sendMsg(clientId,
                    "You have no flipable cards, and thus cannot perform a free action.");
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("You have the following cards that can be flipped: \n");
        for (int i = 0; i < criteriaCards.size(); i++){
            msg.append(i).append(": ").append(criteriaCards.get(i).represent()).append("\n");
        }
        msg.append("To flip a card, type '<CARD_NUMBER>' (i.e. 1), or leave blank (press enter) to skip your free action.");
        server.sendMsg(clientId, msg.toString());

        String input;
        do {
            input = server.getClientInput(clientId);
            try {
                int cardIndex = Integer.parseInt(input);
                if (cardIndex < 0 || cardIndex >= criteriaCards.size()){
                    server.sendMsg(clientId, "Invalid card number. Please try again.");
                } else {
                    criteriaCards.get(cardIndex).flip();
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
