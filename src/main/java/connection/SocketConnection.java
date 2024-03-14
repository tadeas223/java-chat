package connection;

import protocol.Instruction;
import protocol.InvalidStringException;
import protocol.ProtocolTranslator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection {
    public static final char BREAK_CHAR = 10;
    private Socket socket;

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }


    public Socket getSocket() {
        return socket;
    }

    public String readString() throws IOException {
        String msg = "";
        int currentChar;

        while((currentChar = socket.getInputStream().read()) != BREAK_CHAR){
            if(currentChar == -1){
                throw new IOException("InputStream closed");
            }

            msg += (char) currentChar;
        }

        return msg;
    }

    public void writeString(String message) throws IOException {
        for(char c : message.toCharArray()){
            socket.getOutputStream().write(c);
        }

        socket.getOutputStream().write(BREAK_CHAR);
    }

    public void writeInstruction(Instruction instruction) throws IOException{
        writeString(ProtocolTranslator.encode(instruction));
    }

    public Instruction readInstruction() throws IOException, InvalidStringException {
        String msg = readString();

        return ProtocolTranslator.decode(msg);
    }

    public void close() throws IOException {
        socket.close();
    }
}
