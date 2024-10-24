package game;

import assets.IAbstractAssetsFactory;
import assets.IGameBoard;
import assets.impl.PointSaladAssetsFactory;
import common.ScannerSingletons;
import common.point_salad.Constants;
import io.IIOManager;
import io.NetworkIO;
import io.OfflineIO;
import game.impl.PointSaladGameLoopFactory;
import game.impl.PointSaladTurnActionStrategyFactory;
import networking.IClient;
import networking.IServer;
import networking.impl.Client;
import networking.impl.Server;
import org.json.JSONObject;
import player.IAbstractPlayerAssetsFactory;
import player.IPlayerManager;
import player.impl.PointSaladPlayerAssetsFactory;


import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static int DEFAULT_PORT = 2048;
    public static void main(String[] args) {
        String ipPort = "";
        String prompt =
                "To join a server: `[IP]:[PORT]`\n" +
                "To host a server: `[PORT]` or just leave blank to use default port (2048).";
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
            int port = DEFAULT_PORT;
            hostPointSaladGame(port);
        } else if (ipPort.split(":").length == 1) {
            int port = Integer.parseInt(ipPort);
            hostPointSaladGame(port);
        }
        else {
            String ip = ipPort.split(":")[0];
            int port = Integer.parseInt(ipPort.split(":")[1]);
            joinGame(ip, port);
        }
    }

    private static void joinGame(String ip, int port){
        IClient client = new Client();
        client.connectToServer(ip, port);
        Scanner scanner = ScannerSingletons.getInstance(System.in);
        while(client.isConnectionAlive()){
            if (client.serverWaitingForInput()){
                common.Util.flushSystemIn();
                String msg = scanner.nextLine();
                client.sendMessage(msg);
            }
            System.out.println(client.receiveMessage());
        }
    }

    /**
     * Hosts a game of Point Salad.
     * Calls the hostGame method with dependency injection for Point Salad.
     * @param port the port to host the server on
     */
    private static void hostPointSaladGame(int port){
        Random random = new Random();
        IGameLoopFactory gameLoopFactory = new PointSaladGameLoopFactory();
        IAbstractAssetsFactory assetsFactory = new PointSaladAssetsFactory(random);
        IAbstractPlayerAssetsFactory playerFactory = new PointSaladPlayerAssetsFactory(random);
        PointSaladTurnActionStrategyFactory turnActionStrategyFactory = new PointSaladTurnActionStrategyFactory();


        hostGame(
                port,
                gameLoopFactory,
                assetsFactory,
                playerFactory,
                turnActionStrategyFactory,
                random,
                "PointSaladManifest.json"
        );
    }

    private static void hostGame(
            int port,
            IGameLoopFactory gameLoopFactory,
            IAbstractAssetsFactory assetsFactory,
            IAbstractPlayerAssetsFactory playerFactory,
            ITurnActionStrategyFactory turnActionStrategyFactory,
            Random random,
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
        IIOManager io;
        if (humanPlayers > 1) {
            IServer server  = new Server();
            io = new NetworkIO(false, server, port, new HashMap<>());
        } else {
            io = new OfflineIO();
        }

        Map<Integer, Integer> playerClientMap = new HashMap<>();
        ArrayList<ITurnActionStrategy> humanStrategies =
                turnActionStrategyFactory.createHumanStrategies(gameBoard, io);
        ArrayList<ITurnActionStrategy> botStrategies =
                turnActionStrategyFactory.createBotStrategies(gameBoard, playerManager, random);

        GameLoopTemplate gameLoop = gameLoopFactory.createGameLoop(
                io,
                playerManager,
                gameBoard,
                humanStrategies,
                botStrategies
        );

        gameLoop.startGame();
    }
}
