package client.controller;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import client.model.ClientDecoder;
import client.model.ServerConnection;

public class Controller {
    private ClientDecoder clientDecoder;
    private ServerConnection serverConnection;
    private int listenPort;

    public Controller(int listenPort) {
        this.listenPort = listenPort;
        clientDecoder = new ClientDecoder();
        serverConnection = new ServerConnection(listenPort, this);

        System.out.println("IP local: " + getLocalIp());
        System.out.println("Escuchando en el puerto: " + listenPort);
    }

    private String getLocalIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual())
                    continue;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }

            return "No se encontró una IP local válida.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error obteniendo IP";
        }
    }

    public void notifyError(String error) {
        System.err.println("ERROR: " + error);
    }

    public void receiveMessage(String message, boolean esAzul) {
        try {
            Thread.sleep(1000); // Esperar 1 segundo por letra
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        if (esAzul) {
            System.out.print("\u001B[34m" + message + "\u001B[0m");  // Azul
        } else {
            System.out.print("\u001B[31m" + message + "\u001B[0m");  // Rojo
        }
    }
}