package networking.impl;

import networking.ControlProtocol;
import networking.IClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client implements IClient {
    private boolean isConnected = false;
    public DataInputStream inFromClient;
    public DataOutputStream outToClient;

    @Override
    public void connectToServer(String ip, int port) {
        if (isConnected) {
            System.out.println("Already connected to server");
            return;
        }
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            socket = new Socket(ip, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            inFromClient = in;
            outToClient = out;
            isConnected = true;
            System.out.println("Connected to server");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (!isConnected) {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public void sendMessage(String msg) {
        if (!isConnected) {
            System.out.println("Not connected to server");
            return;
        }
        try {
            outToClient.writeUTF(msg);
            outToClient.writeUTF(ControlProtocol.TRANSMISSION_OVER.getValue());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String receiveMessage() {
        StringBuilder msg = new StringBuilder();
        if (!isConnected) {
            System.out.println("Not connected to server");
            return null;
        }
        while (true) {
            try {
                String incoming = inFromClient.readUTF();
                if (incoming.equals(ControlProtocol.TRANSMISSION_OVER.getValue())) {
                    break;
                }
                msg.append(incoming).append("\n");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return msg.toString();
    }
}
