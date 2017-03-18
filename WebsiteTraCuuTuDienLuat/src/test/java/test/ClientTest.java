package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.ActionFuture;
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

public class ClientTest {
    public String ELASTICSEARCH_SERVERIP = "localhost";

    public int ELASTICSEARCH_PORT = 9300;

    public String INDEX_NAME = "dictionary";

    public String MAIN_TYPE_DICTIONARY = "word_table_2";

    public String PERSONAL_TYPE_DICTIONARY = "private_word_table";

    public String MAIN_TYPE_DOCUMENT = "news_table_2";

    public String USER_TYPE = "user_table";

    private Client client = null;

    public ClientTest() {
        try {
            client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ELASTICSEARCH_SERVERIP), ELASTICSEARCH_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            client = null;
        }
        try {
            checkIndexExist();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
            } catch (UnknownHostException e) {
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

    public static void main(String[] args) {
        ClientTest test = new ClientTest();
    }
}
