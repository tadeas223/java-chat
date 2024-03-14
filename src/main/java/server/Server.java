package server;

import connection.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(SocketConnection.PORT);

        while(true){
            ServerConnectionHandler connectionHandler = new ServerConnectionHandler(new SocketConnection(serverSocket.accept()));
            connectionHandler.start();
        }
    }


}
