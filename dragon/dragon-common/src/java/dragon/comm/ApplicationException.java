package dragon.comm;

@javax.ejb.ApplicationException
public class ApplicationException extends RuntimeException {

    private String code;
    
    /**
     * Creates a new instance of <code>ServerException</code> without detail message.
     */
    public ApplicationException() {
    }

    /**
     * Constructs an instance of <code>ServerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
