package connection;

import protocol.Instruction;
import protocol.InvalidStringException;
import protocol.ProtocolTranslator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection {
    public static char BREAK_CHAR = 10;
    public static int PORT = 60000;
    private Socket socket;

    private OutputStream outputStream;
    private InputStream inputStream;

    public SocketConnection(String ip) throws IOException {
        socket = new Socket(ip,PORT);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void writeString(String message) throws IOException {
        for(char c : message.toCharArray()){
            outputStream.write(c);
        }

        outputStream.write(BREAK_CHAR);
    }

    public String readString() throws IOException {
        String msg = "";
        int currentChar;

        while((currentChar = inputStream.read()) != BREAK_CHAR){
            if(currentChar == -1){
                throw new IOException("InputStream closed");
            }

            msg += (char) currentChar;
        }

        return msg;
    }

    public void writeInstruction(Instruction instruction) throws IOException{
        writeString(ProtocolTranslator.encode(instruction));
    }

    public Instruction readInstruction() throws IOException, InvalidStringException {
        String msg = readString();

        return ProtocolTranslator.decode(msg);
    }


    public Socket getSocket() {
        return socket;
    }
}
