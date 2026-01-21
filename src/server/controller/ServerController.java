package server.controller;

import server.model.*;
import java.io.File;
import java.util.*;

public class ServerController {
    private ServerHandlerClients manejadorClientes;
    private Map<String, ServerClient> clientes;

    public ServerController() {
        this.manejadorClientes = new ServerHandlerClients();
        this.clientes = new HashMap<>();
    }

    public boolean agregarIp(String ip, int puerto) {
        if (clientes.containsKey(ip)) {
            return false;
        }

        ServerClient cliente = new ServerClient(ip, puerto);
        clientes.put(ip, cliente);
        manejadorClientes.agregarCliente(cliente);
        return true;
    }

    public boolean eliminarIp(String ip) {
        if (clientes.containsKey(ip)) {
            manejadorClientes.eliminarClientePorIP(ip);
            clientes.remove(ip);
            return true;
        }
        return false;
    }

    public void limpiarIps() {
        manejadorClientes.cerrarTodasLasConexiones();
        clientes.clear();
    }

    public void mostrarIps() {
        if (clientes.isEmpty()) {
            System.out.println("No hay IPs registradas.");
        } else {
            System.out.println("IPs registradas:");
            for (String ip : clientes.keySet()) {
                System.out.println("- " + ip + ":" + clientes.get(ip).getPuerto());
            }
        }
    }

    public void enviarMensajeCodificado() {
        manejadorClientes.conectarClientes();

        new Thread(() -> {
            try {
                ServerEncode encoder = new ServerEncode(new File("Obladi.txt"));

                encoder.setCallback((codigo) -> {
                    manejadorClientes.enviarCodigoATodos(codigo);
                    if (codigo == -1) {
                        manejadorClientes.cerrarTodasLasConexiones();
                        System.out.println("Fin del envío. Se cerraron las conexiones.");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                });

                encoder.codificar();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}