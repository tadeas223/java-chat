package server;

import connection.SocketConnection;

public class ServerConnectionHandler extends Thread{
    private boolean kill = false;
    private SocketConnection socketConnection;

    public ServerConnectionHandler(SocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }

    public void handle(){

    }

    public void kill(){
        kill = true;
    }


    @Override
    public void run() {
        super.run();

        while (!kill){
            handle();
        }
    }
}
