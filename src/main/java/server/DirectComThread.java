package server;

import connection.SocketConnection;

import java.io.IOException;

public class DirectComThread extends Thread {
    private boolean kill = false;
    private SocketConnection connection1;
    private SocketConnection connection2;

    public DirectComThread(SocketConnection connection1, SocketConnection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }

    public void kill(){
        kill = true;
    }
    @Override
    public void run() {
        super.run();

        while(!kill){
            try {
                String str = connection1.readString();
                connection2.writeString(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
