package server;

import connection.SocketConnection;
import protocol.Instruction;
import protocol.InstructionBuilder;
import protocol.InvalidStringException;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComThread extends Thread{
    private boolean disable = false;
    private int userId = -1;
    private SocketConnection connection;
    private Server server;
    private boolean kill = false;

    private ComThread dcComThread = null;

    public ComThread(SocketConnection connection,Server server) {
        this.connection = connection;
        this.server = server;
    }

    public ComThread(Socket socket,Server server) {
        this.connection = new SocketConnection(socket);
        this.server = server;
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
        try {
            Instruction request = connection.readInstruction();

            if (dcComThread != null) {
                if (request.getName().equals(InstructionBuilder.accept().getName())) {
                    dcComThread.directConnectionAccept(this);
                }
            } else {
                switch (request.getName()) {
                    case "HELLO_WORLD":
                        connection.writeInstruction(new Instruction("HELLO_WORLD"));
                        break;
                    case "LOGIN":
                        login(request);
                        break;
                    case "SIGN_UP":
                        signup(request);
                        break;
                    case "GET_ID":
                        if (userId != -1) {
                            connection.writeInstruction(InstructionBuilder.output(String.valueOf(userId)));
                        } else {
                            connection.writeInstruction(InstructionBuilder.error("User not logged in"));
                        }
                        break;
                    case "LOGOUT":
                        if (userId != -1) {
                            userId = -1;
                            connection.writeInstruction(InstructionBuilder.done());
                        } else {
                            connection.writeInstruction(InstructionBuilder.error("User not logged in"));
                        }
                        break;
                    case "DIRECT_CONNECTION":
                        directConnection(request);
                        break;
                    default:
                        connection.writeInstruction(InstructionBuilder.error("Invalid Instruction"));
                        break;
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidStringException e) {
            throw new RuntimeException(e);
        }

    }

    private void login(Instruction instruction) throws SQLException, IOException {
        String username = instruction.getParam("username");
        String password = instruction.getParam("password");

        SQLConnection sqlConnection = new SQLConnection();

        sqlConnection.connect();

        userId = sqlConnection.login(username,password);

        sqlConnection.close();

        if(userId != -1){
            connection.writeInstruction(InstructionBuilder.done());
        } else {
            connection.writeInstruction(InstructionBuilder.error("Invalid username or password"));
        }
    }

    private void signup(Instruction instruction) throws SQLException, IOException {
        String username = instruction.getParam("username");
        String password = instruction.getParam("password");

        SQLConnection sqlConnection = new SQLConnection();

        sqlConnection.connect();

        userId = sqlConnection.signup(username,password);

        sqlConnection.close();

        connection.writeInstruction(InstructionBuilder.done());
    }

    private void directConnection(Instruction instruction) throws IOException {
        int id = Integer.parseInt(instruction.getParam("id"));

        for(ComThread thread : server.getComThreads()){
            if(thread.userId == id){
                thread.connection.writeInstruction(InstructionBuilder.directConnectionRequest(userId));
                thread.directConnectionRequest(this);
            }
        }
    }

    public void directConnectionRequest(ComThread comThread){
        dcComThread = comThread;
    }

    public void directConnectionAccept(ComThread comThread){
        disable = true;
        comThread.setDisable(true);
        DirectCom directCom = new DirectCom(this.connection,comThread.connection);
        directCom.start();
    }

    public void setDisable(boolean disable){
        this.disable = disable;
    }

    @Override
    public void run() {
        super.run();

        try {
            connection.writeInstruction(InstructionBuilder.connection(false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(!kill) {
            try {
                if(!disable){
                    handle();
                }
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
