package protocol;

public class InstructionBuilder {
    public static Instruction sendMessage(String message,String receiver){
        ParamList params = new ParamList();
        params.put("msg",message);
        params.put("receiver",receiver);
        return new Instruction("SENDMESSAGE",params);
    }

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

}
