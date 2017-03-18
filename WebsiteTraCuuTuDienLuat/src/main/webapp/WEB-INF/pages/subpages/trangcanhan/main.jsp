<meta charset="utf-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<div class="staticAttribute" style="display: hidden;">
	<div id="usernameAttribute" data-prodnumber="${user.username}" />
</div>
<div class="tab-content-person">
   <div class="tab active" id="thongtintaikhoan">
      <jsp:include page="subpages/header.jsp">
         <jsp:param name="titleNumber" value="0" />
      </jsp:include>
      <jsp:include page="subpages/thongtintaikhoan.jsp"/>
   </div>
   <div class="tab" id="tudiencanhan">
      <jsp:include page="subpages/header.jsp">
         <jsp:param name="titleNumber" value="1" />
      </jsp:include>
      <jsp:include page="subpages/tudiencanhan.jsp"/>
   </div>
   <div class="tab" id="doimatkhau">
       <jsp:include page="subpages/header.jsp">
         <jsp:param name="titleNumber" value="2" />
      </jsp:include>
      <jsp:include page="subpages/doimatkhau.jsp"/>
   </div>
</div>