package game.impl.turns;

import assets.IGameBoard;
import assets.IPile;
import game.ITurnActionStrategy;
import networking.IServer;
import player.IPlayer;

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
        message.append("You can either: \n");
        message.append("- Pick one card from a pile by typing 'P<X>' (i.e. P2) \n");
        message.append("- Draft a card at position (x,y) from the [M]arket by typing 'M<X><Y>' (i.e. M11) \n");
        message.append("- Draft two cards from the [M]arket by typing 'M<X1><Y1><X2><Y2>' (i.e. M1101)\n");
        server.sendMsg(clientId, message.toString());

        String input;
        char action;
        int pileIndex;
        int[][] coords;

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
                    coords = parseCoords(input.substring(1));
                    break;
                }
            }
        } while (true);

        if (action == 'P') {
            IPile pile = gameBoard.getPile(pileIndex);
            player.addToHand(pile.drawTop());
        } else {
            player.addToHand(gameBoard.getMarket().draftCard(coords[0][0], coords[0][1]));
            if (coords[1][0] != -1 && coords[1][1] != -1) {
                player.addToHand(gameBoard.getMarket().draftCard(coords[1][0], coords[1][1]));
            }
        }
    }

    private int[][] parseCoords(String coords) {
        int x1, x2, y1, y2;

        if (coords.length() == 4) {
            x1 = Integer.parseInt(coords.substring(0, 1));
            y1 = Integer.parseInt(coords.substring(1, 2));
            x2 = Integer.parseInt(coords.substring(2,3));
            y2 = Integer.parseInt(coords.substring(3,4));
        } else if (coords.length() == 2) {
            x1 = Integer.parseInt(coords.substring(0, 1));
            y1 = Integer.parseInt(coords.substring(1,2));
            x2 = -1;
            y2 = -1;
        } else {
            return null;
        }
        return new int[][]{{x1, y1}, {x2, y2}};
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
                IPile pile = gameBoard.getPile(pileIndex);
                if (pile.getCardCount() == 0) return false;
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
