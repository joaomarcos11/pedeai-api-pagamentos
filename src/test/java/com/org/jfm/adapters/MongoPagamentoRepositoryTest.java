// package com.org.jfm.adapters;

// import static org.mockito.Mockito.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.mockito.MockitoAnnotations;
// import com.org.jfm.domain.entities.Pagamento;
// import org.bson.types.ObjectId;
// import java.util.Arrays;
// import java.util.List;

// class MongoPagamentoRepositoryTest {

//     @Mock
//     private MongoPagamentoRepository repository;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testAdd() {
//         Pagamento pagamento = new Pagamento();
//         pagamento.setId(new ObjectId());
//         pagamento.setPedidoID(12345L);
//         pagamento.setValue(100.50);

//         when(repository.add(any(Pagamento.class))).thenReturn(pagamento.getId().toHexString());

//         String id = repository.add(pagamento);
        
//         assertNotNull(id);
//         verify(repository).add(pagamento);
//     }

//     @Test
//     void testFindById() {
//         Pagamento pagamento = new Pagamento();
//         String id = new ObjectId().toHexString();
        
//         when(repository.findById(id)).thenReturn(pagamento);
        
//         Pagamento result = repository.findById(id);
        
//         assertNotNull(result);
//         verify(repository).findById(id);
//     }

//     @Test
//     void testUpdateStatus() {
//         String id = new ObjectId().toHexString();
//         String status = "approved";
        
//         when(repository.updateStatus(id, status, "someAdditionalArgument")).thenReturn(1L);
        
//         long result = repository.updateStatus(id, status, "someAdditionalArgument");
        
//         assertEquals(1L, result);
//         verify(repository).updateStatus(id, status, "someAdditionalArgument");
//     }

//     @Test
//     void testDeletePagamento() {
//         String id = new ObjectId().toHexString();
        
//         when(repository.deletePagamento(id)).thenReturn(1L);
        
//         long result = repository.deletePagamento(id);
        
//         assertEquals(1L, result);
//         verify(repository).deletePagamento(id);
//     }
// }
package com.org.jfm.adapters;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.org.jfm.domain.entities.Pagamento;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MongoPagamentoRepositoryTest {

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoDatabase database;

    @Mock
    private MongoCollection<Pagamento> collection;

    @Mock
    private FindIterable<Pagamento> findIterable;

    private MongoPagamentoRepository pagamentoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mongoClient.getDatabase(anyString())).thenReturn(database);
        when(database.withCodecRegistry(any())).thenReturn(database);
        when(database.getCollection(anyString(), eq(Pagamento.class))).thenReturn(collection);
        pagamentoRepository = new MongoPagamentoRepository(mongoClient);
    }

    @Test
    void testConstructor() {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .register("com.org.jfm.domain.entities")
                .automatic(true)
                .build();

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider));

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("test")
                .withCodecRegistry(codecRegistry);

        MongoPagamentoRepository repository = new MongoPagamentoRepository(mongoClient);

        assertNotNull(repository);
    }

    @Test
    void testAdd() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("pending");

        doAnswer(invocation -> {
            Pagamento p = invocation.getArgument(0);
            p.setId(new ObjectId());
            return null;
        }).when(collection).insertOne(any(Pagamento.class));

        String id = pagamentoRepository.add(pagamento);

        assertNotNull(id);
        verify(collection).insertOne(pagamento);
    }

 @Test
    void testFindById() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("approved");

        when(collection.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(pagamento);

        Pagamento result = pagamentoRepository.findById(pagamento.getId().toHexString());

        assertNotNull(result);
        assertEquals(pagamento.getId(), result.getId());
    }

    @Test
    void testUpdateStatus() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("pending");

        UpdateResult updateResult = mock(UpdateResult.class);
        when(updateResult.getModifiedCount()).thenReturn(1L);
        when(collection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);

        long modifiedCount = pagamentoRepository.updateStatus(pagamento.getId().toHexString(), "approved", Instant.now().toString());

        assertEquals(1, modifiedCount);
        verify(collection).updateOne(any(Bson.class), any(Bson.class));
    }

    @Test
    void testDeletePagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("pending");

        DeleteResult deleteResult = mock(DeleteResult.class);
        when(deleteResult.getDeletedCount()).thenReturn(1L);
        when(collection.deleteOne(any(Bson.class))).thenReturn(deleteResult);

        long deletedCount = pagamentoRepository.deletePagamento(pagamento.getId().toHexString());

        assertEquals(1, deletedCount);
        verify(collection).deleteOne(any(Bson.class));
    }
}