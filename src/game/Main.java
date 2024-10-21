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
import java.util.*;

public class Main {
    private static int DEFAULT_PORT = 2048;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String ipPort = "";
        String prompt =
                "To join a server: `[IP]:[PORT]`\n" +
                "To host a server: `[PORT]` or just leave blank to use default port (2048).";
        if (args.length == 0){
            System.out.println(prompt);
            ipPort = Util.getIpPort(scanner);
        }
        else if (args.length != 1) {
            System.out.println("Invalid number of arguments.");
            System.out.println(prompt);
            ipPort = Util.getIpPort(scanner);
        } else {
            if (!Util.validateIpPort(args[0])) {
                System.out.println("Invalid IP and port.");
                System.out.println(prompt);
                ipPort = Util.getIpPort(scanner);
            } else {
                ipPort = args[0];
            }
        }

        if (Objects.equals(ipPort, "")) {
            int port = DEFAULT_PORT;
            hostPointSaladGame(scanner, port);
        } else if (ipPort.split(":").length == 1) {
            int port = Integer.parseInt(ipPort);
            hostPointSaladGame(scanner, port);
        }
        else {
            String ip = ipPort.split(":")[0];
            int port = Integer.parseInt(ipPort.split(":")[1]);
            joinGame(scanner, ip, port);
        }
    }

    private static void joinGame(Scanner scanner, String ip, int port){
        IClient client = new Client();
        client.connectToServer(ip, port);

        // loop and listen to server until you need to send a message, then continue looping again
    }

    /**
     * Hosts a game of Point Salad.
     * Calls the hostGame method with dependency injection for Point Salad.
     * @param scanner the scanner for user input
     * @param port the port to host the server on
     */
    private static void hostPointSaladGame(Scanner scanner, int port){
        IGameLoopFactory gameLoopFactory = new PointSaladGameLoopFactory();
        IAbstractAssetsFactory assetsFactory = new PointSaladAssetsFactory();
        IAbstractPlayerAssetsFactory playerFactory = new PointSaladPlayerAssetsFactory();
        PointSaladTurnActionStrategyFactory turnActionStrategyFactory = new PointSaladTurnActionStrategyFactory();

        hostGame(
                scanner,
                port,
                gameLoopFactory,
                assetsFactory,
                playerFactory,
                turnActionStrategyFactory,
                "PointSaladManifest.json"
        );
    }

    private static void hostGame(
            Scanner scanner,
            int port,
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
        int humanPlayers = Util.getValidInput(scanner, minHumanPlayers, maxHumanPlayers);

        int botPlayers;
        if (humanPlayers == Constants.MAX_PLAYERS.getValue()){
            botPlayers = 0;
        } else {
            int minBots = Math.max(Constants.MIN_PLAYERS.getValue() - humanPlayers, 0);
            int maxBots = Math.min(Constants.MAX_PLAYERS.getValue() - humanPlayers, Constants.MAX_PLAYERS.getValue()-1);
            System.out.println("How many AI players are there (" + minBots + "-" + maxBots + ")?");
            botPlayers = Util.getValidInput(scanner, minBots, maxBots);
        }

        IPlayerManager playerManager = playerFactory.createPlayerManager(humanPlayers, botPlayers);
        IGameBoard gameBoard = assetsFactory.createGameBoard(deckJson, humanPlayers + botPlayers);
        IServer server;
        if (humanPlayers > 1) {
            server = new Server();
            server.startServer(port);
        } else {
            server = new OfflineServer(scanner);
        }

        Map<Integer, Integer> playerClientMap = new HashMap<>();
        ArrayList<ITurnActionStrategy> humanStrategies =
                turnActionStrategyFactory.createHumanStrategies(gameBoard, server, playerClientMap);
        ArrayList<ITurnActionStrategy> botStrategies =
                turnActionStrategyFactory.createBotStrategies(gameBoard, playerManager);

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
