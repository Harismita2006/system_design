import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatServer {

    private static Set<Session> clients = new CopyOnWriteArraySet<>();
    private static MongoCollection<Document> collection =
            MongoUtil.getDatabase().getCollection("messages");

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);

        for (Document doc : collection.find()) {
            try {
                session.getBasicRemote().sendText(
                    doc.getString("user") + ": " + doc.getString("text")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String[] parts = message.split(":", 2);

        Message msg = new Message();
        msg.user = parts[0];
        msg.text = parts[1];

        collection.insertOne(msg.toDocument());

        for (Session s : clients) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }
}
