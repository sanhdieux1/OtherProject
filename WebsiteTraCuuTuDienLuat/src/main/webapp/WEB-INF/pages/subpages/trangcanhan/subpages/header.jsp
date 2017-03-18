<meta charset="utf-8">
<%@ page contentType="text/html; charset=UTF-8"%>
<link rel="stylesheet" href="src/css/personal/subpages/header.css">
<div class="header">
   <div id="divTitleUc">
      <div class="icon_tk">
         <div class="h_tit">
            <span>Tài khoản</span>
            <p>
               <%
                   switch (request.getParameter("titleNumber")) {
                   case "0":
                       out.println("Thông tin tài khoản");
                       break;
                   case "1":
                       out.println("Từ điển cá nhân");
                       break;
                   case "2":
                       out.println("Đỗi mật khẩu");
                       break;
                   default:
                   }
               %>
            </p>
         </div>
      </div>
   </div>
</div>