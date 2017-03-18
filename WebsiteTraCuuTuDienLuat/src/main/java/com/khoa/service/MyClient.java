package com.khoa.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("MyClient")
public class MyClient {
    @Value("${ELASTICSEARCH_SERVERIP:localhost}")
    private String ELASTICSEARCH_SERVERIP;

    @Value("${ELASTICSEARCH_PORT:9300}")
    private int ELASTICSEARCH_PORT;

    @Value("${INDEX_NAME:dir}")
    private String INDEX_NAME;

    @Value("${MAIN_TYPE_DICTIONARY:drf}")
    private String MAIN_TYPE_DICTIONARY;

    @Value("${PERSONAL_TYPE_DICTIONARY:fch}")
    private String PERSONAL_TYPE_DICTIONARY;

    @Value("${MAIN_TYPE_DOCUMENT:fyrfty}")
    private String MAIN_TYPE_DOCUMENT;

    @Value("${USER_TYPE:xdr}")
    private String USER_TYPE;

    private Client client = null;

    public MyClient() {
       
    }


    private void checkIndexExist() throws IOException {
        ActionFuture<IndicesExistsResponse> exists = client.admin().indices().exists(new IndicesExistsRequest(INDEX_NAME));
        IndicesExistsResponse actionGet = exists.actionGet();
        boolean hasIndex = actionGet.isExists();
        if (!hasIndex) {
            createIndex();
        }else{
            checkMappingExist();
        }
    }

    private void createIndex() throws IOException {
        final CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX_NAME);
        final XContentBuilder mappingDictioncaryType = XContentFactory.jsonBuilder().startObject().startObject(MAIN_TYPE_DICTIONARY)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        createIndexRequestBuilder.addMapping(MAIN_TYPE_DICTIONARY, mappingDictioncaryType);

        final XContentBuilder mappingPersonalType = XContentFactory.jsonBuilder().startObject().startObject(PERSONAL_TYPE_DICTIONARY)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        createIndexRequestBuilder.addMapping(PERSONAL_TYPE_DICTIONARY, mappingPersonalType);
        
        final XContentBuilder mappingUserType = XContentFactory.jsonBuilder().startObject().startObject(USER_TYPE)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        createIndexRequestBuilder.addMapping(USER_TYPE, mappingUserType);
        
        final XContentBuilder mappingDocumentType = XContentFactory.jsonBuilder().startObject().startObject(MAIN_TYPE_DOCUMENT)
                .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                .endObject();
        createIndexRequestBuilder.addMapping(MAIN_TYPE_DOCUMENT, mappingDocumentType);
        
        // MAPPING DONE
        createIndexRequestBuilder.execute().actionGet();
    }

    private void deleteIndex() {
        final IndicesExistsResponse res = client.admin().indices().prepareExists(INDEX_NAME).execute().actionGet();
        if (res.isExists()) {
            final DeleteIndexRequestBuilder delIdx = client.admin().indices().prepareDelete(INDEX_NAME);
            delIdx.execute().actionGet();
        }
    }

    private void checkMappingExist() throws IOException {
        ClusterStateResponse resp = client.admin().cluster().prepareState().execute().actionGet();
        ImmutableOpenMap<String, MappingMetaData> mappings = resp.getState().metaData().index(INDEX_NAME).mappings();
        if (!mappings.containsKey(MAIN_TYPE_DICTIONARY)) {
            final XContentBuilder mappingDictioncaryType = XContentFactory.jsonBuilder().startObject().startObject(MAIN_TYPE_DICTIONARY)
                    .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                    .endObject();
            client.admin().indices().preparePutMapping(INDEX_NAME).setType(MAIN_TYPE_DICTIONARY).setSource(mappingDictioncaryType).execute().actionGet();
        }
        if (!mappings.containsKey(PERSONAL_TYPE_DICTIONARY)) {
            final XContentBuilder mappingPersonalType = XContentFactory.jsonBuilder().startObject().startObject(PERSONAL_TYPE_DICTIONARY)
                    .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                    .endObject();
            client.admin().indices().preparePutMapping(INDEX_NAME).setType(PERSONAL_TYPE_DICTIONARY).setSource(mappingPersonalType).execute().actionGet();
        }
        if (!mappings.containsKey(MAIN_TYPE_DOCUMENT)) {
            final XContentBuilder mappingDocumentType = XContentFactory.jsonBuilder().startObject().startObject(MAIN_TYPE_DOCUMENT)
                    .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                    .endObject();
            client.admin().indices().preparePutMapping(INDEX_NAME).setType(MAIN_TYPE_DOCUMENT).setSource(mappingDocumentType).execute().actionGet();
        }
        if (!mappings.containsKey(USER_TYPE)) {
            final XContentBuilder mappingUserType = XContentFactory.jsonBuilder().startObject().startObject(USER_TYPE)
                    .startObject("_ttl").field("enabled", "true").field("default", "1s").endObject().endObject()
                    .endObject();
            client.admin().indices().preparePutMapping(INDEX_NAME).setType(USER_TYPE).setSource(mappingUserType).execute().actionGet();
        }
    }

    @SuppressWarnings("resource")
    public Client getClient() {
        if (client == null) {
            try {
                client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ELASTICSEARCH_SERVERIP), ELASTICSEARCH_PORT));
                //verify client has connected.
                NodesStatsResponse response = client.admin().cluster().prepareNodesStats().get();
                
                checkIndexExist();
            } catch (IOException e) {
                e.printStackTrace();
                client = null;
            }
        }
        return client;
    }
    /*
     * @PreDestroy
     * public void destroy() {
     * client.close();
     * }
     */

    @SuppressWarnings("resource")
    public void reconnect() {
        try {
            client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ELASTICSEARCH_SERVERIP), ELASTICSEARCH_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            client = null;
        }
    }

}
