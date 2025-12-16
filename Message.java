import org.bson.Document;

public class Message {
    public String user;
    public String text;

    public Document toDocument() {
        return new Document("user", user)
                .append("text", text)
                .append("time", System.currentTimeMillis());
    }
}
