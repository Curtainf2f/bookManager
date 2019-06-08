package dataProcess;

public class DataProcess {
    public static String process(String a){
        if(a == null) return "null";
        else return "'" + a + "'";
    }
    public static String process(Integer a){
        if(a == null) return "null";
        else return a.toString();
    }
}
