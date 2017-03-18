<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<span class="btn btn-success" id="btnAddWordPersonal">
   <i class="glyphicon glyphicon-plus"></i>
   <span>Thêm từ</span>
</span>

<span class="btn btn-success" id="btnRefreshWordTable">
   <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
   <span>Làm mới</span>
</span>
<jsp:include page="../../dialog-themtu.jsp">
   <jsp:param name="isNewWord" value="true"/>
</jsp:include>
<br>
<br>
<div class="table" id="table-tudiencanhan">
   <div class=thead>
      <p class="title-col">Tra gần đây</p>
      <p class="title-col">Cá nhân</p>
      <p class="title-col">Chi tiết</p>
   </div>
   <div class="tbody">
      <div class="tcol">
         <div id="history-content"></div>
      </div>
      <div class="tcol">
         <div id="word-personal-content">
            <div class="list-group"></div>
         </div>
      </div>
      <div class="tcol">
         <div id="tabsResultWordPersonal">
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
      </div>
   </div>
</div>