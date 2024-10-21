package game.impl.turns;

import assets.IGameBoard;
import assets.IPile;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

import java.util.ArrayList;
import java.util.Map;

public class PointSaladHumanMain implements ITurnActionStrategy {
    private IGameBoard gameBoard;
    private IServer server;
    private Map<Integer, Integer> playerClientMap;

    public PointSaladHumanMain(IGameBoard gameBoard, IServer server, Map<Integer, Integer> playerClientMap) {
        this.gameBoard = gameBoard;
        this.server = server;
        this.playerClientMap = playerClientMap;
    }

    @Override
    public void executeTurnAction(IPlayer player) {
        int clientId = playerClientMap.get(player.getId());
        StringBuilder message = new StringBuilder();
        message.append("It's your turn (player ").append(player.getId()).append(")").append("!\n");
        message.append("Your current hand: \n");
        message.append(player.represent()).append("\n");
        message.append("You can either: \n");
        message.append("- Pick one card from a pile by typing 'P<X>' (i.e. P2) \n");
        message.append("- Draft a card from from the [M]arket by typing 'M<ROW><COL>' (i.e. M11) \n");
        message.append("- Draft two cards from the [M]arket by typing 'M<ROW1><COL1><ROW2><COL2>' (i.e. M1101)\n");
        server.sendMsg(clientId, message.toString());

        String input;
        char action;
        int pileIndex;
        int[][] marketIndices;

        do {
            input = server.getClientInput(clientId);
            if (!validateInput(input)) {
                server.sendMsg(clientId, "Invalid input. Please try again.");
            } else {
                if (!validateAction(input)) {
                    server.sendMsg(clientId, "Invalid action. Please try again.");
                } else {
                    action = input.charAt(0);
                    pileIndex = Integer.parseInt(input.substring(1,2));
                    marketIndices = parseCoords(input.substring(1));
                    break;
                }
            }
        } while (true);

        if (action == 'P') {
            player.addToHand(gameBoard.getCardFromPile(pileIndex));
        } else {
            player.addToHand(gameBoard.getMarket().draftCard(marketIndices[0][0], marketIndices[0][1]));
            if (marketIndices[1][0] != -1 && marketIndices[1][1] != -1) {
                player.addToHand(gameBoard.getMarket().draftCard(marketIndices[1][0], marketIndices[1][1]));
            }
        }
    }

    private int[][] parseCoords(String coords) {
        int row1, row2, col1, col2;

        if (coords.length() == 4) {
            row1 = Integer.parseInt(coords.substring(0, 1));
            col1 = Integer.parseInt(coords.substring(1, 2));
            row2 = Integer.parseInt(coords.substring(2,3));
            col2 = Integer.parseInt(coords.substring(3,4));
        } else if (coords.length() == 2) {
            row1 = Integer.parseInt(coords.substring(0, 1));
            col1 = Integer.parseInt(coords.substring(1,2));
            row2 = -1;
            col2 = -1;
        } else {
            return null;
        }
        return new int[][]{{row1, col1}, {row2, col2}};
    }

    private boolean validateAction(String input) {
        char action = input.charAt(0);
        int pileIndex;

        try { // Check that it the pile index is / coordinates are a valid integer
            pileIndex = Integer.parseInt(input.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }

        if (action == 'P'){
            try {
                ArrayList<Integer> nonEmptyPiles = gameBoard.getNonEmptyPiles();
                if (!nonEmptyPiles.contains(pileIndex)) return false;
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        }
        if (action == 'M'){
            int[][] parsedCoords = parseCoords(input.substring(1));
            if (parsedCoords == null) return false; // Check that the coordinates are of right length
            // Check that a player doesn't try to draft from the same position twice in one turn
            if (parsedCoords[0][0] == parsedCoords[1][0] && parsedCoords[0][1] == parsedCoords[1][1]) return false;
            try {
                if (gameBoard.getMarket().viewCard(parsedCoords[0][0], parsedCoords[0][1]) == null) return false;
                if (parsedCoords[1][0] != -1 && parsedCoords[1][1] != -1) {
                    if (gameBoard.getMarket().viewCard(parsedCoords[1][0], parsedCoords[1][1]) == null) return false;
                }
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateInput(String input) {
        input = input.toUpperCase();

        if (input.length() < 2) return false;
        if (input.charAt(0) != 'P' && input.charAt(0) != 'M') return false;
        try {
            Integer.parseInt(input.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
