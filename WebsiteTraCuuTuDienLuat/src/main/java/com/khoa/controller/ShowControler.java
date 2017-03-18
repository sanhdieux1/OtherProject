package com.khoa.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.khoa.entity.MyUserDetails;
import com.khoa.entity.UserDetailsForm;
import com.khoa.service.ElasticsearchService;

@Controller
public class ShowControler {

    @Autowired
    ElasticsearchService elasticsearchSearcch;

    @RequestMapping(value = { "/", "/home", "/index" }, method = RequestMethod.GET)
    public String home(@RequestParam(value = "page", required = false) String page, Locale locale, Model model) {
        UserDetailsForm currentUser = null;
        String currentUsername = getCurrentUserName();
        if (currentUsername != null) {
            if (currentUsername.equals("admin")) {
                currentUser = new UserDetailsForm();
                currentUser.setFullName("Adminitrator");
                currentUser.setRole("ADMIN");
                currentUser.setUsername("admin");
            } else {
                currentUser = elasticsearchSearcch.getUser(getCurrentUserName());
            }
        }
        model.addAttribute("page", page);
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            Set<String> authorities = getAuthorities();
            String role = "USER";
            if (authorities.contains("ROLE_ADMIN"))
                role = "ADMIN";
            if (authorities.contains("ROLE_DBA"))
                role = "DBA";
            model.addAttribute("role", role);
        }

        return "index";
    }

    @RequestMapping(value = "/tintuc", method = RequestMethod.GET)
    public String tinTuc(Locale locale, Model model) {
        return "tin-tuc";
    }

    /*
     * @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
     * public String homePage(ModelMap model) {
     * model.addAttribute("greeting", "Hi, Welcome to mysite. ");
     * return "welcome";
     * }
     */

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap model) {
        String currentUserName = getCurrentUserName();
        if (currentUserName != null) {
            model.addAttribute("username", currentUserName);

            Set<String> authorities = getAuthorities();
            String role = "USER";
            if (authorities.contains("ROLE_ADMIN"))
                role = "ADMIN";
            if (authorities.contains("ROLE_DBA"))
                role = "DBA";
            model.addAttribute("role", role);
        }

        return "admin";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getCurrentUserName());
        return "accessDenied";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    private MyUserDetails getPrincipal() {
        MyUserDetails myUserDetails = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserDetails) {
            myUserDetails = ((MyUserDetails) principal);
        }
        /*
         * else {
         * userName = principal.toString();
         * }
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
        MyUserDetails currentUser = getPrincipal();
        String username = null;
        if (currentUser != null) {
            username = currentUser.getUsername();
        }
        return username;
    }

}
