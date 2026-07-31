# Algoritmo LWZ — Compresión LZW Cliente/Servidor

## 1. Descripción general

Proyecto académico en **Java** que implementa el **algoritmo de compresión LZW (Lempel-Ziv-Welch)** sobre una arquitectura cliente-servidor comunicada por sockets TCP.

El servidor toma un archivo de texto, lo comprime en tiempo real generando códigos numéricos, y los transmite a uno o varios clientes conectados. Cada cliente reconstruye (descomprime) el mensaje original usando la misma lógica de diccionario, y lo muestra en consola letra por letra, alternando colores para diferenciar visualmente cada paso de la decodificación.

> Nota: no es un ejercicio sobre el abecedario. Lo que sí usa es la **tabla ASCII (caracteres 0–255)** como diccionario inicial del algoritmo.

## 2. ¿Qué es LZW y qué hace este proyecto con él?

LZW es un algoritmo de compresión sin pérdida. La idea central:

1. Se arranca con un diccionario que contiene todos los caracteres individuales posibles (en este caso, los 256 caracteres ASCII).
2. Se recorre el texto buscando la secuencia más larga ya conocida en el diccionario.
3. Cuando aparece una combinación nueva (por ejemplo, "ob" después de tener "o" y "b" por separado), se agrega esa combinación como una nueva entrada del diccionario, y se envía el código de la secuencia anterior conocida.
4. El resultado es que, en vez de mandar el texto completo, se mandan **números (códigos)** que representan secuencias de texto cada vez más largas.

El cliente hace el proceso inverso: reconstruye el mismo diccionario a medida que recibe códigos, y así recupera el texto original sin necesidad de que el servidor le mande el diccionario completo.

## 3. Arquitectura del proyecto

```
src/
├── client/
│   ├── controller/
│   │   └── Controller.java        → orquesta la recepción y muestra el mensaje decodificado
│   ├── model/
│   │   ├── ClientDecoder.java     → mantiene el diccionario de decodificación (código → texto)
│   │   └── ServerConnection.java  → escucha conexiones entrantes por socket y reconstruye el mensaje
│   └── view/
│       └── ClientApp.java         → punto de entrada del cliente (pide puerto de escucha)
│
└── server/
    ├── controller/
    │   └── ServerController.java  → maneja el registro de IPs de clientes y dispara el envío
    ├── model/
    │   ├── ServerClient.java      → representa la conexión saliente hacia un cliente
    │   ├── ServerEncode.java      → implementa el algoritmo LZW de codificación
    │   └── ServerHandlerClients.java → administra la lista de clientes conectados
    └── view/
        └── MainServer.java        → punto de entrada del servidor (menú por consola)
```

Archivos de datos:
- `text/Obladi.txt`: texto de ejemplo que el servidor comprime y envía (letra de "Ob-La-Di, Ob-La-Da").
- `src/server/data/assci_table.txt`: listado de referencia de los 256 caracteres ASCII usados como diccionario base.

## 4. Flujo de ejecución

1. Se levanta el **servidor** (`MainServer`). Desde el menú se pueden registrar las IPs y puertos de los clientes que se van a conectar.
2. Se levanta cada **cliente** (`ClientApp`), que pide un puerto local donde va a escuchar la conexión del servidor.
3. Desde el menú del servidor se elige "Enviar mensaje codificado": esto conecta con todos los clientes registrados y comienza a codificar `Obladi.txt` con LZW, mandando un código por vez.
4. Cada cliente recibe los códigos, los traduce con su propio diccionario (que crece igual que el del servidor) y va imprimiendo el texto reconstruido en consola, alternando color rojo/azul, con 1 segundo de pausa entre letras para que se note el proceso.
5. Cuando el servidor termina de enviar el texto, manda un código de cierre (`-1`) y cierra las conexiones.

## 5. Tecnologías utilizadas

- **Java** (JDK 8+)
- **Sockets TCP** (`Socket`, `ServerSocket`) para la comunicación cliente-servidor
- **NetBeans / Ant** como entorno y sistema de build (hay archivos `build.xml` y `nbproject/`)
- **Git / GitHub** para control de versiones

## 6. Instalación y ejecución

### Requisitos
1. Java JDK 8 o superior.
2. Git.
3. IDE recomendado: NetBeans (el proyecto ya trae configuración de NetBeans en `nbproject/`).

### Pasos
```bash
git clone https://github.com/juandiegogalindo/DesarrolloAplicaciones-Algoritmo.git
```

1. Abrir el proyecto en NetBeans (o compilar manualmente con `javac` respetando los paquetes `client.*` y `server.*`).
2. Ejecutar primero `server.view.MainServer` y registrar la IP/puerto del cliente.
3. Ejecutar `client.view.ClientApp` en la otra máquina (o en otra terminal si es local) e ingresar el puerto de escucha.
4. Desde el menú del servidor, elegir la opción de enviar el mensaje codificado y ver cómo el cliente lo reconstruye en pantalla.

## 7. Conceptos nuevos que se usan en este código

Cosas del código que probablemente no hayas usado todavía en Java, JS o Spring Boot:

- **`try (...) { }` (try-with-resources):** es una variante del `try/catch` que abre un recurso (como un `Socket` o un `ServerSocket`) dentro del paréntesis, y Java se encarga automáticamente de cerrarlo al terminar el bloque, aunque ocurra una excepción. Evita tener que escribir un `finally` con `socket.close()` manualmente.
- **`Runnable` + `Thread`:** `Runnable` es una interfaz que define un método `run()`. Cuando una clase la implementa (como `ServerConnection`), se le puede pasar a un `Thread` para que ese código se ejecute en un **hilo separado**, en paralelo al resto del programa (en este caso, para escuchar conexiones sin bloquear el resto de la app).
- **`Consumer<Integer>` (interfaz funcional):** viene del paquete `java.util.function`. Representa "una función que recibe un valor y no devuelve nada". Se usa para pasar un **callback**: `ServerEncode` no sabe qué hacer con cada código generado, entonces recibe una función (`callback.accept(codigo)`) que se ejecuta cada vez que se genera un nuevo código, y quien la definió (`ServerController`) decide qué hacer con ese código (en este caso, enviarlo a los clientes).
- **`DataInputStream` / `DataOutputStream`:** son flujos de entrada/salida que permiten leer y escribir tipos de datos primitivos (como `int`) directamente por un socket, sin tener que convertir manualmente a texto o bytes.
- **Códigos de escape ANSI (`\u001B[34m...\u001B[0m`):** son secuencias especiales que, al imprimirse en una terminal compatible, cambian el color del texto (34 = azul, 31 = rojo) hasta que se resetea con `\u001B[0m`.

## 8. Notas

El `README.md` original del repositorio no describía correctamente el proyecto (mencionaba "ejercicios de algoritmia básica" y un link de clonado con otro nombre de repositorio). Este README reemplaza esa descripción por una que corresponde al código real: un compresor/descompresor LZW con comunicación cliente-servidor.
