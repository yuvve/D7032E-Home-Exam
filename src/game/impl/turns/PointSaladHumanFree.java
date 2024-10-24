package game.impl.turns;

import assets.ICard;
import assets.IGameBoard;
import io.IIOManager;
import game.ITurnActionStrategy;
import player.IPlayer;

import java.util.ArrayList;

/**
 * The human action strategy representing a Point Salad free action.
 */
public class PointSaladHumanFree implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IIOManager io;

    public PointSaladHumanFree(IGameBoard gameBoard, IIOManager io) {
        this.gameBoard = gameBoard;
        this.io = io;
    }

    @Override
    public void executeTurnAction(IPlayer player) {
        ArrayList<ICard> criteriaCards = player.getCriteriaCards();

        if (criteriaCards.isEmpty()){
            io.sendMsg(player.getId(),
                    "You have no flipable cards, and thus cannot perform a free action.");
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("You have the following cards that can be flipped: \n");
        for (int i = 0; i < criteriaCards.size(); i++){
            msg.append(i).append(": ").append(criteriaCards.get(i).represent()).append("\n");
        }
        msg.append("To flip a card, type '<CARD_NUMBER>' (i.e. 1), or leave blank (press enter) to skip your free action.");
        io.sendMsg(player.getId(), msg.toString());

        String input;
        do {
            input = io.getPlayerInput(player.getId());
            try {
                int cardIndex = Integer.parseInt(input);
                if (cardIndex < 0 || cardIndex >= criteriaCards.size()){
                    io.sendMsg(player.getId(), "Invalid card number. Please try again.");
                } else {
                    criteriaCards.get(cardIndex).flip();
                    break;
                }
            } catch (NumberFormatException e){
                if (input.equals("")){
                    break;
                } else {
                    io.sendMsg(player.getId(), "Invalid input. Please try again.");
                }
            }
        } while (true);
    }
}
