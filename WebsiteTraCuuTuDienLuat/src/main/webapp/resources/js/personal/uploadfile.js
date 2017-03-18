/** upload doc * */

$(function submitFileDoc() {
   var fileDoc = $([]);
   var formDataDoc;
   var tableShowFilesName = $("#tableShowFilesName");
   var tableResultUploadFile = $("#tableResultUploadFile");
   var dataTableResultUploadFile= null;
   var tbodyResultUploadFile = tableResultUploadFile.find("tbody");
   var tbodyShowFilesName = tableShowFilesName.find("tbody");
   var fileInputSelector = $("#fileInputSelector");
   $("#fileinput-button").click(function() {
       fileInputSelector.trigger('click');
   });

   addListenerSelectFile();

   $("#btnSubmitDoc").click(function() {
      var responeData = processFileUpload();
      handlingResponeData(responeData);
   });

   $("#formFileDoc").submit(function(e) {
      e.preventDefault();
   });
   function processFileUpload() {
      var responeData = null;
      var encoder = new TextEncoder();
      $.ajax({
         url : 'admin/uploadDoc',
         data : formDataDoc,
         processData : false,
         contentType : false,// convert sang string
         dataType : "json",
         async : false, // dong bo. Hoan tat ajax truoc khi tiep tuc.
         type : 'POST',
         success : function(respone) {
            responeData = respone;
         },
         error : function(err) {
            updateTips("Upload lỗi", "", tip);
         }
      });
      return responeData;
   }

   function refreshButton(){
       if (fileDoc.length > 0) {
           $("#btnSubmitDoc").prop("disabled", false);
        } else {
           $("#btnSubmitDoc").attr("disabled", "disabled");
        }
   }
   
   function addListenerSelectFile(){
       fileInputSelector.on('change', function(event) {
           event.preventDefault();
           fileDoc = event.target.files;
           var ints = fileDoc.length;
           tbodyShowFilesName.html('');
           formDataDoc = new FormData();
           for (var i = 0; i < ints; i++) {
              formDataDoc.append("files", fileDoc[i]);
              var tr = $("<tr>");
              tr.append('<td>' + fileDoc[i].name + "</td>");
              tbodyShowFilesName.append(tr);
           }
           // enable btn upload
           refreshButton();
        });
   }
   
   function refreshFileInputSelector(){
       fileInputSelector.replaceWith('<input type="file" multiple name="files[]" id="fileInputSelector" />');
       fileInputSelector = $("#fileInputSelector");
       addListenerSelectFile();
   }
   
   function refreshFileInput() {
      fileDoc = $([]);
      tbodyShowFilesName.html('');
      refreshFileInputSelector();
      refreshButton();
   }

   function handlingResponeData(responeData) {
      console.log(responeData);
      if (responeData != null) {
         var arrayRespone = responeData.multifileRespone;
         var length = arrayRespone.length;
         tbodyResultUploadFile.html("");
         for(var i = 0 ; i < length ; i++){
            var respone = arrayRespone[i];
            var tr = $("<tr>");
            var tdFileName = $("<td>");
            tdFileName.html(respone.fileName);
            
            var tdResult = $("<td>");
            if(respone.isIgnore){
                tdResult.html("Không hợp lệ");
            }else {
               if(!respone.isConverted){
                   tdResult.html("Không thể convert");
               }else if(!respone.isSaved){
                   tdResult.html("Lỗi database");
               }else{
                   tdResult.html("Thành công");
               }
            }
            tr.append(tdFileName,tdResult);
            tbodyResultUploadFile.append(tr);
         }
         if(dataTableResultUploadFile == null){
             dataTableResultUploadFile = tableResultUploadFile.DataTable({
                 "oLanguage" : {
                     "sSearch" : "Tìm kiếm",
                     "oPaginate" : {
                        "sNext" : "Tới",
                        "sPrevious" : "Lùi"
                     },
                     "sInfo" : "_START_ - _END_ trên tổng _TOTAL_"
                  }
               });
             tableResultUploadFile.removeClass("hide");
         }
         
         updateTips("Upload thành công", "success", tip);
         refreshFileInput();

         /*
          * var detailsDoc = JSON.parse(responeData.data) var formDetailsDoc =
          * $("#formDetailsDoc"); var titleDocRespone = $("#titleDocRespone"); var
          * byUser = $("#byUser"); var dateCreate = $("#dateCreate"); var idDoc =
          * $("#idDoc");
          * 
          * console.log(detailsDoc); titleDocRespone.val(detailsDoc.title);
          * byUser.text(detailsDoc.byUser); var date = new
          * Date(detailsDoc.dateCreate);
          * 
          * 
          * var dateFormat = require('dateformat'); dateFormat(dateCreate, "dddd,
          * mmmm dS, yyyy");
          * 
          * 
          * dateCreate.text($.datepicker.formatDate('dd/mm/yy', date));
          * idDoc.val(detailsDoc.idFile);
          */
      }
   }
});

