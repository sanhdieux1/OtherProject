package com.khoa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.khoa.entity.Document;
import com.khoa.service.ElasticsearchService;
import com.khoa.ultils.Ultils;

@Controller
public class EventControler {

    @Autowired
    public ElasticsearchService elasticserService;

    @Value("${PATH_DOC:directoryDocPath}")
    public String PATH_DOC;

    @Value("${PATH_IMAGES:directoryImagesPath}")
    public String PATH_IMAGES;

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String search(@RequestParam("keySearch") String keySearch, @RequestParam("isPersonal") String isPersonal) {
        System.out.println("search request:" + keySearch);
        return elasticserService.searchKeyWord(keySearch);
    }

    @RequestMapping(value = "/autocomplete", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String autoComplete(@RequestParam("keySearch") String keySearch) {
        return elasticserService.autoComplete(keySearch);
    }

    @RequestMapping(value = "/getAllDoc", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject getAllDoc() {
        JsonObject restul = new JsonObject();
        restul.add("data", elasticserService.getAllDoc());
        return restul;
    }

    @RequestMapping(value = "/download/{idDoc}", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @PathVariable("idDoc") String idDoc) throws IOException {

        File folderDoc = new File(PATH_DOC);
        File[] listFile = folderDoc.listFiles();
        File file = null;
        for (File fileItem : listFile) {
            String fileName = fileItem.getName();
            if ((fileName.substring(0, fileName.lastIndexOf("."))).equals(idDoc)) {
                file = fileItem;
                break;
            }
        }
        if(file == null){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
        if (!file.exists()) {
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
        String fileName = file.getName();
        Document document = elasticserService.getDocument(fileName.substring(0, fileName.lastIndexOf(".")));

        if (document == null) {
            return;
        }
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        System.out.println("mimetype : " + mimeType);

        response.setContentType(mimeType);

        /*
         * "Content-Disposition : inline" will show viewable types [like
         * images/text/pdf/anything viewable by browser] right on browser while
         * others(zip e.g) will be directly downloaded [may provide save as
         * popup, based on your browser setting.]
         */
        // response.setHeader("Content-Disposition", String.format("inline;
        // filename=\"" + file.getName() +"\""));

        /*
         * "Content-Disposition : attachment" will be directly download, may
         * provide save as popup, based on your browser setting
         */
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", document.getFileName()));

        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        // Copy bytes from source to destination(outputstream in this example),
        // closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @RequestMapping(value = "/uploadImages", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject uploadImages(@RequestParam("file") MultipartFile file) {
        JsonObject respone = new JsonObject();
        String linkFile = "images";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // Creating the directory to store file
                File dir = new File(PATH_IMAGES);
                if (!dir.exists() || !dir.isDirectory())
                    dir.mkdirs();

                String fileName = file.getOriginalFilename();
                // Create the file on server
                String fileNameFormated = Ultils.formatFileName(fileName);
                linkFile += "/" + fileNameFormated;
                File serverFile = new File(dir.getAbsolutePath() + File.separator + fileNameFormated);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                respone.addProperty("MESSAGE", linkFile);
            } catch (Exception e) {
                respone.addProperty("MESSAGE", "fail");
            }
        } else {
            respone.addProperty("MESSAGE", "fail");
        }
        return respone;
    }
    @RequestMapping(value = "/getURLWithContextPath", method = RequestMethod.GET)
    @ResponseBody
    public String getURLWithContextPath(HttpServletRequest request, HttpServletResponse response) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
     }
}
