<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.khoa.entity.UserDetailsForm"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<fieldset>
   <table class="tableUserDetails">
      <tr>
         <td>Tên đăng Nhập</td>
         <td>:</td>
         <td><span>${user.username}</span></td>
      </tr>
      <tr>
         <td>Ngày đăng ký</td>
         <td>:</td>
         <td><jsp:useBean id="dateCreate" class="java.util.Date" /> <jsp:setProperty name="dateCreate" property="time"
               value="${user.dateCreate}" /> <span><fmt:formatDate type="date" value="${dateCreate}" /></span></td>
      </tr>
      <tr>
         <td>Loại tài khoản</td>
         <td>:</td>
         <td><span class="gradientText"><i> 
         <%
           UserDetailsForm currentUser = (UserDetailsForm) request.getAttribute("user");
           switch (currentUser.getRole()) {
           case "ADMIN":
               out.println("Administrator");
               break;
           case "USER":
               out.println("Member");
               break;
           case "DBA":
               out.println("DBA");
               break;
           default:
           }
           %>
            </i></span></td>
      </tr>
   </table>
</fieldset>
<br>
<br>
<p id="validateTips"></p>
<form id="informationForm" action="#">
   <fieldset>
      <table class="tableUserDetails">
         <tr>
            <td>Họ và tên</td>
            <td>:</td>
            <td><input type="text" id="fullNameEdit" value="${user.fullName}"></td>
         </tr>
         <tr>
            <td>Giới tính</td>
            <td>:</td>
            <td><c:if test="${user.sex=='male'}">
                  <label><input type="radio" id="sex" name="sex" value="male" checked="checked">Nam</label>
                  <label><input type="radio" id="sex" name="sex" value="female">Nữ</label>
               </c:if> <c:if test="${user.sex=='female'}">
                  <label><input type="radio" id="sex" name="sex" value="male">Nam</label>
                  <label><input type="radio" id="sex" name="sex" value="female" checked="checked">Nữ</label>
               </c:if> <c:if test="${user.sex==null}">
                  <label><input type="radio" id="sex" name="sex" value="male" checked="checked">Nam</label>
                  <label><input type="radio" id="sex" name="sex" value="female">Nữ</label>
               </c:if></td>
         </tr>
         <tr>
            <td>Ngày sinh</td>
            <td>:</td>
            <td><jsp:useBean id="birthDay" class="java.util.Date" /> <jsp:setProperty name="birthDay" property="time" value="${user.birthDay}" />
               <input type="hidden" id="hiddenDaySelect" value='<fmt:formatDate type="date" pattern="d" value="${birthDay}" />'> <input
                  type="hidden" id="hiddenMonthSelect" value='<fmt:formatDate type="date" pattern="M" value="${birthDay}" />'> <input
                  type="hidden" id="hiddenYearSelect" value='<fmt:formatDate type="date" pattern="yyyy" value="${birthDay}" />'> <select
               class="daySelect">
            </select> <select class="monthSelect">
                  <option value="1" selected="selected">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                  <option value="6">6</option>
                  <option value="7">7</option>
                  <option value="8">8</option>
                  <option value="9">9</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
            </select> <select class="yearSelect">
            </select></td>
         </tr>
         <tr>
            <td>Email</td>
            <td>:</td>
            <td><input id="email" type='text' value='${user.email}'></td>
         </tr>
         <tr>
            <td>Địa chỉ</td>
            <td>:</td>
            <td><input id="address" type='text' value='${user.address}'></td>
         </tr>
         <tr>
            <td>Số điện thoại</td>
            <td>:</td>
            <td><input id="phoneNumber" type='text' value='${user.phoneNumber}'></td>
         </tr>
      </table>
   </fieldset>
   <br> <br>
   <input type="submit" value="CẬP NHẬT" class="btn btn-primary">
</form>