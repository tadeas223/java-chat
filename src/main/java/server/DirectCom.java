package server;

import connection.SocketConnection;
import protocol.InstructionBuilder;

import java.io.IOException;

public class DirectCom {
    private SocketConnection connection1;
    private SocketConnection connection2;

    private DirectComThread thread1;
    private DirectComThread thread2;

    public DirectCom(SocketConnection connection1, SocketConnection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;

        try{
            connection1.writeInstruction(InstructionBuilder.connection(true));
            connection2.writeInstruction(InstructionBuilder.connection(true));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void start(){
        thread1 = new DirectComThread(connection1,connection2);
        thread2 = new DirectComThread(connection2,connection1);

        thread1.start();
        thread2.start();
    }

    public void kill(){
        thread1.kill();
        thread2.kill();
    }


}
