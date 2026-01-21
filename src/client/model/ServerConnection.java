package client.model;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.controller.Controller;

public class ServerConnection implements Runnable {
    private ClientDecoder decoder;
    private final int LISTEN_PORT;
    private Thread listenerThread;

    private Controller controller;

    public ServerConnection(int clientPort, Controller controller) {
        decoder = new ClientDecoder();
        this.controller = controller;
        this.LISTEN_PORT = clientPort;

        listenerThread = new Thread(this);
        listenerThread.start();
    }

    @Override
    public void run() {
        String firstValue;
        String secondValue;
        boolean esAzul = false;

        try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                        DataInputStream payload = new DataInputStream(socket.getInputStream())) {

                    System.out.println("conectado, esperando");
                    int first = payload.readInt();
                    System.out.println("Codigo:" + first);

                    firstValue = decoder.getValueCode(first);
                    System.out.println("Primer valor: " + firstValue);

                    while (true) {
                        if (esAzul) {
                            esAzul = false;
                        } else {
                            esAzul = true;
                        }
                        int second = payload.readInt();

                        if (second == -1) {
                            controller.receiveMessage(firstValue + "\n", esAzul);
                            break;
                        }

                        if (decoder.getValueCode(second) == null) {
                            secondValue = firstValue + firstValue.charAt(0);
                        } else {
                            secondValue = decoder.getValueCode(second);
                        }

                        String completeMessage = firstValue + secondValue.charAt(0);

                        if (!decoder.valueExists(completeMessage)) {
                            decoder.addEntry(completeMessage);
                        }

                        controller.receiveMessage(firstValue, esAzul);
                        firstValue = secondValue;
                    }

                }
            }
        } catch (Exception e) {
            controller.notifyError("Error: " + e.getMessage());
        }
    }

}
