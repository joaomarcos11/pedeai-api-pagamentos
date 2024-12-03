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
    void testUpdateStatus() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(new ObjectId());
        pagamento.setPedidoID(12345L);
        pagamento.setValue(100.50);
        pagamento.setStatus("pending");

        UpdateResult updateResult = mock(UpdateResult.class);
        when(updateResult.getModifiedCount()).thenReturn(1L);
        when(collection.updateOne(any(Bson.class), any(Bson.class))).thenReturn(updateResult);

        long modifiedCount = pagamentoRepository.updateStatus(pagamento.getId().toHexString(), "approved",
                Instant.now().toString());

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