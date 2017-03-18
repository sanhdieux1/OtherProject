package com.khoa.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.khoa.entity.Document;
import com.khoa.entity.FileRespone;
import com.khoa.entity.FormData;
import com.khoa.entity.MultifileRespone;
import com.khoa.entity.Multimedia;
import com.khoa.entity.MyUserDetails;
import com.khoa.entity.UserDetailsForm;
import com.khoa.entity.WordDelail;
import com.khoa.entity.WordType;
import com.khoa.service.ConvertWordToHTML;
import com.khoa.service.ElasticsearchService;
import com.khoa.ultils.Ultils;

@Controller
@RequestMapping("/admin")
public class AdminControler {

    @Value("${PATH_FILE:fileUpLoad}")
    public String PATH_FILE;

    @Value("${PATH_DOC:directoryDocPath}")
    public String PATH_DOC;

    @Value("${PATH_HTML:directoryHTML}")
    public String PATH_HTML;

    @Autowired
    public Gson gson;

    @Autowired
    public ConvertWordToHTML convertWordToHTML;

    @Autowired
    public ElasticsearchService elasticserService;

    @RequestMapping(value = "/deleteWord", method = RequestMethod.GET)
    @ResponseBody
    public boolean delete(@RequestParam("keyword") String keyword) {
        boolean kq = elasticserService.deleteWord(keyword);
        return kq;
    }

