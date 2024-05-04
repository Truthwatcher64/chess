package server;

public class ServerException extends Exception{
    int errorCode;
    public ServerException(String msg, int code){
        super(msg);
        this.errorCode=code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
