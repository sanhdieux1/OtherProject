<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html dir="ltr" lang="en-US">
<head>
<!-- Created by Artisteer v4.3.0.60745 -->
<meta charset="utf-8">
<%@ page contentType="text/html; charset=UTF-8"%>
<title>Trang Chủ</title>
<meta name="viewport" content="initial-scale = 1.0, maximum-scale = 1.0, user-scalable = no, width = device-width">
<link rel="stylesheet" href="src/css/style.css">
<link rel="stylesheet" href="src/css/style.responsive.css">
<link rel="stylesheet" href="src/css/bootstrap.css">
<link rel="stylesheet" href="src/css/jquery-ui/jquery-ui.css" media="screen">
<link rel="stylesheet" href="src/css/magnific-popup.css" media="all">
<link rel="stylesheet" href="src/css/personal/style-index.css">
<link rel="stylesheet" href="src/css/personal/style-tintuc.css">
<link rel="stylesheet" href="src/css/personal/trangcanhan.css">
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
   <input type="hidden" id="urlPage" value='<c:url value="/"></c:url>'>
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
            <div id="content-session">
               <c:if test="${user != null}">
                  <p class="text-primary">
                     <strong>${user.fullName}</strong> | <a href="<c:url value="/logout" />">Đăng xuất</a>
                  </p>
                  <c:if test="${user.role=='USER'}">
                     <span class="gradientText">
                        <i>Thành Viên</i>
                     </span>
                  </c:if>
                  <c:if test="${user.role=='ADMIN'}">
                     <span class="gradientText">
                        <i>Administrator</i>
                     </span>
                     <a href="<c:url value="/admin" />"><p>Trang Quản Trị</p></a>
                  </c:if>
                  <c:if test="${user.role=='DBA'}">
                     <span class="gradientText">
                        <i>Database Administrator</i>
                     </span>
                     <br />
                     <br />
                     <a href="<c:url value="/admin" />"><p>Trang Quản Trị</p></a>
                  </c:if>
               </c:if>
            </div>
         </div>
         <nav class="art-nav">
            <ul class="art-hmenu">
               <li><a href="#timtu"
                  <c:choose>
                    <c:when
                        test="${page =='timtu'}">
                        class="active"
                    </c:when>
                    <c:when
                        test="${page ==null}">
                        class="active"
                    </c:when>
                    <c:otherwise>
                        
                    </c:otherwise>
                </c:choose>>Tra
                     Từ</a></li>
               <li><a href="#tintuc" <c:if test='${page=="tintuc"}'>class="active"</c:if>>Tin Tức</a></li>
               <c:if test='${user.role != null}'>
                  <li><a href="#trangcanhan" <c:if test='${page=="trangcanhan"}'>class="active"</c:if>>Trang Cá Nhân</a></li>
               </c:if>
            </ul>
         </nav>
         <div class="art-layout-wrapper">
         <div id="mainTips"></div>
            <div class="art-content-layout">
               <div class="art-content-layout-row">
                  <div class="art-layout-cell art-content">
                     <div class="art-block clearfix">
                        <div class="art-blockheader"></div>
                     </div>
                     <article class="art-post art-article">
                        <div class="art-postcontent art-postcontent-0 clearfix">
                           <div class="art-content-layout-wrapper layout-item-0">
                              <div class="art-content-layout">
                                 <div class="art-content-layout-row">
                                    <div class="art-layout-cell layout-item-1" style="width: 100%">
                                       <div class="clearfix">
                                          <div id="result">
                                             <div class="tab-content">
                                                <div id="timtu"
                                                   class='tab <c:if test='${page=="timtu"}'>active</c:if> <c:if test='${page==null}'>active</c:if>'>
                                                   <div class="art-blockcontent">
                                                      <div>
                                                         <form action="#" class="art-search" method="get" name="searchform" id="searchform">
                                                            <input id="keyComplete" type="text" value="" name="s" autocomplete="off" />
                                                            <input id="btnSearch" type="button" name="search" class="art-search-button" />
                                                         </form>
                                                         <div id="display">
                                                            <ul class="ulSuggest">
                                                            </ul>
                                                         </div>
                                                      </div>
                                                   </div>
                                                   <!--  -->
                                                   <h3>Kết Quả</h3>
                                                   <div id="show-content">
                                                      <div id="tabsResultSearch">
                                                         <ul>
                                                            <li><a href="#description">Mô Tả</a></li>
                                                            <li><a href="#image">Hình Ảnh</a></li>
                                                            <li><a href="#audio">Âm Thanh</a></li>
                                                            <li><a href="#document">Tài liệu</a></li>
                                                         </ul>
                                                         <div id="description"></div>
                                                         <div id="image"></div>
                                                         <div id="audio"></div>
                                                         <div id="document"></div>
                                                      </div>
                                                      <div id="listResultSearch">
                                                         <ul>
                                                         </ul>
                                                      </div>
                                                   </div>
                                                </div>
                                                <div id="tintuc" class='tab <c:if test='${page=="tintuc"}'>active</c:if>'>
                                                   <jsp:include page="subpages/tintuc.jsp" />
                                                </div>
                                                <div id="trangcanhan" class="tab <c:if test='${page=="trangcanhan"}'>active</c:if>">
                                                   <c:if test='${user !=null}'><jsp:include page="subpages/trangcanhan/main.jsp" /></c:if>
                                                </div>
                                             </div>
                                          </div>
                                       </div>
                                    </div>
                                 </div>
                              </div>
                           </div>
                        </div>
                     </article>
                  </div>
                  <div class="art-layout-cell art-sidebar1">
                     <div class="art-block clearfix">
                        <div class="art-blockheader"></div>
                        <div class="art-blockcontent">
                           <c:if test="${user == null}">
                              <form action="${loginUrl}" method="post" id="form-login" modelAttribute="loginBean">
                                 <fieldset class="input" style="border: 0 none;">
                                    <c:if test="${param.error != null}">
                                       <div class="alert alert-danger">
                                          <p>Tài khoản hoặc mật khẩu không đúng!</p>
                                       </div>
                                    </c:if>
                                    <c:if test="${param.logout != null}">
                                       <div class="alert alert-success">
                                          <p>Đăng xuất thành công!</p>
                                       </div>
                                    </c:if>
                                    <p id="form-login-username">
                                       <label for="modlgn_username">Đăng nhập</label> <br />
                                       <input id="modlgn_username" type="text" name="ssoId" placeholder="Tài khoản" required class="inputbox"
                                          alt="username" style="width: 100%" />
                                    </p>
                                    <p id="form-login-password">
                                       <input id="modlgn_passwd" type="password" name="password" placeholder="Mật khẩu" required class="inputbox"
                                          size="18" alt="password" style="width: 100%" />
                                    </p>
                                    <p id="form-login-remember">
                                       <label class="art-checkbox"> <input type="checkbox" id="modlgn_remember" name="remember" value="yes"
                                             alt="Remember Me" />Nhớ tài khoản
                                       </label>
                                    </p>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <input type="submit" value="Đăng Nhập" name="Submit" class="art-button" />
                                 </fieldset>
                                 <ul>
                                    <li><a href="#">Bạn quên mật khẩu?</a></li>
                                    <li><a href="#">Tạo tài khoản</a></li>
                                 </ul>
                              </form>
                           </c:if>
                        </div>
                     </div>
                     <!--  -->
                     <c:if test='${user.role != null}'>
                        <div class="art-vmenublock clearfix <c:if test='${page == "trangcanhan"}'>active</c:if>">
                           <div class="art-vmenublockheader">
                              <h3 class="t">Danh Mục</h3>
                           </div>
                           <div class="art-vmenublockcontent">
                              <ul class="art-vmenu">
                                 <li><a href="#thongtintaikhoan" class="active">Thông tin tài khoản</a></li>
                                 <li><a href="#tudiencanhan" class="">Từ điển cá nhân</a></li>
                                 <li><a href="#doimatkhau" class="">Đỗi mật khẩu</a></li>
                              </ul>
                           </div>
                        </div>
                     </c:if>
                     <!--  -->
                  </div>
               </div>
            </div>
         </div>
         <footer class="art-footer"> 
            <input type="file" id="file-provisional" accept="image/*">
         </footer>
      </div>
      <p class="art-page-footer"></p>
   </div>
   <script src="src/js/jquery-1.9.0.js"></script>
   <script src="src/js/script.js"></script>
   <script src="src/js/script.responsive.js"></script>
   <script src="src/js/magnific-popup.js"></script>
   <script src="src/js/jquery-ui/jquery-ui.js"></script>
   <script src="src/js/personal/person-index.js"></script>
   <script src="src/js/personal/person-tintuc.js"></script>
   <script src="src/js/personal/person-trangcanhan.js"></script>
   <script src="src/js/personal/tintuc-content.js"></script>
   <script src="src/js/personal/uploadMultimedia.js"></script>
   <script type="text/javascript" src="src/js/personal/dialog-themtu.js"></script>
</body>
</html>