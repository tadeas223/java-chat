package protocol;

public class Instruction {
    private ParamList paramList;
    private String name;

    public Instruction(String name,ParamList paramList){
        this.name = name;
        this.paramList = paramList;
    }

    public Instruction(String name){
        this.name = name;
    }

    public ParamList getParamList() {
        return paramList;
    }

    public String getName() {
        return name;
    }

    public String getParam(String key){
        return paramList.get(key);
    }

    @Override
    public String toString() {
        return ProtocolTranslator.encode(this);
    }
}
