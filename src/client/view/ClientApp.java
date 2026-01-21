package client.view;

import client.controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class ClientApp extends JFrame {



   public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int clientPort;

        while (true) {
            System.out.print("Ingrese el puerto para escuchar conexiones: ");
            String input = scanner.nextLine();

            try {
                int port = Integer.parseInt(input);

                if (port < 1024 || port > 65535) {
                    System.out.println("El puerto debe estar entre 1024 y 65535.");
                    continue;
                }

                clientPort = port;
                break;

            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        new Controller(clientPort);
    }
}