    @RequestMapping(value = "/deleteDoc", method = RequestMethod.GET)
    @ResponseBody
    public boolean deleteDoc(@RequestParam("idDoc") String idDoc) {
        boolean kq = elasticserService.deleteDoc(idDoc);
        if (kq) {
            File folderDoc = new File(PATH_DOC);
            File[] listFile = folderDoc.listFiles();
            for (File file : listFile) {
                String fileName = file.getName();
                if (fileName.contains(idDoc)) {
                    file.delete();
                }
            }
            File fileHTML = new File(PATH_HTML + "\\" + idDoc + ".html");
            if (fileHTML.exists())
                fileHTML.delete();
        }
        return kq;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ResponseBody
    public JsonObject deleteUser(@RequestParam("idUser") String idUser) {
        JsonObject responeHTML = new JsonObject();
        UserDetailsForm userDetailsForm = elasticserService.getUser(idUser);
        boolean valid = false;
        for (String auth : getAuthorities()) {
            Set<String> authsCanDelete = getAuthCanDelete(auth);
            if (authsCanDelete.contains(userDetailsForm.getRole())) {
                valid = true;
            }
        }
        if (valid) {
            if (elasticserService.deleteUser(idUser)) {
                responeHTML.addProperty("MESSAGE", "DB_ERROR");
            } else {
                responeHTML.addProperty("MESSAGE", "SUCCESS");
            }
        } else {
            responeHTML.addProperty("MESSAGE", "NOT_PERMISSION");
        }
        return responeHTML;
    }

    @RequestMapping(value = "/insertWord", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String insertWord(@RequestBody FormData formData) {
        WordDelail wordDelail = new WordDelail();
        wordDelail.setKeySearch(formData.getKeySearch());
        wordDelail.setDescription(formData.getDescription());
        wordDelail.setByUser(getCurrentUserName());
        wordDelail.setDate(new Date().getTime());

        Multimedia multimedia = new Multimedia();
        multimedia.setImage(formData.getImage());
        multimedia.setAudio(formData.getAudio());
        multimedia.setLinked(formData.getLinked());
        wordDelail.setMultimedia(multimedia);

        String jsonResponse = "{\"message\":\"" + elasticserService.indexData(wordDelail) + "\"}";
        return jsonResponse;
    }

    @RequestMapping(value = "/editWord", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String editWord(@RequestBody FormData formData) {
        String jsonResponse = "{\"message\":\"" + elasticserService.editData(formData) + "\"}";
        return jsonResponse;
    }

    @RequestMapping(value = "/editDoc", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String editDoc(@RequestParam("title") String title, @RequestParam("ID") String ID) {
        Document document = new Document();
        document.setTitle(title);
        String jsonResponse = "{\"message\":\"" + elasticserService.editUpdateDoc(document, ID) + "\"}";
        return jsonResponse;
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String editUser(@RequestBody UserDetailsForm userDetailsForm) {
        JsonObject  responeHTML = new JsonObject();
        UserDetailsForm userAfter = elasticserService.getUser(userDetailsForm.getUsername());
        if (userAfter != null) {
            userAfter.setLastUpdate(new Date().getTime());
            userAfter.setAddress(userDetailsForm.getAddress());
            userAfter.setBirthDay(userDetailsForm.getBirthDay());
            userAfter.setEmail(userDetailsForm.getEmail());
            userAfter.setFullName(userDetailsForm.getFullName());
            userAfter.setLastUpdate(new Date().getTime());
            userAfter.setPhoneNumber(userDetailsForm.getPhoneNumber());
            try {
                if (userDetailsForm.getSex().equals("male"))
                    userAfter.setSex(userDetailsForm.getSex());
                else
                    userAfter.setSex("female");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            String roleUser = userDetailsForm.getRole();
            Set<String> authorities = getAuthorities();
            // check PERMISSION
            boolean valid = false;
            for (String authority : authorities) {
                if (authority.equals("ROLE_ADMIN")) {
                    if (roleUser.equals("user")) {
                        userAfter.setRole("USER");
                        responeHTML.addProperty("MESSAGE", elasticserService.editUser(userAfter));
                        break;
                    } else if (roleUser.equals("administrator")) {
                        userAfter.setRole("ADMIN");
                        responeHTML.addProperty("MESSAGE", elasticserService.editUser(userAfter));
                        break;
                    }
                    valid = true;
                } else if (authority.equals("ROLE_DBA")) {
                    if (roleUser.equals("administrator")) {
                        userAfter.setRole("ADMIN");
                        responeHTML.addProperty("MESSAGE", elasticserService.editUser(userAfter));
                        break;
                    } else if (roleUser.equals("dba")) {
                        userAfter.setRole("DBA");
                        responeHTML.addProperty("MESSAGE", elasticserService.editUser(userAfter));
                        break;
                    } else if (roleUser.equals("user")) {
                        userAfter.setRole("USER");
                        responeHTML.addProperty("MESSAGE", elasticserService.editUser(userAfter));
                        break;
                    }
                }
            }
            if (responeHTML.get("MESSAGE") == null) {
                responeHTML.addProperty("MESSAGE", "NOT_PERMISSION");
            }
        }else {
            responeHTML.addProperty("MESSAGE", "NOT_USER");
        }
        return responeHTML.toString();
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile partFile) {
        File file = null;
        JsonObject respone = new JsonObject();
        String responeString = null;
        try {
            WordType wordType = getWordType(partFile);
            String fileName = partFile.getOriginalFilename();
            System.out.println("File Name:" + fileName);

            // making directories for our required path.
            byte[] bytes = partFile.getBytes();
            File directory = new File(PATH_FILE);
            if (!directory.isDirectory())
                directory.mkdirs();
            // saving the file
            file = new File(directory.getAbsolutePath() + "\\" + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(bytes);
            stream.close();
            responeString = elasticserService.insertFromFile(file, wordType);
        } catch (Exception e) {
            e.printStackTrace();
            respone.addProperty("message", "ERROR");
            respone.addProperty("data", e.toString());
            return respone.toString();
        }
        return responeString;
    }

    @RequestMapping(value = "/uploadDoc", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public MultifileRespone uploadDoc(@RequestParam("files") MultipartFile[] multiPartFiles) {
        MultifileRespone multifileRespone = new MultifileRespone();

        for (MultipartFile partFile : multiPartFiles) {
            FileRespone fileRespone = new FileRespone();
            multifileRespone.addRespone(fileRespone);
            File file = null;
            String fileName = null;
            String outputName = null;
            try {
                WordType wordType = getWordType(partFile);
                fileName = partFile.getOriginalFilename();
                fileRespone.setFileName(fileName);
                if (wordType.equals(WordType.NONE)) {
                    fileRespone.setIgnore(true);
                    continue;
                }
                byte[] bytes = partFile.getBytes();
                /*
                 * saving the file format name with random 30 character Ex : thongtu.doc -> sheudnhcdhisderfdscvgtoehrjdnc.doc
                 */
                outputName = Ultils.formatFileName(fileName);
                System.out.println("after format:" + outputName);
                file = saveFileToLocal(bytes, outputName, fileRespone);
                if (file != null) {
                    // convert to html
                    boolean isConverted = convertFileToHTML(file, wordType);
                    fileRespone.setConverted(isConverted);
                    if (isConverted) {
                        // update database
                        fileRespone.setInsertedDatabase(updateDatabase(file, fileName, wordType));
                    }
                }
            } catch (IOException e) {
                fileRespone.setIgnore(true);
                System.out.println(e.toString());
            }
        }
        return multifileRespone;
    }

    private WordType getWordType(MultipartFile partFile) throws IOException {
        WordType wordType;
        String fileType = partFile.getContentType();
        if (fileType.equals("application/msword")) {
            wordType = WordType.DOC;
        } else if (fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            wordType = WordType.DOCX;
        } else {
            wordType = WordType.NONE;
        }
        return wordType;
    }

    private File saveFileToLocal(byte[] bytes, String outputName, FileRespone fileRespone) {
        File directory = new File(PATH_DOC);
        if (!directory.exists() || !directory.isDirectory())
            directory.mkdirs();

        File file = new File(directory.getAbsolutePath() + "\\" + outputName);
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            os.write(bytes);
            os.flush();
            os.close();
            fileRespone.setSaved(true);
        } catch (IOException e) {
            fileRespone.setSaved(false);
            System.out.println(e.getMessage());
            return null;
        }
        return file;
    }

    private boolean updateDatabase(File file, String fileName, WordType wordType) {
        String fileNameLocal = file.getName();
        Document document = new Document();
        document.setByUser(getCurrentUserName());
        document.setDateCreate(new Date().getTime());
        document.setIdFile(fileNameLocal.substring(0, fileNameLocal.lastIndexOf(".")));
        document.setTitle(fileName);
        document.setFileName(fileName);
        StringBuilder builder = new StringBuilder();
        String[] arrayParagrap = null;
        try {
            arrayParagrap = convertWordToHTML.getArrayParagrap(file, wordType);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (arrayParagrap != null) {
            for (String s : arrayParagrap) {
                builder.append(s);
            }
            document.setDocument(builder.toString());
            return elasticserService.insertDoc(document);
        } else {
            return false;
        }
    }

    private boolean convertFileToHTML(File file, WordType wordType) {
        String outputName = file.getName();
        byte[] byteArrayHtml = null;
        try {
            byteArrayHtml = convertWordToHTML.convertWordHTML(file, wordType);
            if (byteArrayHtml != null) {
                File directoryHTML = new File(PATH_HTML);
                if (!directoryHTML.isDirectory() || !directoryHTML.exists())
                    directoryHTML.mkdirs();

                // create file html with name is 30 character random.
                File fileHTML = new File(directoryHTML.getAbsolutePath() + "\\" + outputName.substring(0, outputName.lastIndexOf(".")) + ".html");
                if (!fileHTML.exists())
                    fileHTML.createNewFile();

                FileOutputStream out = new FileOutputStream(fileHTML);
                out.write(byteArrayHtml);
                out.flush();
                out.close();
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("can't convert Word to HTML");
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject getAll() {
        JsonObject result = new JsonObject();
        result.add("data", elasticserService.getAll());
        return result;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String createUser(@RequestBody UserDetailsForm userDetailsForm, HttpServletResponse response) {
        System.out.println(userDetailsForm.getFullName());
        Set<String> authorities = getAuthorities();
        String responeHTML = null;
        userDetailsForm.setDateCreate(new Date().getTime());
        for (String authority : authorities) {
            if (authority.equals("ROLE_ADMIN")) {
                if (userDetailsForm.getRole().equals("user")) {
                    userDetailsForm.setRole("USER");
                    responeHTML = elasticserService.createUser(userDetailsForm, getCurrentUserName());
                    break;
                } else if (userDetailsForm.getRole().equals("administrator")) {
                    userDetailsForm.setRole("ADMIN");
                    responeHTML = elasticserService.createUser(userDetailsForm, getCurrentUserName());
                    break;
                }
            } else if (authority.equals("ROLE_DBA")) {
                if (userDetailsForm.getRole().equals("administrator")) {
                    userDetailsForm.setRole("ADMIN");
                    responeHTML = elasticserService.createUser(userDetailsForm, getCurrentUserName());
                    break;
                } else if (userDetailsForm.getRole().equals("dba")) {
                    userDetailsForm.setRole("DBA");
                    responeHTML = elasticserService.createUser(userDetailsForm, getCurrentUserName());
                    break;
                } else {
                    userDetailsForm.setRole("USER");
                    responeHTML = elasticserService.createUser(userDetailsForm, getCurrentUserName());
                    break;
                }
            }
        }
        if (responeHTML == null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MESSAGE", "ERROR");
            jsonObject.addProperty("DATA", "NOT_PERMISSION");
            responeHTML = jsonObject.toString();
        }
        return responeHTML;
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject getAllUser() {
        Set<String> authorities = getAuthorities();
        Set<String> role = new HashSet<String>();
        JsonObject respone = new JsonObject();

        if (authorities.contains("ROLE_DBA")) {
            role.addAll(Arrays.asList("ALL"));
            respone.add("data", elasticserService.getAllUser(role.toString()));
            return respone;
        } else if (authorities.contains("ROLE_ADMIN")) {
            // role.addAll(Arrays.asList("ADMIN","USER","_all"));
            role.addAll(Arrays.asList("ADMIN"));
            respone.add("data", elasticserService.getAllUser(role.toString()));
            return respone;
        } else {
            respone.addProperty("data", "");
            return respone;
        }
    }

    private MyUserDetails getPrincipal() {
        MyUserDetails myUserDetails = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserDetails) {
            myUserDetails = ((MyUserDetails) principal);
        }
        /*
         * else { userName = principal.toString(); }
         */
        return myUserDetails;
    }

    private Set<String> getAuthorities() {
        Set<String> authoritiesTable = new HashSet<String>();
        MyUserDetails currentUser = getPrincipal();
        Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            authoritiesTable.add(authority.getAuthority());
        }
        return authoritiesTable;
    }

    private String getCurrentUserName() {
        return getPrincipal().getUsername();
    }

    private Set<String> getAuthCanDelete(String auth) {
        Set<String> authorities = new HashSet<String>();
        switch (auth) {
        case "ADMIN":
            authorities.add("USER");
            break;
        case "DBA":
            authorities.add("USER");
            authorities.add("ADMIN");
            break;
        case "USER":
            break;
        default:
            break;
        }
        return authorities;
    }
}
