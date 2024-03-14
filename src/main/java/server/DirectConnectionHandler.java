package server;

import connection.SocketConnection;
import protocol.Instruction;
import protocol.InstructionBuilder;
import protocol.InvalidStringException;

import java.io.IOException;

public class DirectConnectionHandler {

    SocketConnection startCon;

    SocketConnection endCon;

    public DirectConnectionHandler(SocketConnection startCon, SocketConnection endCon) {
        this.startCon = startCon;
        this.endCon = endCon;
    }

    public void startConnection() {
        while(true) {
            try{
                Instruction inst = startCon.readInstruction();

                endCon.writeInstruction(startCon.readInstruction());

                if(inst.getName().equals(InstructionBuilder.directConnectionDisconnect().getName())){
                    break;
                }

            } catch (Exception e){
                //throw new RuntimeException(e);
            }
        }
    }
}
