<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<div id="dialog-edit-word" title="<%if(request.getParameter("isNewWord").equals("true")){
            out.print("Thêm từ");
         }else{
             out.print("Sửa từ");
         }; %>">
   <p class="validateTipsEditWordDialog"></p>
   <form>
      <fieldset>
         <label for="name">Từ khóa</label>
         <input type="text" name="keySearch" id="keySearchEdit" <%if(!request.getParameter("isNewWord").equals("true")){
            out.print("disabled");
         }; %> >
         <label for="email">Mô tả</label>
         <textarea name="description" id="descriptionEdit" wrap="hard" rows="8" required>  </textarea>
         <div class="multimediaEdit multimedia">
            <div class="image-content fields-content">
               <label>Hình Ảnh</label>
               <input type="button" class="btn btn-primary btn-xs add_field_button" value="Thêm URL">
               <button type="button" class="btn btn-primary btn-xs input-image-btn">Tải lên</button>
               <div class="fields"></div>
            </div>
            <div class="audio-content fields-content">
               <label>Âm Thanh</label>
               <input type="button" class="btn btn-primary btn-xs add_field_button" value="Thêm URL">
               <button type="button" class="btn btn-primary btn-xs input-image-btn">Tải lên</button>
               <div class="fields"></div>
            </div>
            <div class="linked-content fields-content">
               <label for="password">Liên kết</label>
               <input type="button" class="btn btn-primary btn-xs add_field_button" value="Thêm URL">
               <div class="fields"></div>
            </div>
         </div>
         <!-- Allow form submission with keyboard without duplicating the dialog button -->
         <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
      </fieldset>
   </form>
</div>