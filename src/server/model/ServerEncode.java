package server.model;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class ServerEncode {
    private Map<String, Integer> diccionario;
    private List<Integer> codigos;
    private File mensaje;
    private Consumer<Integer> callback;
    private int count_key;

    public ServerEncode(File mensaje) throws IOException {
        this.count_key = 0;
        this.mensaje = mensaje;
        this.diccionario = new HashMap<>();
        this.codigos = new ArrayList<>();
        cargarDiccionario();
    }

    public void setCallback(Consumer<Integer> callback) {
        this.callback = callback;
    }

    private void cargarDiccionario() throws IOException {
        for (int i = 0; i < 256; i++) {
            diccionario.put(String.valueOf((char) i), i);
        }
        count_key = 256;
    }

    public void codificar() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(mensaje));
        StringBuilder contenido = new StringBuilder();
        String linea;

        while ((linea = br.readLine()) != null) {
            contenido.append(linea + "\n");
        }
        br.close();

        String input = contenido.toString();
        if (input.isEmpty())
            return;

        String PE = "" + input.charAt(0);
        System.out.println("Primera letra:" + PE);

        for (int i = 1; i < input.length(); i++) {
            System.out.println("\n\nLetra numero: " + i);
            String SE = "" + input.charAt(i);
            System.out.println("Segunda letra:" + SE);

            String PS = PE + SE;
            System.out.println("COMBINACION:" + PS);

            if (diccionario.containsKey(PS)) {
                PE = PS;
            } else {
                codigos.add(diccionario.get(PE));
                if (callback != null)
                    callback.accept(diccionario.get(PE));
                System.out.println("SE mando el codigo de (" + PE + "):" + diccionario.get(PE));

                diccionario.put(PS, count_key++);
                System.out.println("Llave:" + PS + ", Valor:" + diccionario.get(PS));
                PE = SE;
            }
        }

        codigos.add(diccionario.get(PE));
        if (callback != null)
            callback.accept(diccionario.get(PE));

        if (callback != null)
            callback.accept(-1);
    }

}
