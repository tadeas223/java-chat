package server;

import connection.SocketConnection;
import protocol.Instruction;
import protocol.InstructionBuilder;
import protocol.InvalidStringException;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ComThread extends Thread{
    private int userId;
    private SocketConnection connection;

    private boolean kill = false;

    public ComThread(SocketConnection connection) {
        this.connection = connection;
    }

    public ComThread(Socket socket) {
        this.connection = new SocketConnection(socket);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void killConnection(){
        kill = true;
    }

    public void handle() throws IOException {
        try{
            Instruction request = connection.readInstruction();

            switch (request.getName()){
                case "HELLO_WORLD":
                    connection.writeInstruction(new Instruction("HELLO_WORLD"));
                    break;
                case "LOGIN":
                    login(request);
                    break;
                case "SIGN_UP":
                    signup(request);
                    break;
                default:
                    connection.writeInstruction(InstructionBuilder.error("Invalid Instruction"));
                    break;
            }

        } catch (InvalidStringException e){
            connection.writeInstruction(InstructionBuilder.error("Cant read this statement"));
        } catch (SQLException e){
            connection.writeInstruction(InstructionBuilder.error("Database Error"));
        }


    }

    private void login(Instruction instruction) throws SQLException {
        String username = instruction.getParam("username");
        String password = instruction.getParam("password");

        SQLConnection sqlConnection = new SQLConnection();

        sqlConnection.connect();

        userId = sqlConnection.login(username,password);

        sqlConnection.close();
    }

    private void signup(Instruction instruction) throws SQLException {
        String username = instruction.getParam("username");
        String password = instruction.getParam("password");

        SQLConnection sqlConnection = new SQLConnection();

        sqlConnection.connect();

        userId = sqlConnection.signup(username,password);

        sqlConnection.close();
    }
    @Override
    public void run() {
        super.run();

        while(!kill) {
            try {
                handle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
