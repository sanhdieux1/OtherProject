package com.khoa.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.khoa.entity.Document;
import com.khoa.entity.FormData;
import com.khoa.entity.Multimedia;
import com.khoa.entity.MyUserDetails;
import com.khoa.entity.UserDetailsForm;
import com.khoa.entity.WordDelail;
import com.khoa.entity.WordType;

@Service
@DependsOn("MyClient")
public class ElasticsearchService {

    @Autowired
    public ConvertWordToHTML convertWordToHTML;

    @Value("${INDEX_NAME}")
    public String INDEX_NAME;

    @Value("${MAIN_TYPE_DICTIONARY}")
    public String MAIN_TYPE_DICTIONARY;

    @Value("${PERSONAL_TYPE_DICTIONARY}")
    public String PERSONAL_TYPE_DICTIONARY;
    
    @Value("${MAIN_TYPE_DOCUMENT}")
    public String MAIN_TYPE_DOCUMENT;

    @Value("${USER_TYPE}")
    public String USER_TYPE;

    @Autowired
    private MyClient myClient;

    @Autowired
    public Gson gson;

    public MyClient getMyClient() {
        return myClient;
    }

    public ElasticsearchService() {
    }

    public void setMyClient(MyClient myClient) {
        this.myClient = myClient;
    }

