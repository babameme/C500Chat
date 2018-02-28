package dataframe;
import java.io.Serializable;

public class Packet implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name = new String(), message = new String(), url = new String();

    public Packet(String name, String url, String msg) {

        this.setName(name);
        this.setMessage(msg);
        this.setUrl(url);
    }

    public boolean equalName(String string){
        if (string.equalsIgnoreCase(this.getName())){
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
