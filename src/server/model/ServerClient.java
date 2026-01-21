package server.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClient {
    private String direccionIP;
    private int puerto;
    private Socket socket;
    private DataOutputStream salida;

    public ServerClient(String direccionIP, int puerto) {
        this.direccionIP = direccionIP;
        this.puerto = puerto;
    }

    public boolean conectar() {
        try {
            socket = new Socket(direccionIP, puerto);
            salida = new DataOutputStream(socket.getOutputStream());
            System.out.println("se conecto al cliente" + direccionIP);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void enviarMensaje(int codigo) {
        try {
            if (salida != null && socket != null && socket.isConnected() && !socket.isClosed()) {
                salida.writeInt(codigo);
                salida.flush();

            } else {
                System.out.println("No se puede enviar: conexión no válida o cerrada.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public int getPuerto() {
        return puerto;
    }
}