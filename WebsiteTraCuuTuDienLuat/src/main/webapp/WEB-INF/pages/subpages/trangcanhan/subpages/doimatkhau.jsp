<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<table>
   <tbody>
      <tr>
         <td>Mật khẩu cũ</td>
         <td><input type="password" id="password"></td>
      </tr>
      <tr>
         <td>Mật khẩu mới</td>
         <td><input type="password" id="newPass"></td>
      </tr>
      <tr>
         <td>Nhập lại mật khẩu mới</td>
         <td><input type="password" id="newPassAgain"></td>
      </tr>
      <tr>
         <td colspan="2"><button type="button" class="btn btn-danger" id="btnChangePass">ĐỔI MẬT KHẨU</button></td>
      </tr>
   </tbody>
</table>