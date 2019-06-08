package customException;

public class CoustemExecption extends Exception{
    String message;
    public CoustemExecption(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