    public boolean keyIsExist(String keySearch, String type) {
        // check exist of key
        GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, type, keySearch).execute().actionGet();
        if (response.isSourceEmpty()) {
            return false;
        } else
            return true;
    }

    public String indexData(WordDelail wordDelail) {
        if (!keyIsExist(wordDelail.getKeySearch(), MAIN_TYPE_DICTIONARY)) {
            String json = gson.toJson(wordDelail);
            IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, MAIN_TYPE_DICTIONARY, wordDelail.getKeySearch()).setSource(json)
                    .execute().actionGet();
            boolean isCreate = responseES.isCreated();
            if (isCreate)
                return "SUCCESS";
            else
                return "FAIL";
        } else {
            return "Key Is Exist";
        }
    }

    public String indexWordPersonal(WordDelail wordDelail) {
        String json = gson.toJson(wordDelail);
        IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, PERSONAL_TYPE_DICTIONARY).setSource(json)
                .execute().actionGet();
        boolean isCreate = responseES.isCreated();
        if (isCreate)
            return "SUCCESS";
        else
            return "FAIL";
    }

    public JsonArray getAll() {
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DICTIONARY).setSize(5000).execute().actionGet();
        SearchHit hit[] = response.getHits().getHits();
        JsonArray jsonArray = new JsonArray();
        for (SearchHit item : hit) {
            JsonObject obj = new JsonParser().parse(item.getSourceAsString()).getAsJsonObject();
            obj.addProperty("id", item.getId());
            jsonArray.add(obj);
        }
        return jsonArray;
    }
    
    public JsonArray getAll(String username) {

        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setQuery(QueryBuilders.multiMatchQuery(username,"byUser")).setTypes(PERSONAL_TYPE_DICTIONARY).setSize(5000).execute().actionGet();
        SearchHit hit[] = response.getHits().getHits();
        JsonArray jsonArray = new JsonArray();
        for (SearchHit item : hit) {
            JsonObject obj = new JsonParser().parse(item.getSourceAsString()).getAsJsonObject();
            obj.addProperty("id", item.getId());
            jsonArray.add(obj);
        }
        return jsonArray;
    }
    public String searchKeyWord(String keyWord) {

        GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, MAIN_TYPE_DICTIONARY, keyWord).execute().actionGet();
        if (response.isSourceEmpty()) {
            return search(keyWord);
        } else {
            // find the key word in all the document
            String arrayDoc = searchInDocument(keyWord);
            JsonObject jsonObjResult = new JsonObject();
            JsonObject obj = (JsonObject) new JsonParser().parse(response.getSourceAsString());
            obj.addProperty("keySearch", response.getId());
            jsonObjResult.add("source", obj.getAsJsonObject());
            jsonObjResult.add("arrayDoc", gson.fromJson(arrayDoc, JsonArray.class));
            jsonObjResult.addProperty("final", "true");

            return jsonObjResult.toString();
        }
    }

    public String searchInDocument(String keyWord) {
        JsonArray jsonArray = new JsonArray();
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DOCUMENT)
                .setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(QueryBuilders.matchPhrasePrefixQuery("document", keyWord)).setSize(600).execute()
                .actionGet();

        SearchHit hit[] = response.getHits().getHits();
        for (SearchHit item : hit) {
            JsonObject obj = gson.fromJson(item.getSourceAsString(), JsonObject.class);
            obj.addProperty("idDoc", item.getId());
            obj.remove("document");
            jsonArray.add(obj);
        }
        return jsonArray.toString();
    }

    /**
     * approximately search in elasticsearch database
     * 
     * @param keyWord
     *            : key word
     * @return
     */
    public String search(String keyWord) {
        JsonObject jsonResult = new JsonObject();
        jsonResult.addProperty("final", "false");

        JsonArray jsonArray = new JsonArray();
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DICTIONARY)
                .setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(QueryBuilders.matchPhrasePrefixQuery("keySearch", keyWord))
                // .setFrom(0).setSize(60).setExplain(true)
                .execute().actionGet();

        SearchHit hit[] = response.getHits().getHits();
        for (SearchHit item : hit) {
            JsonObject obj = new JsonParser().parse(item.getSourceAsString()).getAsJsonObject();
            obj.addProperty("keyword", item.getId());
            jsonArray.add(obj);
        }
        jsonResult.add("source", jsonArray.getAsJsonArray());
        return jsonResult.toString();
    }

    public String autoComplete(String keyWord) {
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DICTIONARY)
                .setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(QueryBuilders.matchPhrasePrefixQuery("keySearch", keyWord))
                // .setFrom(0).setSize(60).setExplain(true)
                .execute().actionGet();

        SearchHit hit[] = response.getHits().getHits();
        String[] string = new String[hit.length];
        int i = 0;
        for (SearchHit item : hit) {
            string[i] = item.getId();
            i++;
        }
        return gson.toJson(string);
    }

    public boolean deleteWord(String key) {
        DeleteResponse response = myClient.getClient().prepareDelete(INDEX_NAME, MAIN_TYPE_DICTIONARY, key).execute().actionGet();
        return response.isFound();
    }

    public boolean deleteUser(String idUser) {
        DeleteResponse response = myClient.getClient().prepareDelete(INDEX_NAME, USER_TYPE, idUser).execute().actionGet();
        return response.isFound();
    }

    public boolean deleteDoc(String idDoc) {
        DeleteResponse response = myClient.getClient().prepareDelete(INDEX_NAME, MAIN_TYPE_DOCUMENT, idDoc).execute().actionGet();
        return response.isFound();
    }

    public String editData(FormData formData) {
        String keySearch = formData.getKeySearch();
        if (keyIsExist(keySearch, MAIN_TYPE_DICTIONARY)) {
            WordDelail wordDelail = new WordDelail();
            wordDelail.setKeySearch(keySearch);
            wordDelail.setDescription(formData.getDescription());
            wordDelail.setByUser("admin");
            wordDelail.setDate(new Date().getTime());

            Multimedia multimedia = new Multimedia();
            multimedia.setImage(formData.getImage());
            multimedia.setAudio(formData.getAudio());
            multimedia.setLinked(formData.getLinked());
            wordDelail.setMultimedia(multimedia);

            String json = gson.toJson(wordDelail);

            IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, MAIN_TYPE_DICTIONARY, keySearch).setSource(json).execute()
                    .actionGet();
            if (responseES.isCreated()) {
                return "FAIL";
            } else
                return "SUCCESS";
        } else {
            return "KEY_IS_NOT_EXIST";
        }
    }

    public String editUpdateDoc(Document document, String ID) {
        if (keyIsExist(ID, MAIN_TYPE_DOCUMENT)) {
            String json = gson.toJson(document);
            UpdateResponse response = myClient.getClient().prepareUpdate(INDEX_NAME, MAIN_TYPE_DOCUMENT, ID).setDoc(json).execute().actionGet();
            return String.valueOf(!response.isCreated());
        } else
            return "DOC_IS_NOT_EXIST";
    }

    public String editUser(UserDetailsForm userDetailsForm) {
        String idUser = userDetailsForm.getUsername();
        String respone = "ERROR";
        // check exist of key
        if (keyIsExist(idUser, USER_TYPE)) {
            String json = gson.toJson(userDetailsForm);
            myClient.getClient().prepareIndex(INDEX_NAME, USER_TYPE, idUser).setSource(json).execute().actionGet();
            respone = "SUCCESS";
        } else
            return "USER_IS_NOT_EXIST";
        return respone;
    }

    public UserDetailsForm getUser(String username) {
        UserDetailsForm userDetailsForm = null;
        if (username != null) {
            GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, USER_TYPE, username).execute().actionGet();
            if (!response.isSourceEmpty()) {
                JsonObject jsonObject = gson.fromJson(response.getSourceAsString(), JsonObject.class);
                userDetailsForm = new UserDetailsForm();
                try {
                    userDetailsForm.setCreateBy(jsonObject.get("createBy").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setLastUpdate(jsonObject.get("lastUpdate").getAsLong());
                } catch (Exception e) {
                    userDetailsForm.setLastUpdate(0);
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setPassword(jsonObject.get("password").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                //
                try {
                    userDetailsForm.setAddress(jsonObject.get("address").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setDateCreate(jsonObject.get("dateCreate").getAsLong());
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setEmail(jsonObject.get("email").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setFullName(jsonObject.get("fullName").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setPhoneNumber(jsonObject.get("phoneNumber").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setSex(jsonObject.get("sex").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setBirthDay(jsonObject.get("birthDay").getAsLong());
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    userDetailsForm.setRole(jsonObject.get("role").getAsString());
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                userDetailsForm.setUsername(username);

            }
        }
        return userDetailsForm;
    }

    public String insertFromFile(File file, WordType wordType) throws FileNotFoundException, Exception {
        JsonObject jsonObj = new JsonObject();
        JsonObject respone = new JsonObject();
        String[] fileData = null;
        fileData = convertWordToHTML.getArrayParagrap(file, wordType);
        if (fileData == null) {
            respone.addProperty("message", "ERROR");
            respone.addProperty("data", "can't convert to html");
        } else {
            for (int i = 0; i < fileData.length; i++) {
                if (fileData[i] != null && !fileData[i].equals("\r\n")) {
                    int position = fileData[i].indexOf(':');
                    if (position != -1) {
                        String keySearch = fileData[i].substring(0, position);
                        String description = fileData[i].substring(position + 1);
                        boolean isSuccess = insertWordClassic(keySearch, description);
                        jsonObj.addProperty(keySearch, isSuccess);
                    }
                }
            }
            respone.addProperty("message", "SUCCESS");
            respone.addProperty("data", jsonObj.toString());
        }
        return respone.toString();
    }

    public boolean insertWordClassic(String keySearch, String description) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((MyUserDetails) principal).getUsername();
        WordDelail wordDelail = new WordDelail();
        wordDelail.setByUser(username);
        wordDelail.setDate(new Date().getTime());
        wordDelail.setDescription(description);
        wordDelail.setKeySearch(keySearch);
        boolean isCreate;
        String source = gson.toJson(wordDelail);
        if (keyIsExist(keySearch, MAIN_TYPE_DICTIONARY)) {
            return false;
        } else {
            IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, MAIN_TYPE_DICTIONARY, keySearch).setSource(source).execute()
                    .actionGet();

            isCreate = responseES.isCreated();
        }
        return isCreate;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    public boolean insertDoc(Document document) {
        if (keyIsExist(document.getIdFile(), MAIN_TYPE_DOCUMENT)) {
            return false;
        } else {
            String source = gson.toJson(document);
            IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, MAIN_TYPE_DOCUMENT, document.getIdFile()).setSource(source)
                    .execute().actionGet();
            return responseES.isCreated();
        }
    }

    public JsonArray getAllDoc() {
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(MAIN_TYPE_DOCUMENT).setSize(5000).execute().actionGet();

        SearchHit hit[] = response.getHits().getHits();

        JsonArray jsonArray = new JsonArray();
        for (SearchHit item : hit) {
            JsonObject obj = new JsonParser().parse(item.getSourceAsString()).getAsJsonObject();
            obj.addProperty("id", item.getId());
            obj.remove("document");
            jsonArray.add(obj);
        }
        return jsonArray;
    }

    public synchronized String createUser(UserDetailsForm myUserDetails, String byUsername) {
        JsonObject responseHTML = new JsonObject();
        if (myUserDetails.validate()) {
            if (keyIsExist(myUserDetails.getUsername(), USER_TYPE)) {
                responseHTML.addProperty("MESSAGE", "ERROR");
                responseHTML.addProperty("DATA", "USER_IS_EXIST");
            } else {
                myUserDetails.setCreateBy(byUsername);
                String source = gson.toJson(myUserDetails);
                IndexResponse responseES = myClient.getClient().prepareIndex(INDEX_NAME, USER_TYPE, myUserDetails.getUsername()).setSource(source)
                        .execute().actionGet();
                boolean isCreate = responseES.isCreated();
                if (isCreate) {
                    responseHTML.addProperty("MESSAGE", "SUCCESS");
                    responseHTML.addProperty("DATA", responseES.getId());
                }
            }
        } else {
            responseHTML.addProperty("MESSAGE", "ERROR");
            responseHTML.addProperty("DATA", "NOT_VALIDATE");
        }
        return responseHTML.toString();
    }

    public JsonArray getAllUser(String role) {

        QueryBuilder query;
        if (role.equals("[ALL]")) {
            query = QueryBuilders.matchAllQuery();
        } else {
            query = QueryBuilders.matchQuery("role", role);
        }
        SearchResponse response = myClient.getClient().prepareSearch(INDEX_NAME).setTypes(USER_TYPE).setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(query) // Query
                .setSize(5000).execute().actionGet();

        SearchHit[] searchHit = response.getHits().getHits();
        JsonArray jsonArray = new JsonArray();
        for (SearchHit hit : searchHit) {
            JsonObject obj = new JsonParser().parse(hit.getSourceAsString()).getAsJsonObject();
            obj.addProperty("id", hit.getId());
            jsonArray.add(obj);
        }

        return jsonArray;

    }

    public Document getDocument(String id) {
        Document result;
        if (keyIsExist(id, MAIN_TYPE_DOCUMENT)) {
            GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, MAIN_TYPE_DOCUMENT, id).execute().actionGet();
            result = gson.fromJson(response.getSourceAsString(), Document.class);
            return result;
        }
        return null;
    }

    public GetResponse getUserDetails(String userName) {
        if (keyIsExist(userName, USER_TYPE)) {
            return myClient.getClient().prepareGet(INDEX_NAME, USER_TYPE, userName).execute().actionGet();
        } else {
            return null;
        }
    }

    public boolean deleteWordPersonal(String idWordPersonal, String username) {
        if (keyIsExist(idWordPersonal, PERSONAL_TYPE_DICTIONARY)) {
            GetResponse response = myClient.getClient().prepareGet(INDEX_NAME, PERSONAL_TYPE_DICTIONARY, idWordPersonal).execute().actionGet();
            WordDelail wordDelail = gson.fromJson(response.getSourceAsString(),WordDelail.class);
            if(wordDelail.getByUser().equals(username)){
                DeleteResponse responseDelete = myClient.getClient().prepareDelete(INDEX_NAME, PERSONAL_TYPE_DICTIONARY, idWordPersonal).execute().actionGet();
                return responseDelete.isFound();
            }
        }
        return false;
    }

}
