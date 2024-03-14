package client;

import connection.SocketConnection;
import protocol.Instruction;
import protocol.InstructionBuilder;
import protocol.InvalidStringException;

import java.io.IOException;

public class ClientThread extends Thread{
    Client client;

    public ClientThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        super.run();

        SocketConnection socketConnection = client.socketConnection;

        while(true){
            try {
                Instruction inst = socketConnection.readInstruction();

                socketConnection.writeInstruction(inst);
//                switch(inst.getName()){
//                    case "DIRECT_CONNECTION_REQUEST":
//                        socketConnection.writeInstruction(InstructionBuilder.done());
//                        break;
//
//                    default:
//                        socketConnection.writeInstruction(InstructionBuilder.error("Invalid Instruction"));
//                }

            } catch (IOException e) {
                //throw new RuntimeException(e);
            } catch (InvalidStringException e) {
                //throw new RuntimeException(e);
            }

        }
    }
}
