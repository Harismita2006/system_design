import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoUtil {

    private static MongoClient client;
    private static MongoDatabase database;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            client = MongoClients.create(
                "YOUR_MONGODB_ATLAS_URL"
            );
            database = client.getDatabase("chatdb");
        }
        return database;
    }
}
