package com.khoa.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.khoa.entity.FormData;
import com.khoa.entity.Multimedia;
import com.khoa.entity.MyUserDetails;
import com.khoa.entity.UserDetailsForm;
import com.khoa.entity.WordDelail;
import com.khoa.service.ElasticsearchService;

@Controller
@RequestMapping("/user")
public class UserControler {

    @Autowired
    public ElasticsearchService elasticserService;

    @RequestMapping(value = "/editUser", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String editUser(@RequestBody UserDetailsForm userDetailsForm) {
        String currentUserName = getCurrentUserName();
        JsonObject jsonRespone = new JsonObject();
        try {
            if (currentUserName.equals(userDetailsForm.getUsername())) {
                UserDetailsForm userAfter = elasticserService.getUser(userDetailsForm.getUsername());
                userAfter.setFullName(userDetailsForm.getFullName());
                userAfter.setSex(userDetailsForm.getSex());
                userAfter.setBirthDay(userDetailsForm.getBirthDay());
                userAfter.setEmail(userDetailsForm.getEmail());
                userAfter.setLastUpdate(new Date().getTime());
                userAfter.setAddress(userDetailsForm.getAddress());
                userAfter.setPhoneNumber(userDetailsForm.getPhoneNumber());
                jsonRespone.addProperty("MESSAGE", elasticserService.editUser(userAfter));
            } else {
                jsonRespone.addProperty("MESSAGE", "NOT_VAILDATE");
            }
        } catch (NullPointerException e) {
            jsonRespone.addProperty("MESSAGE", "NOT_VAILDATE");
        }
        return jsonRespone.toString();
    }

    @RequestMapping(value = "/insertWordPersonal", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public String insertWordPersonal(@RequestBody FormData formData) {
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

        String jsonResponse = "{\"message\":\"" + elasticserService.indexWordPersonal(wordDelail) + "\"}";
        return jsonResponse;
    }

    @RequestMapping(value = "/deleteWordPersonal", method = RequestMethod.GET)
    @ResponseBody
    public boolean deleteUser(@RequestParam("idWordPersonal") String idWordPersonal) {
        boolean kq = elasticserService.deleteWordPersonal(idWordPersonal, getCurrentUserName());
        return kq;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject getAll() {
        JsonObject result = new JsonObject();
        result.add("data", elasticserService.getAll(getCurrentUserName()));
        return result;
    }

    @RequestMapping(value = "/editpass", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public JsonObject editPass(@RequestParam("password") String password, @RequestParam("newPass") String newPass) {
        JsonObject jsonObject = new JsonObject();
        MyUserDetails user = getPrincipal();
        if (user.getPassword().equals(password)) {
            UserDetailsForm userDetailsForm = elasticserService.getUser(getCurrentUserName());
            userDetailsForm.setPassword(newPass);
            String respone = elasticserService.editUser(userDetailsForm);
            jsonObject.addProperty("MESSAGE", respone);
        } else {
            jsonObject.addProperty("MESSAGE", "WRONG_PASSWORD");
        }
        return jsonObject;
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

    /*
     * private Set<String> getAuthorities(){ Set<String> authoritiesTable = new HashSet<String>(); MyUserDetails currentUser = getPrincipal(); Collection<? extends
     * GrantedAuthority> authorities= currentUser.getAuthorities(); for(GrantedAuthority authority : authorities){ authoritiesTable.add(authority.getAuthority()); }
     * return authoritiesTable; }
     */
    private String getCurrentUserName() {
        MyUserDetails currentUser = getPrincipal();
        if (currentUser != null)
            return currentUser.getUsername();
        else
            return null;
    }

}
