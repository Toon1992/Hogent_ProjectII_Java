package exceptions;

/**
 * Created by donovandesmedt on 20/04/16.
 */
public class MultiException extends IllegalArgumentException{
    public MultiException() {

    }

    public MultiException(String s) {
        super(s);
    }
}
