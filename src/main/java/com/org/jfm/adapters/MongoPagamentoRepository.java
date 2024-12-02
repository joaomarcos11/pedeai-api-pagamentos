// package com.org.jfm.adapters;

// import com.org.jfm.domain.entities.Pagamento;
// import com.org.jfm.domain.ports.PagamentoRepository;
// import com.mongodb.client.MongoClient;
// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoDatabase;
// import com.mongodb.MongoClientSettings;
// import org.bson.Document;
// import org.bson.codecs.configuration.CodecProvider;
// import org.bson.codecs.configuration.CodecRegistries;
// import org.bson.codecs.configuration.CodecRegistry;
// import org.bson.codecs.pojo.PojoCodecProvider;
// import org.bson.conversions.Bson;
// import org.bson.types.ObjectId;

// import jakarta.enterprise.context.ApplicationScoped;

// import static com.mongodb.client.model.Filters.eq;

// @ApplicationScoped
// public class MongoPagamentoRepository implements PagamentoRepository {

//     private final MongoCollection<Pagamento> coll;

//     public MongoPagamentoRepository() {
//         CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
//                 .register("com.org.jfm.domain.entities")
//                 .automatic(true)
//                 .build();

//         CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
//                 MongoClientSettings.getDefaultCodecRegistry(),
//                 CodecRegistries.fromProviders(pojoCodecProvider));

//         MongoClient mongoClient = MongoClients.create(System.getenv("MONGO_URI"));
//         MongoDatabase database = mongoClient.getDatabase("test")
//                 .withCodecRegistry(codecRegistry);

//         this.coll = database.getCollection("pagamentos", Pagamento.class);
//     }

//     @Override
//     public String add(Pagamento pagamento) {
//         return coll.insertOne(pagamento).getInsertedId().asObjectId().getValue().toHexString();
//     }

//     @Override
//     public Pagamento findById(String id) {
//         return coll.find(eq("_id", new ObjectId(id))).first();
//     }

//     @Override
//     public long updateStatus(String id, String status, String dateApproved) {
//         Bson filter = eq("_id", new ObjectId(id));
//         Bson update = new Document("$set", new Document()
//             .append("status", status)
//             .append("dateApproved", dateApproved));
//         return coll.updateOne(filter, update).getModifiedCount();
//     }

//     @Override
//     public long deletePagamento(String id) {
//         Bson filter = eq("_id", new ObjectId(id));
//         return coll.deleteOne(filter).getDeletedCount();
//     }
// }

// package com.org.jfm.adapters;

// import com.mongodb.MongoClientSettings;
// import com.mongodb.client.MongoClient;
// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoDatabase;
// import com.org.jfm.domain.entities.Pagamento;
// import com.org.jfm.domain.ports.PagamentoRepository;

// import org.bson.Document;
// import org.bson.codecs.configuration.CodecProvider;
// import org.bson.codecs.configuration.CodecRegistries;
// import org.bson.codecs.configuration.CodecRegistry;
// import org.bson.codecs.pojo.PojoCodecProvider;
// import org.bson.conversions.Bson;
// import org.bson.types.ObjectId;

// import jakarta.enterprise.context.ApplicationScoped;

// import static com.mongodb.client.model.Filters.eq;

// @ApplicationScoped
// public class MongoPagamentoRepository implements PagamentoRepository {
//     private final MongoCollection<Pagamento> coll;

//     public MongoPagamentoRepository() {
//         this(MongoClients.create(System.getenv("MONGO_URI")));
//     }

//     public MongoPagamentoRepository(MongoClient mongoClient) {
//         CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
//             .register("com.org.jfm.domain.entities")
//             .automatic(true)
//             .build();
            
//         CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
//             MongoClientSettings.getDefaultCodecRegistry(),
//             CodecRegistries.fromProviders(pojoCodecProvider)
//         );

//         MongoDatabase database = mongoClient.getDatabase("test")
//             .withCodecRegistry(codecRegistry);

//         this.coll = database.getCollection("pagamentos", Pagamento.class);
//     }

//     @Override
//     public String add(Pagamento pagamento) {
//         coll.insertOne(pagamento);
//         return pagamento.getId().toHexString();
//     }

//     @Override
//     public Pagamento findById(String id) {
//         return coll.find(eq("_id", new ObjectId(id))).first();
//     }

//     @Override
//     public long updateStatus(String id, String status, String dateApproved) {
//         Bson filter = eq("_id", new ObjectId(id));
//         Bson update = new Document("$set", new Document()
//             .append("status", status)
//             .append("dateApproved", dateApproved));
//         return coll.updateOne(filter, update).getModifiedCount();
//     }

//     @Override
//     public long deletePagamento(String id) {
//         Bson filter = eq("_id", new ObjectId(id));
//         return coll.deleteOne(filter).getDeletedCount();
//     }
// }

package com.org.jfm.adapters;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.org.jfm.domain.entities.Pagamento;
import com.org.jfm.domain.ports.PagamentoRepository;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class MongoPagamentoRepository implements PagamentoRepository {
    private final MongoCollection<Pagamento> coll;

    @Inject
    public MongoPagamentoRepository(MongoClient mongoClient) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .register("com.org.jfm.domain.entities")
                .automatic(true)
                .build();

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider));

        MongoDatabase database = mongoClient.getDatabase("pedeai")
                .withCodecRegistry(codecRegistry);

        this.coll = database.getCollection("pagamentos", Pagamento.class);
    }

    @Override
    public String add(Pagamento pagamento) {
        coll.insertOne(pagamento);
        return pagamento.getId().toHexString();
    }

    @Override
    public Pagamento findById(String id) {
        return coll.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public long updateStatus(String id, String status, String dateApproved) {
        Bson filter = eq("_id", new ObjectId(id));
        Bson update = new Document("$set", new Document()
                .append("status", status)
                .append("dateApproved", dateApproved));
        return coll.updateOne(filter, update).getModifiedCount();
    }

    @Override
    public long deletePagamento(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        return coll.deleteOne(filter).getDeletedCount();
    }
}