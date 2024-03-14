package client;

import connection.SocketConnection;
import protocol.Instruction;
import protocol.InstructionBuilder;
import protocol.InvalidStringException;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "localhost";

    private ClientThread thread;

    SocketConnection socketConnection;
    public Client() throws IOException {
        socketConnection = new SocketConnection(SERVER_IP,SocketConnection.PORT);

        thread = new ClientThread(this);
        thread.start();
    }

    public void sendMessage(String msg,int userId) throws IOException {
        socketConnection.writeInstruction(InstructionBuilder.directConnection(userId));

        try{
            if(socketConnection.readInstruction().getName().equals(InstructionBuilder.directConnectionEstablished().getName())){
                socketConnection.writeInstruction(InstructionBuilder.message(msg));
                socketConnection.writeInstruction(InstructionBuilder.directConnectionDisconnect());
            }
        } catch (InvalidStringException e){
            throw new RuntimeException(e);
        }


    }

    public boolean login(String username, String password) throws IOException {
        socketConnection.writeInstruction(InstructionBuilder.login(username,password));

        try{
            if(socketConnection.readInstruction().getName().equals("DONE")){
                return true;
            }
            return false;
        } catch (InvalidStringException e){
            throw new RuntimeException(e);
        }

    }

    public boolean signup(String username, String password) throws IOException {
        socketConnection.writeInstruction(InstructionBuilder.signup(username,password));

        try{
            if(socketConnection.readInstruction().getName().equals("DONE")){
                return true;
            }
            return false;
        } catch (InvalidStringException e){
            throw new RuntimeException(e);
        }

    }
}
