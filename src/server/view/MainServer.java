package server.view;

import java.util.Scanner;
import server.controller.ServerController;


public class MainServer {
    public static void main(String[] args) {
        ServerController ctrl = new ServerController();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENU SERVIDOR =====");
            System.out.println("1. Agregar IP");
            System.out.println("2. Ver lista de IPs");
            System.out.println("3. Enviar mensaje codificado (archivo)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Ingrese la IP del cliente: ");
                    String ip = sc.nextLine().trim();
                    System.out.print("Ingrese el puerto del cliente: ");
                    int puerto = Integer.parseInt(sc.nextLine().trim());

                    if (ctrl.agregarIp(ip, puerto)) {
                        System.out.println("Cliente agregado: " + ip + ":" + puerto);
                    } else {
                        System.out.println("Ya hay un cliente registrado con esa IP.");
                    }
                    break;

                case "2":
                    ctrl.mostrarIps();
                    break;

                case "3":
                    ctrl.enviarMensajeCodificado();
                    break;

                case "4":
                    salir = true;
                    System.out.println("Servidor finalizado.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }

        sc.close();
    }
}
