package server.model;

import java.util.ArrayList;
import java.util.List;

public class ServerHandlerClients {
    private List<ServerClient> listaClientes;

    public ServerHandlerClients() {
        listaClientes = new ArrayList<>();
    }

    public void agregarCliente(ServerClient c) {
        listaClientes.add(c);
    }

    public void enviarCodigoATodos(int codigo) {
        for (ServerClient cliente : listaClientes) {
            cliente.enviarMensaje(codigo);
        }
    }

    public void conectarClientes() {
        for (ServerClient cliente : listaClientes) {
            cliente.conectar();
        }
    }

    public void eliminarClientePorIP(String ip) {
        listaClientes.removeIf(c -> {
            if (c.getDireccionIP().equals(ip)) {
                c.enviarMensaje(-1);
                c.cerrarConexion();
                return true;
            }
            return false;
        });
    }

    public void cerrarTodasLasConexiones() {
        for (ServerClient cliente : listaClientes) {
            cliente.cerrarConexion();
        }

    }
}
