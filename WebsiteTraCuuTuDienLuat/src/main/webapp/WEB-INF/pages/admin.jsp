<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html dir="ltr" lang="en-US">
<head>
<!-- Created by Artisteer v4.3.0.60745 -->
<meta charset="utf-8">
<%@ page contentType="text/html; charset=UTF-8"%>
<title>Trang Quản Lý</title>
<meta name="viewport" content="initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no, width = device-width">
<!-- style templete -->
<link rel="stylesheet" href="src/css/style.responsive.css" media="all">
<link rel="stylesheet" href="src/css/style.css" media="screen">
<link rel="stylesheet" href="src/css/jquery-ui.css" media="all">
<!-- datatables -->
<link rel="stylesheet" href="src/css/datatables/jquery.dataTables.css">
<link rel="stylesheet" href="src/css/datatables/jquery.dataTables.css">
<link rel="stylesheet" href="src/css/datatables/buttons.dataTables.css">
<!-- Bootstrap -->
<link rel="stylesheet" href="src/css/bootstrap/bootstrap.css">
<!-- Magnific Popup -->
<link rel="stylesheet" href="src/css/magnific-popup.css">
<!-- style person -->
<link rel="stylesheet" href="src/css/personal/style-admin.css" media="all">
<link rel="stylesheet" href="src/css/personal/uploadfile.css" media="all">
<link rel="stylesheet" href="src/css/personal/ultils.css" media="all">
<style>
.art-content .art-postcontent-0 .layout-item-0 {
   margin-bottom: 10px;
}

.art-content .art-postcontent-0 .layout-item-1 {
   padding-right: 10px;
   padding-left: 10px;
}

.ie7 .art-post .art-layout-cell {
   border: none !important;
   padding: 0 !important;
}