/** import word from file* */
$(function() {
   var uploadWordFiles = [];
   var data = null;
   var themtufileContent = $("#themtufile");
   var form = themtufileContent.find("form");
   var buttonImport = form.find("button");
   var buttonSelectFile = form.children("span");
   var fileInput = form.find("input");
   var label = form.find("label");

   form.submit(function(e) {
      e.preventDefault();
   })

   buttonSelectFile.click(function() {
      fileInput.trigger('click');
   });

   fileInput.on('change', function(event) {
      uploadWordFiles = event.target.files;
      var uploadWordFile = uploadWordFiles[0];
      // enable btn upload
      if (uploadWordFile != null && uploadWordFile != undefined) {
         buttonImport.prop("disabled", false);
         label.html(uploadWordFile.name);
      } else {
         buttonImport.attr("disabled", "disabled");
         label.html('');
      }
   });

   buttonImport
         .click(function(e) {
            var showContent = $('#show-content');
            showContent.html('');
            data = processFileUploadWord();
            if (data == null) {
               updateTips("Lỗi khi upload", "", tip);
            } else if (data.message == "SUCCESS") {
               source = jQuery.parseJSON(data.data);
               var listKeys = Object.keys(source);
               var length = listKeys.length;

               // create table
               var table = $('<table id="tableRespone" class="display compact" cellspacing="0" width="100%">');
               var thead = $('<thead>');
               var tfoot = $('<tfoot>');
               var trHead = $('<tr>');
               var trFoot = $('<tr>');
               trHead.append('<th>Từ Khóa</th>');
               trHead.append('<th>Kết Quả</th>');
               trFoot.append('<th>Từ Khóa</th>');
               trFoot.append('<th>Kết Quả</th>');
               thead.append(trHead);
               tfoot.append(trFoot);
               var tbody = $('<tbody>');
               for (var i = 0; i < length; i++) {
                  var keySearch = listKeys[i];
                  var validate = source[listKeys[i]];
                  var trBody = $('<tr>');
                  var tdKeySearch = $('<td>').append(keySearch);
                  var tdValidate = $('<td>');
                  if (validate) {
                     tdValidate.append("Thành công");
                     trBody.append(tdKeySearch);
                     trBody.append(tdValidate);
                  } else {
                     tdValidate.append("Thất Bại");
                     trBody.append(tdKeySearch);
                     trBody.append(tdValidate);
                  }

                  tbody.append(trBody);
               }
               table.append(thead);
               table.append(tfoot);
               table.append(tbody);
               showContent.html(table);
               $('#tableRespone').DataTable({
                  "oLanguage" : {
                     "sSearch" : "Tìm kiếm",
                     "oPaginate" : {
                        "sNext" : "Tới",
                        "sPrevious" : "Lùi"
                     },
                     "sInfo" : "_START_ - _END_ trên tổng _TOTAL_"
                  }
               });
               //refresh file input
               refreshFileInput();
            } else if (data.message == "ERROR") {
               source = data.data;
               updateTips("Lỗi trong quá trình chèn từ vào database"
                     + source, "", tip);
            }

         });

   function refreshFileInput() {
      uploadWordFiles = [];
      label.html('');
   }

   function processFileUploadWord() {
      var responeData;
      var formData = new FormData();
      formData.append('file', uploadWordFiles[0]);
      $.ajax({
         url : 'admin/uploadFile',
         data : formData,
         processData : false,
         contentType : false,
         async : false,
         type : 'POST',
         success : function(respone) {
            responeData = respone;
         },
         error : function(err) {
            updateTips("Lỗi:" + err, "", tip);
         }
      });
      return responeData;
   }

   function prepareLoad(event) {
      uploadWordFiles = event.target.files;
   }
});
