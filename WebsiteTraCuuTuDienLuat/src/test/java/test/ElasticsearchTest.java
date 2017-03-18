package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.threadpool.ThreadPoolStats.Stats;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.khoa.entity.Multimedia;
import com.khoa.entity.WordDelail;
import com.khoa.service.MyClient;

public class ElasticsearchTest {
	@Autowired
	Gson gson;


	public Gson getGson() {
		return gson;
	}


	public void setGson(Gson gson) {
		this.gson = gson;
	}


	@SuppressWarnings("unused")
	public static void main (String[]args) throws ElasticsearchException, UnknownHostException{
	    Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(
                InetAddress.getByName("localhost"), 9300));
	    NodesStatsResponse response = client.admin().cluster().prepareNodesStats().get();
	    for (NodeStats nodeStats : response.getNodes()) {
	        Iterator<Stats> iterator = nodeStats.getThreadPool().iterator();
	        while (iterator.hasNext()) {
	            Stats stats = iterator.next();
	            if ("bulk".equals(stats.getName())) {
	                int queue = stats.getQueue();
	                System.out.println(queue);
	            }
	        }
	    }
	    
		String INDEX_NAME="dictionary_db",MAIN_TYPE_DOCUMENT="news_table" ,MAIN_TYPE_DICTIONARY="word_table", USER_TYPE="user_table";
		String PERSONAL_TYPE_DICTIONARY="private_word_table";
		
		
		/*SearchResponse response = client.prepareSearch(INDEX_NAME).setQuery(QueryBuilders.multiMatchQuery("sanhdieu","byUser")).setTypes(PERSONAL_TYPE_DICTIONARY).setSize(5000).execute().actionGet();
		System.out.println(response.getHits().hits().toString());*/
		
//		DeleteResponse response = client.prepareDelete(INDEX_NAME, USER_TYPE, "dba").execute().actionGet();
		
		/*SearchResponse response = client.prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DOCUMENT)
                .setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(QueryBuilders.matchPhrasePrefixQuery("document","Office.Download")).setSize(600).execute()
                .actionGet();*/
		
		WordDelail wordDelail = new WordDelail();
		wordDelail.setByUser("dangkhoa");
		 Date date = new Date();
		wordDelail.setDate(date.getTime());
		wordDelail.setDescription("mo ta");
		wordDelail.setKeySearch("newword");
		
		Multimedia multimedia = new Multimedia();
		wordDelail.setMultimedia(multimedia);
		Gson gson = new Gson();
		String json = gson.toJson(wordDelail);
		IndexResponse responseES = client.prepareIndex(INDEX_NAME, MAIN_TYPE_DICTIONARY, wordDelail.getKeySearch()).setSource(json)
                .execute().actionGet();
        boolean isCreate = responseES.isCreated();
        
		System.out.println();
	}
}
