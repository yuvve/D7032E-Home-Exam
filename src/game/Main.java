package game;

import assets.IAbstractAssetsFactory;
import assets.IGameBoard;
import assets.impl.PointSaladAssetsFactory;
import common.point_salad.Constants;
import game.impl.PointSaladGameLoop;
import game.impl.PointSaladGameLoopFactory;
import game.impl.PointSaladTurnActionStrategyFactory;
import networking.IClient;
import networking.IServer;
import networking.impl.Client;
import networking.impl.OfflineServer;
import networking.impl.Server;
import org.json.JSONObject;
import player.IAbstractPlayerAssetsFactory;
import player.IPlayerManager;
import player.impl.PointSaladPlayerAssetsFactory;


import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String ipPort = "";
        String prompt = "Enter an IP address and port to join a server [IP]:[PORT], or leave blank to host a server.";
        if (args.length == 0){
            System.out.println(prompt);
            ipPort = Util.getIpPort();
        }
        else if (args.length != 1) {
            System.out.println("Invalid number of arguments.");
            System.out.println(prompt);
            ipPort = Util.getIpPort();
        } else {
            if (!Util.validateIpPort(args[0])) {
                System.out.println("Invalid IP and port.");
                System.out.println(prompt);
                ipPort = Util.getIpPort();
            } else {
                ipPort = args[0];
            }
        }

        if (Objects.equals(ipPort, "")) {
            hostPointSaladGame(ipPort);
        } else {
            joinGame(ipPort);
        }
    }

    private static void joinGame(String ipPort){
        String ip = ipPort.split(":")[0];
        int port = Integer.parseInt(ipPort.split(":")[1]);
        IClient client = new Client();
        client.connectToServer(ip, port);

        // loop and listen to server until you need to send a message, then continue looping again
    }

    /**
     * Hosts a game of Point Salad.
     * Calls the hostGame method with dependency injection for Point Salad.
     * @param ipPort The IP address and port to host the game on.
     */
    private static void hostPointSaladGame(String ipPort){
        IGameLoopFactory gameLoopFactory = new PointSaladGameLoopFactory();
        IAbstractAssetsFactory assetsFactory = new PointSaladAssetsFactory();
        IAbstractPlayerAssetsFactory playerFactory = new PointSaladPlayerAssetsFactory();
        PointSaladTurnActionStrategyFactory turnActionStrategyFactory = new PointSaladTurnActionStrategyFactory();

        hostGame(
                ipPort,
                gameLoopFactory,
                assetsFactory,
                playerFactory,
                turnActionStrategyFactory,
                "PointSaladManifest.json"
        );
    }

    private static void hostGame(
            String ipPort,
            IGameLoopFactory gameLoopFactory,
            IAbstractAssetsFactory assetsFactory,
            IAbstractPlayerAssetsFactory playerFactory,
            ITurnActionStrategyFactory turnActionStrategyFactory,
            String deckManifestFilename){

        JSONObject deckJson;

        try {
            deckJson = Util.fileToJSON(deckManifestFilename);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find the deck manifest.");
            return;
        }
        int minHumanPlayers = Constants.MIN_PLAYERS.getValue()-1;
        int maxHumanPlayers = Constants.MAX_PLAYERS.getValue();
        System.out.println("How many HUMAN players are there (" + minHumanPlayers + "-" + maxHumanPlayers + ")?");
        int humanPlayers = Util.getValidInput(minHumanPlayers, maxHumanPlayers);

        int botPlayers;
        if (humanPlayers == Constants.MAX_PLAYERS.getValue()){
            botPlayers = 0;
        } else {
            int minBots = Math.max(Constants.MIN_PLAYERS.getValue() - humanPlayers, 0);
            int maxBots = Math.min(Constants.MAX_PLAYERS.getValue() - humanPlayers, Constants.MAX_PLAYERS.getValue()-1);
            System.out.println("How many AI players are there (" + minBots + "-" + maxBots + ")?");
            botPlayers = Util.getValidInput(minBots, maxBots);
        }

        IPlayerManager playerManager = playerFactory.createPlayerManager(humanPlayers, botPlayers);
        IGameBoard gameBoard = assetsFactory.createGameBoard(deckJson, humanPlayers + botPlayers);
        IServer server;
        if (humanPlayers > 1) {
            server = new Server();
        } else {
            server = new OfflineServer();
        }

        Map<Integer, Integer> playerClientMap = new HashMap<>();
        ArrayList<ITurnActionStrategy> humanStrategies =
                turnActionStrategyFactory.createHumanStrategies(gameBoard, server, playerClientMap);
        ArrayList<ITurnActionStrategy> botStrategies =
                turnActionStrategyFactory.createBotStrategies(gameBoard);

        GameLoopTemplate gameLoop = gameLoopFactory.createGameLoop(
                server,
                playerManager,
                gameBoard,
                playerClientMap,
                humanStrategies,
                botStrategies
        );

        gameLoop.startGame();
    }
}
