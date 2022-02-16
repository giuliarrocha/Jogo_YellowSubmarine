import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Servidor {
  final static int porto = 12345;

  public static void main(String[] args) {
    ServerSocket serverSocket = null;

    try {
      serverSocket = new ServerSocket(porto);
    } catch (IOException e) {
      System.out.println("Could not listen on port: " + porto + ", " + e);
      System.exit(1);
    }

    System.out.println("Servidor esperando jogadores...");
    StreamsDeSaida saida = new StreamsDeSaida();
    Logica logica = new Logica();
    for (int i = 0; i < 2; i++) {
      Socket clientSocket = null;
      try {
        clientSocket = serverSocket.accept();
      } catch (IOException e) {
        System.out.println("Accept failed: " + porto + ", " + e);
        System.exit(1);
      }

      System.out.println("Jogador chegou!");

      new ServindoCliente(clientSocket, saida, logica).start();

    }
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}