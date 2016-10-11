package tiy.Timeline;

/**
 * Created by fenji on 10/11/2016.
 */
public class Error implements Failable{
    String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Error() {
    }

    public Error(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