.ie6 .art-post .art-layout-cell {
   border: none !important;
   padding: 0 !important;
}
</style>
</head>
<body>
   <div id="art-main">
      <div class="art-sheet clearfix">
         <header class="art-header">
            <div class="art-shapes">
               <div class="art-textblock art-object1757191460">
                  <div class="art-object1757191460-text-container">
                     <div class="art-object1757191460-text">
                        <p
                           style="color: #3A2E17; font-size: 29px; font-family: Arvo, Arial, 'Arial Unicode MS', Helvetica, Sans-Serif; font-weight: bold; font-style: normal; text-decoration: none; letter-spacing: 2px; text-transform: uppercase">WEBSITE
                           TRA CỨU TỪ ĐIỂN CHUYÊN NGÀNH LUẬT</p>
                        <p
                           style="color: #282010; font-size: 11px; font-family: 'PT Sans', Arial, 'Arial Unicode MS', Helvetica, Sans-Serif; text-decoration: none; letter-spacing: 1px; text-transform: uppercase">LUẬT
                           CỦA MỌI NHÀ</p>
                     </div>
                  </div>
               </div>
            </div>
         </header>
         <div>
            <c:if test="${username != null}">
               <p>
                  <b>${username}</b> | <a href="logout">Logout</a>
               </p>
               <p>
                  <b>${role}</b>
               </p>
               <a href="${pageContext.request.contextPath}/">Trang Chủ</a>
            </c:if>
            <div id="validateTip" class></div>
         </div>
         <div class="art-layout-wrapper">
            <div class="art-content-layout">
               <div class="art-content-layout-row">
                  <div class="art-layout-cell art-sidebar1">
                     <div class="art-vmenublock clearfix">
                        <div class="art-vmenublockheader">
                           <h3 class="t">Danh Mục</h3>
                        </div>
                        <div class="art-vmenublockcontent">
                           <ul class="art-vmenu">
                              <li><a href="#themtudon" class="active">Thêm từ đơn</a></li>
                              <li><a href="#themtufile" class="">Thêm từ file</a></li>
                              <li><a href="#danhsachtudien" class="">Danh sách từ điển</a></li>
                              <li><a href="#danhsachnguoidung" class="">Danh sách người dùng</a></li>
                              <li><a href="#taotaikhoan" class="">Tạo tài khoản</a></li>
                              <li><a href="#quanlydocument" class="">Quản lý văn bản</a></li>
                           </ul>
                        </div>
                     </div>
                  </div>
                  <div class="art-layout-cell art-content">
                     <article class="art-post art-article">
                        <!-- body fix -->
                        <div class="tab-content">
                           <div id="themtudon" class="tab active">
                              <form modelAttribute="formData" id="formData">
                                 <table class=tableTuDon>
                                    <tr>
                                       <td>Từ khóa:</td>
                                       <td><input name="keySearch" id="keySearch" required></td>
                                    </tr>
                                    <tr>
                                       <td>Mô tả:</td>
                                       <td><textarea name="description" id="description" wrap="hard" rows="3" required></textarea></td>
                                    </tr>
                                 </table>
                                 <div class="multimediaForm multimedia">
                                    <div class="fields-content image-content" align="center">
                                       <h2>Hình Ảnh</h2>
                                       <button type="button" class="btn btn-primary btn-xs add_field_button">Thêm URL</button>
                                       <button type="button" class="btn btn-primary btn-xs input-image-btn">Tải lên</button>
                                       <div class="fields"></div>
                                    </div>
                                    <div class="fields-content audio-content" align="center">
                                       <h2>Âm Thanh</h2>
                                       <button type="button" class="btn btn-primary btn-xs add_field_button">Thêm URL</button>
                                       <button type="button" class="btn btn-primary btn-xs input-image-btn">Tải lên</button>
                                       <div class="fields"></div>
                                    </div>
                                    <div class="fields-content linked-content" align="center">
                                       <h2>Liên Kết</h2>
                                       <button type="button" class="btn btn-primary btn-xs add_field_button">Thêm URL</button>
                                       <div class="fields"></div>
                                    </div>
                                 </div>
                                 <br>
                                 <input type="button" value="Hoàn tất" id="btnInsert" class="btn btn-info">
                              </form>
                           </div>
                           <div id="themtufile" class="tab">
                              <form action="#">
                                 <span class="btn btn-success" id="btnSelectInputFile" style="width: 150px"> <i
                                    class="glyphicon glyphicon-plus"></i> <span>Chọn file</span>
                                 </span>
                                 <input id='fileImportWord' type="file" name="file"
                                    accept="application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document">
                                 <button class="btn btn-primary" disabled="disabled">
                                    <i class="glyphicon glyphicon-upload"></i> <span>Tải lên</span>
                                 </button>
                                 <br> <label></label>
                              </form>
                              <br> <br>
                              <div id="show-content"></div>
                           </div>
                           <div id="quanlydocument" class="tab">
                              <form action="" id="formFileDoc">
                                 <br> <br> <span class="btn btn-success" id="fileinput-button" style="width: 150px"> <i
                                    class="glyphicon glyphicon-plus"></i> <span>Thêm files...</span>
                                 </span>
                                 <input type="file" multiple name="files[]" id="fileInputSelector">
                                 <button class="btn btn-primary" disabled="disabled" id="btnSubmitDoc">
                                    <i class="glyphicon glyphicon-upload"></i> <span>Tải lên</span>
                                 </button>
                                 <br>
                                 <table id="tableShowFilesName">
                                    <tbody></tbody>
                                 </table>
                              </form>
                              <br>
                              <div>
                                 <table id="tableResultUploadFile" class="hide">
                                    <thead>
                                       <tr>
                                          <th>Tên file</th>
                                          <th>Kết quả</th>
                                       </tr>
                                    </thead>
                                    <tbody></tbody>
                                    <tfoot>
                                       <tr>
                                          <th>Tên file</th>
                                          <th>Kết quả</th>
                                       </tr>
                                    </tfoot>
                                 </table>
                              </div>
                              <div>
                                 <div>
                                    <input id="getAllDoc" type="button" value="Lấy Danh Sách">
                                 </div>
                                 <div id="resultDoc">
                                    <table id="tableResultDoc" class="my-datatable hide" cellspacing="0" width="100%">
                                       <thead>
                                          <tr>
                                             <th>Sửa</th>
                                             <th>Xóa</th>
                                             <th>Tiêu đề</th>
                                             <th>Ngày tạo</th>
                                             <th>Người tạo</th>
                                             <th>ID</th>
                                          </tr>
                                       </thead>
                                       <tfoot>
                                          <tr>
                                             <th>Edit</th>
                                             <th>Delete</th>
                                             <th>Title</th>
                                             <th>Date Create</th>
                                             <th>User Created:</th>
                                             <th>ID</th>
                                          </tr>
                                       </tfoot>
                                       <tbody>
                                       </tbody>
                                    </table>
                                 </div>
                              </div>
                              <div id="dialog-edit-doc" title="Thay đổi tiêu đề">
                                 <form>
                                    <input type="text" id="titleEditDoc" name="title" placeholder="Nhập tiêu đề">
                                    <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
                                 </form>
                              </div>
                           </div>
                           <div id="danhsachtudien" class="tab">
                           <button type="button" class="btn btn-primary btn-xs" id="refreshListWord">Làm mới</button>
                              <table id="tableResultWord" class="my-datatable" cellspacing="0" width="100%">
                                 <thead>
                                    <tr>
                                       <th>Từ Khóa</th>
                                       <th>Ngày tạo</th>
                                       <th>Người tạo</th>
                                       <th></th>
                                       <th></th>
                                       <th></th>
                                       <th>ID</th>
                                       <th>Mô Tả</th>
                                       <th>previous</th>
                                       <th>multimedia</th>
                                    </tr>
                                 </thead>
                                 <tfoot>
                                    <tr>
                                       <th>Từ Khóa</th>
                                       <th>Date Create</th>
                                       <th>User Created:</th>
                                       <th></th>
                                       <th></th>
                                       <th></th>
                                       <th>ID</th>
                                       <th>Mô Tả</th>
                                       <th>previous</th>
                                       <th>multimedia</th>
                                    </tr>
                                 </tfoot>
                              </table>
                              <jsp:include page="subpages/dialog-themtu.jsp">
                                 <jsp:param name="isNewWord" value="false"/>
                              </jsp:include>
                           </div>
                           <div id="danhsachnguoidung" class="tab">
                              <div class="title"></div>
                              <button type="button" class="btn btn-primary btn-xs" id="refreshListUser">Làm mới</button>
                              <div id='showContentUser'>
                                 <table id="tableUser" class="my-datatable" cellspacing="0" width="100%">
                                    <thead>
                                       <th></th>
                                       <th></th>
                                       <th></th>
                                       <th>Tài khoản</th>
                                       <th>Họ và tên</th>
                                       <th>Quyền hạn</th>
                                       <th>Địa chỉ</th>
                                       <th>Số điện thoại</th>
                                       <th>Email</th>
                                       <th>Người tạo</th>
                                       <th>Ngày tạo</th>
                                    </thead>
                                    <tfoot>
                                       <th></th>
                                       <th></th>
                                       <th></th>
                                       <th>Tài khoản</th>
                                       <th>Họ và tên</th>
                                       <th>Quyền hạn</th>
                                       <th>Địa chỉ</th>
                                       <th>Số điện thoại</th>
                                       <th>Email</th>
                                       <th>Người tạo</th>
                                       <th>Ngày tạo</th>
                                    </tfoot>
                                    <tbody></tbody>
                                 </table>
                              </div>
                              <div id="dialog-edit-user" title="Sửa thông tin người dùng">
                                 <p class="validateTipsEditUserDialog"></p>
                                 <form>
                                    <fieldset>
                                       <legend>Thông tin tài khoản:</legend>
                                       <table class='table-dialog-edit-user'>
                                          <tbody>
                                             <tr>
                                                <td><label>Tên Đăng Nhập</label></td>
                                                <td>:</td>
                                                <td><b><label name="username" id="usernameEdit"></label></b></td>
                                             </tr>
                                             <tr>
                                                <td><label>Ngày Tạo</label></td>
                                                <td>:</td>
                                                <td><input type="date" id='dataCreateEdit' readonly style="background-color: #dddddd"></td>
                                             </tr>
                                             <tr>
                                                <td><label>Quyền Truy Cập</label></td>
                                                <td>:</td>
                                                <td><select name="role" id=roleEdit>
                                                      <option value="administrator">Administrator</option>
                                                      <option value="user">User Normal</option>
                                                </select></td>
                                             </tr>
                                          </tbody>
                                       </table>
                                    </fieldset>
                                    <fieldset>
                                       <legend>Thông tin cá nhân:</legend>
                                       <table class='table-dialog-edit-user'>
                                          <tbody>
                                             <tr>
                                                <td><label>Họ Và Tên</label></td>
                                                <td>:</td>
                                                <td><input name="fullName" id="fullNameEdit" required></td>
                                             </tr>
                                             <tr>
                                                <td><label>Địa Chỉ</label></td>
                                                <td>:</td>
                                                <td><input name="address" id="addressEdit" required></td>
                                             </tr>
                                             <tr>
                                                <td><label>Ngày Sinh</label></td>
                                                <td>:</td>
                                                <td><input name="birthDay" id="birthDayEdit" type="date" required></td>
                                             </tr>
                                             <tr>
                                                <td><label>Số Điện Thoại</label></td>
                                                <td>:</td>
                                                <td><input name="phoneNumber" id="phoneNumberEdit" required></td>
                                             </tr>
                                             <tr>
                                                <td><label>Email</label></td>
                                                <td>:</td>
                                                <td><input name="email" id="emailEdit" required></td>
                                             </tr>
                                          </tbody>
                                       </table>
                                    </fieldset>
                                    <!-- Allow form submission with keyboard without duplicating the dialog button -->
                                    <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
                                 </form>
                              </div>
                           </div>
                           <div id="taotaikhoan" class="tab">
                              <form id='signUpForm' modelAttribute="myUserDetails">
                                 <table id="tableCreateUser" class="my-datatable">
                                    <tr>
                                       <td>Tên đăng Nhập*</td>
                                       <td>:</td>
                                       <td><input class='inputSignUp' id="username" type='text'></td>
                                    </tr>
                                    <tr>
                                       <td>Mật khẩu*</td>
                                       <td>:</td>
                                       <td><input class='inputSignUp' id="password" type='password'></td>
                                    </tr>
                                    <tr>
                                       <td>Nhập lại mật khẩu*</td>
                                       <td>:</td>
                                       <td><input class='inputSignUp' id="confirmPass" name="confirmPass" type='password'></td>
                                    </tr>
                                    <tr>
                                       <td>Loại tài khoản</td>
                                       <td>:</td>
                                       <td><select id='role'>
                                             <c:choose>
                                                <c:when test="${role == 'DBA'}">
                                                   <option value="administrator">Administrator</option>
                                                   <option value="user" selected="selected">User Normal</option>
                                                </c:when>
                                                <c:when test="${role == 'ADMIN'}">
                                                   <option value="administrator">Administrator</option>
                                                   <option value="user" selected="selected">User Normal</option>
                                                </c:when>
                                                <c:when test="${role == 'USER'}">
                                                   <option value="administrator" disabled>Administrator</option>
                                                   <option value="user" selected="selected">User Normal</option>
                                                </c:when>
                                                <c:otherwise>
                                                </c:otherwise>
                                             </c:choose>
                                       </select></td>
                                    </tr>
                                    <tr>
                                       <td>Email</td>
                                       <td>:</td>
                                       <td><input id="email" type='text'></td>
                                    </tr>
                                    <tr>
                                       <td>Họ và Tên*</td>
                                       <td>:</td>
                                       <td><input class='inputSignUp' id="fullName" type='text'></td>
                                    </tr>
                                    <tr>
                                       <td>Giới tính</td>
                                       <td>:</td>
                                       <td><label> <input type="radio" name="sex" value="male">Nam
                                       </label> <label> <input type="radio" name="sex" value="female" checked="checked">Nữ
                                       </label></td>
                                    </tr>
                                    <tr>
                                       <td>Ngày Sinh</td>
                                       <td>:</td>
                                       <td><input type="date" id='birthDay'></td>
                                    </tr>
                                    <tr>
                                       <td>Địa chỉ</td>
                                       <td>:</td>
                                       <td><input id="address" type='text'></td>
                                    </tr>
                                    <tr>
                                       <td>Số điện thoại</td>
                                       <td>:</td>
                                       <td><input id="phoneNumber" type='text'></td>
                                    </tr>
                                 </table>
                                 <input type="submit" value="Tạo">
                              </form>
                           </div>
                        </div>
                     </article>
                  </div>
               </div>
            </div>
         </div>
         <footer class="art-footer"> </footer>
      </div>
      <p class="art-page-footer">
         <input type="file" id="file-provisional" accept="image/*">
         <input type="hidden" value="${requestScope['javax.servlet.forward.request_uri']}">
         <input type="hidden" value="${pageContext.request.requestURL}">
         <input type="hidden" value="${pageContext.request.contextPath}" id="homeUrl">
      </p>
   </div>
   <!-- JQuery -->
   <script src="src/js/jquery-1.11.3.js"></script>
   <!-- DataTables -->
   <script src="src/js/datatables/jquery.dataTables.js"></script>
   <!-- Script templete -->
   <script src="src/js/script.js"></script>
   <!-- Bootstrap -->
   <script src="src/js/bootstrap/bootstrap.js"></script>
   <script src="src/js/script.responsive.js"></script>
   <!-- JQuery UI -->
   <script src="src/js/jquery-ui/jquery-ui.js"></script>
   <!-- Magnific popup -->
   <script src="src/js/magnific-popup.js"></script>
   <!-- Script preson -->
   <script type="text/javascript" src="src/js/personal/ultils.js"></script>
   <script type="text/javascript" src="src/js/personal/person-admin.js"></script>
   <script type="text/javascript" src="src/js/personal/uploadfile.js"></script>
   <script type="text/javascript" src="src/js/personal/uploadMultimedia.js"></script>
   <script type="text/javascript" src="src/js/personal/dialog-themtu.js"></script>
   
</body>
</html>