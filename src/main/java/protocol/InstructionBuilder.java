package protocol;

public class InstructionBuilder {

    public static Instruction login(String username,String password){
        ParamList params = new ParamList();
        params.put("username",username);
        params.put("password",password);
        return new Instruction("LOGIN",params);
    }

    public static Instruction signup(String username,String password){
        ParamList params = new ParamList();
        params.put("username",username);
        params.put("password",password);
        return new Instruction("SIGNUP",params);
    }

    public static Instruction error(String msg){
        return new Instruction("ERROR",new ParamList("msg",msg));
    }

    public static Instruction done(){
        return new Instruction("DONE");
    }

    public static Instruction directConnection(int userId){
        return new Instruction("DIRECT_CONNECTION",new ParamList("id",String.valueOf(userId)));
    }

    public static Instruction connection(boolean isDirect){
        if(isDirect){
            return new Instruction("CONNECTION",new ParamList("con","direct"));
        } else {
            return new Instruction("CONNECTION",new ParamList("con","server"));
        }
    }

    public static Instruction directConnectionRequest(int userId){
        return new Instruction("DIRECT_CONNECTION_REQUEST",new ParamList("id",String.valueOf(userId)));
    }

    public static Instruction accept(){
        return new Instruction("ACCEPT");
    }
    public static Instruction directConnectionEstablished(){
        return new Instruction("DIRECT_CONNECTION_ESTABLISHED");
    }

    public static Instruction directConnectionDisconnect(){
        return new Instruction("DIRECT_CONNECTION_DISCONNECT");
    }

    public static Instruction message(String message){
        return new Instruction("MESSAGE",new ParamList("msg",message));
    }

    public static Instruction output(String output){
        return new Instruction("OUTPUT",new ParamList("out",output));
    }

    public static Instruction getId(){
        return new Instruction("GET_ID");
    }

    public static Instruction logout(){
        return new Instruction("LOGOUT");
    }

}
