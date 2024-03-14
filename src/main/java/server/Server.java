package server;

import connection.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 60000;

    private ArrayList<ComThread> comThreads = new ArrayList<>();
    public void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);

            while(true) {
                ComThread comThread = new ComThread(serverSocket.accept());
                comThread.start();
                comThreads.add(comThread);
            }

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
