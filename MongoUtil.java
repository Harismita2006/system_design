import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {

    private static MongoClient client;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            client = MongoClients.create(
                "mongodb+srv://harismita:12345678h@cluster0.rbl8jge.mongodb.net/?appName=Cluster0"
            );
            database = client.getDatabase("chatdb");
        }
        return database;
    }
}
