var tip = $('#validateTip');

var listDictionary = null;
var dialogEditWord = $("#dialog-edit-word");
var dialogEditUser = $("#dialog-edit-user");
var dialogEditDoc = $("#dialog-edit-doc");
var idEditDoc = null;
var dataTableResultWord = null
var dataTableResultDoc = null;
var dataTableResultUser = null;
// create dialog
$(function() {
    var tips = $(".validateTipsEditWordDialog");
    // dialog edit doc
    dialogEditDoc.dialog({
        autoOpen : false,
        height : 170,
        width : 350,
        modal : true,
        open : function() {
        },
        buttons : {
            "Hoàn tất" : function() {
                submitEditDoc();
            },
            "Thoát" : function() {
                dialogEditDoc.dialog("close");
            }
        },
        close : function() {
            var formEditDoc = dialogEditDoc.find("form")
            formEditDoc[0].reset();
        }
    });

    // dialog edit user
    dialogEditUser.dialog({
        autoOpen : false,
        height : 650,
        width : 750,
        modal : true,
        open : function() {
            updateDialogTips('');
        },
        buttons : {
            "Hoàn tất" : function() {
                submitEditUser();
            },
            "Thoát" : function() {
                dialogEditUser.dialog("close");
            }
        },
        close : function() {
            formUser[0].reset();
        }
    });

    // add event submit dialog
    var formUser = dialogEditUser.find("form").on("submit", function(event) {
        event.preventDefault();
        submitEditUser();
    });
    var formEditDoc = dialogEditDoc.find("form").on("submit", function(event) {
        event.preventDefault();
        submitEditDoc();
    });
    // submit edit Doc

    function submitEditDoc() {
        var titleEditDoc = $("#titleEditDoc").val();
        var formData = {
            title : titleEditDoc,
            ID : idEditDoc
        }

        var jsonFromData = JSON.stringify(formData);
        console.log(jsonFromData);
        $.ajax({
            url : 'admin/editDoc',
            data : formData,
            type : 'POST',
            async : false,
            success : function(respone) {
                if (respone.message == 'true') {
                    dialogEditDoc.dialog("close");
                    setTimeout(function() {
                        dataTableResultDoc.ajax.reload();
                    }, 1000);
                        
                } else
                    updateTips("Cập nhật thất bại!", "", tip);
            },
            error : function(err) {
                console.log(err);
            }
        });

    }

    function submitEditUser() {

        /*
         * var objArray=formUser.serializeArray() ; var jsonString=JSON.stringify(objArray); var jsonObjData = jQuery.parseJSON(jsonString);
         */
        var usernameEdit = $("#usernameEdit").text();
        var emailEdit = $("#emailEdit").val();
        var fullNameEdit = $("#fullNameEdit").val();
        var addressEdit = $("#addressEdit").val();
        var phoneNumberEdit = $("#phoneNumberEdit").val();
        var roleEdit = $("#roleEdit").val();
        var birthDayEdit = $("#birthDayEdit");

        var formData = {
            'username' : usernameEdit,
            'email' : emailEdit,
            'fullName' : fullNameEdit,
            'address' : addressEdit,
            'birthDay' : new Date(birthDayEdit.val()).getTime(),
            'phoneNumber' : phoneNumberEdit,
            'role' : roleEdit
        }

        $.ajax({
            type : 'POST',
            url : 'admin/editUser',
            data : JSON.stringify(formData),
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            async : false,
            success : function(data) {
                console.log(data);
                if (data.MESSAGE == 'SUCCESS') {
                    updateTips("Cập nhật thành công", "success", tip);
                } else if (data.MESSAGE == 'KEY_IS_NOT_EXIST') {
                    callDialog('NOT USER', "Không tồn tại user này");
                } else if (data.MESSAGE == 'NOT_PERMISSION') {
                    callDialog('NOT PERMISSION', '<p> Bạn Không Có Quyền!</p>')
                }else if (data.MESSAGE == 'NOT_USER') {
                    callDialog('NOT USER', '<p> Không có tài khoản này!</p>')
                }
            },
            error : function(e) {
                alert("error:" + e);
            }
        });
        setTimeout(function() {
            dataTableResultUser.ajax.reload();
        }, 500);
        dialogEditUser.dialog("close");
        return true;
    }

    // dialog edit word
    var form;

    dialogEditWord.dialog({
        autoOpen : false,
        height : 500,
        width : 850,
        modal : true,
        open : function() {
            updateDialogTips('');
        },
        buttons : {
            "Hoàn tất" : function() {
                submitEditKeyWord();
            },
            "Thoát" : function() {
                dialogEditWord.dialog("close");
            }
        },
        close : function() {
            form[0].reset();
        }
    });

    form = dialogEditWord.find("form").on("submit", function(event) {
        event.preventDefault();
        submitEditKeyWord();
    });

    function updateDialogTips(message) {
        tips.text(message).addClass("ui-state-highlight");
    }

    function checkEmpty(field, message) {
        var valid = true;
        field.each(function() {
            if ($(this).val() == '' || $(this).length == 0) {
                $(this).addClass("has-error");
                updateDialogTips("Không được bỏ trống: " + message);
                valid = false;
            }
        });
        return valid;
    }

    function checkRegexp(o, regexp, n) {

        if (!(regexp.test(o.val()))) {
            o.addClass("ui-state-error");
            updateDialogTips(n);
            return false;
        } else {
            return true;
        }
    }
    function submitEditKeyWord() {
        var emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
        var multimediaEdit = $(".multimediaEdit");
        var keySearch = $("#keySearchEdit");
        var description = $("#descriptionEdit");
        var imageContent = multimediaEdit.find(".image-content").find(".fields").find('input');
        var audioContent = multimediaEdit.find(".audio-content").find(".fields").find('input');
        var linkedContent = multimediaEdit.find(".linked-content").find(".fields").find('input');

        multimediaEdit.find(".has-error").removeClass("has-error");

        var valid = true;

        valid = valid && checkEmpty(keySearch, "Từ khóa");
        valid = valid && checkEmpty(description, "Mô tả");
        valid = valid && checkEmpty(imageContent, "URL hình ảnh");
        valid = valid && checkEmpty(audioContent, "URL âm thanh");
        valid = valid && checkEmpty(linkedContent, "URL khác");

        if (valid) {
            // /submit edit
            var images = imageContent.map(function() {
                return this.value;
            }).get();
            var audio = audioContent.map(function() {
                return this.value;
            }).get();
            var linked = linkedContent.map(function() {
                return this.value;
            }).get();
            var keySearch = $("#keySearchEdit").val();
            var description = $("#descriptionEdit").val();
            var formData = {
                'image' : images,
                'audio' : audio,
                'linked' : linked,
                'keySearch' : keySearch,
                'description' : description
            }
            
            console.log(formData);
            $.ajax({
                type : 'POST',
                url : 'admin/editWord',
                data : JSON.stringify(formData),
                contentType : "application/json; charset=utf-8",
                // dataType: "json",
                async : false,
                success : function(data) {
                    if (data.message == 'SUCCESS') {
                        updateTips("Cập nhật thành công", "success",tip)
                        currentTabMenu("#danhsachtudien");
                    } else {
                        updateTips("Lỗi:" + data.message, "",tip);
                    }
                },
                error : function(e) {
                    updateTips("Lỗi:" + e, "",tip);
                }
            });

            setTimeout(function() {
                dataTableResultWord.ajax.reload();
            }, 500);
            dialogEditWord.dialog("close");
        }
        return valid;
    }

});

$(function() {

    // event click key word
    $(document).on('click', "#result a:first-child", function(e) {
        var currentAttrValue = $(this).text();
        var currentAttrID = $(this).attr('href');
        // Show/Hide Tabs

        $(".listWord-content").find('a.active').removeClass('active');
        $(this).addClass('active').siblings();

        $(".show-content").find('div.active').removeClass('active');
        $(".show-content " + currentAttrID).addClass('active').siblings();
        e.preventDefault();
    });

    // Set event data result is change and add event button
    // 'Del'
    $('#result').bind('DOMNodeInserted', function(event) {

    });

    // event simple insert word
    $("#formData").submit(function(e) {
        e.preventDefault();
    });

    // add event click menu
    $(".art-vmenu a").on('click', function(e) {
        e.preventDefault();
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        $(".art-vmenu").find('a.active').removeClass('active');
        jQuery(this).addClass('active').siblings();

        $(".tab-content").find('div.active').removeClass('active')
        $(".tab-content " + currentAttrValue).addClass('active').siblings();

    });
    // add event choose file
    $('#signUpForm input[type!="submit"]').click(function() {
        var text = $(this).val();
        if (text === "this is required") {
            $(this).val('');
        }
    });
});

/** insert word * */
$(function() {
    var multimediaForm = $(".multimediaForm");
    var imagesContent = multimediaForm.find(".image-content");
    var audioContent = multimediaForm.find(".audio-content");
    var linkedContent = multimediaForm.find(".linked-content");

    $("#btnInsert").click(function() {
        multimediaForm.find(".has-error").removeClass("has-error");
        var images = imagesContent.find(".fields").find("input");
        var imagesArray = images.map(function() {
            return this.value;
        }).get();

        var audio = audioContent.find(".fields").find("input");
        var audioArray = audio.map(function() {
            return this.value;
        }).get();

        var linked = linkedContent.find(".fields").find("input");
        var linkedArray = linked.map(function() {
            return this.value;
        }).get();
        var keySearch = $("#keySearch");
        var description = $("#description");

        var valid = true;
        function getValid() {
            return valid;
        }
        // verify keySearch
        if (keySearch.val() == '' || keySearch.length == 0) {
            keySearch.addClass("has-error");
            valid = false;
        }
        // verify description
        if (description.val() == '' || description.length == 0) {
            description.addClass("has-error");
            valid = false;
        }
        // verify images url
        var elems = images.nextAll(), count = elems.length;
        images.each(function() {
            var image = $(this);
            testImage(image.val(), function(resul) {
                valid = valid && resul;
                --count;
                if (!resul) {
                    image.addClass("has-error");
                }
            });
        });

        // verify audio url
        audio.each(function() {
            var field = $(this);
            if (field.val() == '' || field.length == 0) {
                field.addClass("has-error");
                valid = false;
            }
        });
        // verify linked url
        linked.each(function() {
            var field = $(this);
            if (field.val() == '' || field.length == 0) {
                field.addClass("has-error");
                valid = false;
            }
        });

        setTimeout(function() {
            if (count) {
                setTimeout(function() {
                    processInsert(getValid());
                }, 2000);
            } else {
                processInsert(valid);
            }
        }, 500);

        function processInsert(valid) {
            if (valid) {
                var formData = {
                    'image' : imagesArray,
                    'audio' : audioArray,
                    'linked' : linkedArray,
                    'keySearch' : keySearch.val(),
                    'description' : description.val()
                }
                $.ajax({
                    type : 'POST',
                    url : 'admin/insertWord',
                    data : JSON.stringify(formData),
                    contentType : "application/json; charset=utf-8",
                    // dataType: "json",
                    success : function(data) {
                        if (data.message == 'SUCCESS') {
                            resetField();
                            updateTips("Thêm từ thành công", "success",tip)
                            currentTabMenu("#themtudon");
                        } else {
                            updateTips("Lỗi:" + data.message, "",tip);
                        }
                    }
                })
            } else {
                console.log("fail valid");
            }
        }
        
        function resetField(){
            multimediaForm.find(".has-error").removeClass("has-error");
            keySearch.val('');
            description.val('');
            multimediaForm.find(".fields").html('');
        }
    });
});

// function choose tab menu
function currentTabMenu(menu) {
    // Show/Hide Tabs
    $(".art-vmenu").find('a.active').removeClass('active');
    $(".art-vmenu").find('a[href="' + menu + '"]').addClass('active').siblings();

    $(".tab-content").find('div.active').removeClass('active')
    $(".tab-content " + menu).addClass('active').siblings();

}

// create user
$(function() {
    $("#signUpForm").submit(function(e) {
        e.preventDefault();
        var multiInput = $(".inputSignUp");
        var check = true;
        multiInput.each(function() {
            var text = $(this).val();
            if (text === '' || text === 'this is required') {
                $(this).css({
                    "border" : "1px solid red",
                    "color" : "red"
                });
                $(this).val("this is required");
                check = false;
            } else {
                $(this).css({
                    "border" : "1px solid #BDB298",
                    "color" : "#4E4532"
                });
            }
        });
        var passWord = $('#password').val();
        var confirmPass = $('#confirmPass').val();
        if (passWord != confirmPass) {
            check = false;
            updateTips("Nhập lại mật khẩu không trùng nhau", "",tip);
            $('#confirmPass').css({
                "border" : "1px solid red"
            });
        } else if (passWord.length < 6) {
            check = false;
            updateTips('Mật khẩu tối thiểu 6 kí tự', "",tip);
            $('#passWord').css({
                "border" : "1px solid red"
            });
        } else {
            $('#passWord').css({
                "border" : "1px solid #BDB298"
            });
            $('#confirmPass').css({
                "border" : "1px solid #BDB298"
            });
        }
        if (check) {
            // submit form create user
            var username = $("#username").val();
            var password = $("#password").val();
            var role = $("#role").val();
            var email = $("#email").val();
            var fullName = $("#fullName").val();
            var sex = $(".tableUser input[name=sex]:checked").val();
            var birthDay = new Date($("#birthDay").val()).getTime();
            var address = $("#address").val();
            var phoneNumber = $("#phoneNumber").val();

            var dataSubmit = {
                'username' : username,
                'password' : password,
                'role' : role,
                'email' : email,
                'fullName' : fullName,
                'sex' : sex,
                'birthDay' : birthDay,
                'address' : address,
                'phoneNumber' : phoneNumber,
            };
            $.ajax({
                type : 'POST',
                url : 'admin/createUser',
                data : JSON.stringify(dataSubmit),
                contentType : "application/json; charset=utf-8",
                dataType : "json",
                success : function(data) {
                    successFunction(data);
                },
                error : function(error) {
                    updateTips("Lỗi:" + error, "",tip);
                }
            })
        }
    });

    function successFunction(json) {
        var objectJson = json;
        var message = objectJson.MESSAGE;
        var data = objectJson.DATA;
        if (message == "SUCCESS") {
            callDialog("SUCCESS", 'Tạo thành công user:<b>' + data + '</b>');
            // refresh fields
            $("#username").val('');
            $("#password").val('');
            $("#role").val('');
            $("#email").val('');
            $("#fullName").val('');
            $("#address").val('');
            $("#phoneNumber").val('');
            $("#confirmPass").val('');
            
        } else if (message == "ERROR") {
            if (data == "USER_IS_EXIST") {
                callDialog("USER IS EXIST", '<b>Tài khoản đã tồn tại!</b>');
            } else if (data == "NOT_PERMISSION") {
                callDialog("NOT PERMISSION", '<b>Bạn không có quyền tạo user này!</b>');
            } else if (data == "NOT_VALIDATE") {
                callDialog("NOT VALIDATE", '<b>Giá trị các trường không đúng!</b>');
            }
        }
    }
});
// Lay danh sach tu dien
$(function (){loadListWord();});

var refreshListWord = $("#refreshListWord");
refreshListWord.on('click',function(){
    if(dataTableResultWord==null){
        loadListWord();
    }else{
        dataTableResultWord.ajax.reload();
    }
    
});

function loadListWord() {
    dataTableResultWord = $("#tableResultWord").DataTable({
        "oLanguage" : {
            "sSearch" : "Tìm kiếm",
            "oPaginate" : {
                "sNext" : "Tới",
                "sPrevious" : "Lùi"
            },
            "sInfo" : "_START_ - _END_ trên tổng _TOTAL_"
        },
        "ajax" : "admin/getAll",
        dom : "Bfrtip",
        "columns" : [ {
            "data" : "keySearch"
        }, {
            "data" : "date",
            "render" : function(data) {
                var date = new Date(data);
                var month = date.getMonth() + 1;
                return date.getDate() + "/" + (month.length > 1 ? month : "0" + month) + "/" + date.getFullYear();
            }
        }, {
            "data" : "byUser"
        }, {
            "orderable" : false,
            "data" : null,
            "render" : function() {
                return '<a class="btn btn-success">' + '<i class="glyphicon glyphicon-zoom-in icon-white"></i>' + 'Xem' + '</a>'
            },
            "defaultContent" : '',
            "visible" : false
        }, {
            "orderable" : false,
            "data" : null,
            "render" : function() {
                return '<a class="btn btn-info">' + '<i class="glyphicon glyphicon-edit icon-white"></i>' + 'Sửa' + '</a>'
            },
            "defaultContent" : ''
        }, {
            "orderable" : false,
            "data" : null,
            "render" : function() {
                return '<a class="btn btn-danger">' + '<i class="glyphicon glyphicon-trash icon-white"></i>' + 'Xóa' + '</a>'
            },
            "defaultContent" : ''
        }, {
            "data" : "id",
            "visible" : false
        }, {
            "data" : "description",
            "visible" : false
        }, {
            "data" : "previous",
            "visible" : false
        }, {
            "data" : "multimedia",
            "visible" : false
        } ],
        "order" : [ [ 1, 'asc' ] ]
    });
    // event click btn Edit key word
    $("#tableResultWord tbody").on('click', 'a.btn-info', function() {
        var tr = $(this).closest('tr');
        var row = dataTableResultWord.row(tr);
        var rowData = row.data();
        $("#keySearchEdit").attr("value", rowData.keySearch);
        $("#descriptionEdit").val(rowData.description);

        var multimedia = rowData.multimedia;
        $(".multimediaEdit .fields").html('');
        if (multimedia != null) {
            var image = multimedia['image'];
            var audio = multimedia['audio'];
            var linked = multimedia['linked'];
            for (var i = 0; i < image.length; i++) {
                var divTam = $("<div>");
                divTam.append($('<input type="text" name="image[]">').attr("value", image[i]));
                divTam.append('<a href="#" class="remove_field ui-icon ui-icon-circle-close">Remove</a>');
                $(".multimediaEdit").find(".image-content .fields").append(divTam);
            }
            for (var i = 0; i < audio.length; i++) {
                var divTam = $("<div>");
                divTam.append($('<input type="text" name="audio[]">').attr("value", audio[i]));
                divTam.append('<a href="#" class="remove_field ui-icon ui-icon-circle-close">Remove</a>');
                $(".multimediaEdit").find(".audio-content .fields").append(divTam);
            }
            for (var i = 0; i < linked.length; i++) {
                var divTam = $("<div>");
                divTam.append($('<input type="text" name="linked[]">').attr("value", linked[i]));
                divTam.append('<a href="#" class="remove_field ui-icon ui-icon-circle-close">Remove</a>');
                $(".multimediaEdit").find(".linked-content .fields").append(divTam);
            }
        }
        dialogEditWord.dialog("open");
    });
    // event click on button Del
    $("#tableResultWord tbody").on('click', 'a.btn-danger', function() {
        var tr = $(this).closest('tr');
        var row = dataTableResultWord.row(tr);
        var rowData = row.data();
        var keySearch = rowData.keySearch;
        createDialogConfirm('Bạn thật sự muốn xóa: ' + keySearch, function() {
            var keyparam = "keyword=" + rowData.id;
            // delete keyword ajax
            $.ajax({
                url : "admin/deleteWord",
                data : keyparam,
                type : "GET",
                async : false,
                success : function(data) {
                    if (data) {
                        updateTips("Xóa từ thành công", "success" ,tip);
                        row.remove().draw();
                    }
                },
                error : function(e) {
                    updateTips("Lỗi:" + e, "",tip);
                }
            });
        });
    });

    presetEvent();
}

// function create table user
function createTableUser() {
    var editIcon = function(data, type, row) {
        if (type === 'display') {
            return data + ' <a class="ui-icon ui-icon-closethick" style="display:flex"></a>';
        }
        return data;
    };

    dataTableResultUser = $('#tableUser').DataTable({
        "oLanguage" : {
            "sSearch" : "Tìm kiếm",
            "oPaginate" : {
                "sNext" : "Tới",
                "sPrevious" : "Lùi"
            },
            "sInfo" : "_START_ - _END_ trên tổng _TOTAL_"
        },
        "ajax" : "admin/getAllUser",
        dom : "Bfrtip",
        "columns" : [ {
            "className" : 'details-control',
            "orderable" : false,
            "data" : null,
            "defaultContent" : ''
        }, {
            "className" : 'edit-control',
            "orderable" : false,
            "data" : null,
            "defaultContent" : ''
        }, {
            "className" : 'delete-control',
            "orderable" : false,
            "data" : null,
            "defaultContent" : ''
        }, {
            "data" : "username"
        }, {
            "data" : "fullName"
        }, {
            "data" : "role"
        }, {
            "data" : "address",
            "visible" : false
        }, {
            "data" : "phoneNumber",
            "visible" : false
        }, {
            "data" : "email",
            "visible" : false
        }, {
            "data" : "createBy",
            "visible" : false
        }, {
            "data" : "dateCreate",
            "visible" : false,
            "render" : function(data) {
                var date = new Date(data);
                var month = date.getMonth() + 1;
                return date.getDate() + "/" + (month.length > 1 ? month : "0" + month) + "/" + date.getFullYear();
            }
        } ],
        "order" : [ [ 1, 'asc' ] ]
    });

    // Add event listener for opening and closing details
    $('#tableUser tbody').on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = dataTableResultUser.row(tr);
        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        } else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    // Add event listener for edit
    $('#tableUser tbody').on('click', 'td.edit-control', function() {
        var tr = $(this).closest('tr');
        var row = dataTableResultUser.row(tr);
        var rowData = row.data();
        // set values field of dialog
        var fullNameEdit = $("#fullNameEdit");
        var usernameEdit = $("#usernameEdit");
        var dataCreateEdit = $("#dataCreateEdit");
        var roleEdit = $("#roleEdit");
        var fullNameEdit = $("#fullNameEdit");
        var addressEdit = $("#addressEdit");
        var birthDayEdit = $("#birthDayEdit");
        var emailEdit = $("#emailEdit");
        var phoneNumberEdit = $("#phoneNumberEdit");

        fullNameEdit.val(rowData.fullName);
        usernameEdit.text(rowData.username)
        dataCreateEdit.val($.datepicker.formatDate('yy-mm-dd', new Date(rowData.dateCreate)));
        var role = rowData.role;
        if (role == 'ADMIN') {
            roleEdit.val('administrator');
        } else if (role == 'USER') {
            roleEdit.val('user');
        } else if (role == 'DBA') {
            roleEdit.val('dba');
        }
        addressEdit.val(rowData.address);
        // set birth day
        var birthDay = new Date(rowData.birthDay);
        birthDayEdit.val(getDefaultDate(birthDay));

        emailEdit.val(rowData.email);
        phoneNumberEdit.val(rowData.phoneNumber);

        // call dialog edit user
        dialogEditUser.dialog("open");

    });

    // Add event listener for delete
    $('#tableUser tbody').on('click', 'td.delete-control', function() {
        var tr = $(this).closest('tr');
        var row = dataTableResultUser.row(tr);
        var rowData = row.data();
        // open dialog confirm
        createDialogConfirm('<b>Bạn thực sự muốn xóa tài khoản:' + rowData.username + '</b>', function() {
            var idUser = "idUser=" + rowData.username;
            // delete keyword ajax
            $.ajax({
                url : "admin/deleteUser",
                data : idUser,
                type : "GET",
                success : function(data) {
                    var meesage = data.MESSAGE;
                    if (meesage = "SUCCESS"){
                        updateTips("Xóa thành công", "success",tip);
                        row.remove().draw();
                    }
                    else if(meesage = "DB_ERROR"){
                        updateTips("Lỗi:" + data, "",tip);
                    } else if(meesage = "NOT_PERMISSION"){
                        
                    }
                },
                error : function(e) {
                    updateTips("Lỗi:" + e, "",tip);
                }
            });
        });
    });

    function format(d) {
        // `d` is the original data object for the row
        return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px; width:90%; ">' + '<tr>' + '<td>Địa chỉ:</td>' + '<td>' + d.address + '</td>'
                + '</tr>' +

                '<tr>' + '<td>Email:</td>' + '<td>' + d.email + '</td>' + '</tr>' +

                '<tr>' + '<td>Số điện thoại:</td>' + '<td>' + d.phoneNumber + '</td>' + '</tr>' +

                '<tr>' + '<td>Ngày tạo:</td>' + '<td>' + $.datepicker.formatDate('dd/mm/yy', new Date(d.dateCreate)) + '</td>' + '</tr>' +

                '<tr>' + '<td>Người Tạo:</td>' + '<td>' + d.createBy + '</td>' + '</tr>' +

                '</table>';
    }
}

// lay danh sach user

var refreshListUser = $("#refreshListUser");
refreshListUser.on('click',function(){
    if(dataTableResultUser==null){
        loadListUser();
    }else{
        dataTableResultUser.ajax.reload();
    }
    
});

$(function(){
    loadListUser();
})

function loadListUser() {
    // call function create table
    createTableUser();
}

// function create dialog
function callDialog(t, codehtml) {
    $("<div>").html(codehtml).dialog({
        title : t,
        resizable : false,
        modal : true,
        buttons : {
            "Ok" : function() {
                $(this).dialog("close");
            }
        }
    });
}

// popup image
function presetEvent() {
    $('.image-popup-no-margins').magnificPopup({
        type : 'image',
        closeOnContentClick : true,
        closeBtnInside : false,
        fixedContentPos : true,
        mainClass : 'mfp-no-margins mfp-with-zoom',
        image : {
            verticalFit : true
        },
        zoom : {
            enabled : true,
            duration : 300
        // don't foget to change the duration also in CSS
        }
    });
}

/** ** content quan ly doc **** */

// event lay danh sach doc
$(function() {
    var tableResultDoc = $("#tableResultDoc");
    
    $("#getAllDoc").on("click", getAllDoc);
    function getAllDoc() {
        createTable();
        tableResultDoc.removeClass("hide");
    }

    function createTable() {
        if (dataTableResultDoc == null) {
            dataTableResultDoc = tableResultDoc.DataTable({
                "ajax" : "getAllDoc",
                'dom' : "Bfrtip",
                "oLanguage" : {
                    "sSearch" : "Tìm kiếm",
                    "oPaginate" : {
                        "sNext" : "Tới",
                        "sPrevious" : "Lùi"
                    },
                    "sInfo" : "_START_ - _END_ trên tổng _TOTAL_"
                },
                "columns" : [ {
                    "className" : 'edit-control',
                    "orderable" : false,
                    "data" : null,
                    "defaultContent" : ''
                }, {
                    "className" : 'delete-control',
                    "orderable" : false,
                    "data" : null,
                    "defaultContent" : ''
                }, {
                    "data" : "title"
                }, {
                    "data" : "dateCreate",
                    "render" : function(data) {
                        var date = new Date(data);
                        var month = date.getMonth() + 1;
                        return date.getDate() + "/" + (month.length > 1 ? month : "0" + month) + "/" + date.getFullYear();
                    }
                }, {
                    "data" : "byUser"
                }, {
                    "data" : "id",
                    "visible" : false

                } ],
                "order" : [ [ 1, 'asc' ] ]
            });

            $('#tableResultDoc tbody').on('click', 'td.delete-control', function() {
                var tr = $(this).closest('tr');
                var row = dataTableResultDoc.row(tr);
                var rowData = row.data();
                // open dialog confirm
                $("<div>").html('<b>Bạn thực sự muốn xóa:' + rowData.title + '</b>').dialog({
                    title : "Xóa tài liệu",
                    resizable : false,
                    modal : true,
                    buttons : {
                        "Có" : function() {
                            var idUser = "idDoc=" + rowData.id;
                            // delete keyword ajax
                            $.ajax({
                                url : "admin/deleteDoc",
                                data : idUser,
                                type : "GET",
                                async : false,
                                success : function(data) {
                                    if (data)
                                        row.remove().draw();
                                    else
                                        alert("Loi:" + data);
                                },
                                error : function(e) {
                                    alert("error: " + e);
                                }
                            });
                            $(this).dialog("close");
                        },
                        "Không" : function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });

            $('#tableResultDoc tbody').on('click', 'td.edit-control', function() {
                dialogEditDoc.dialog("open");
                var tr = $(this).closest('tr');
                var row = dataTableResultDoc.row(tr);
                var rowData = row.data();
                idEditDoc = rowData.id;
            });
        } else {
            dataTableResultDoc.ajax.reload();
        }

    }

    /*
     * $('#tableResultDoc tbody').on('click', 'tr', function() { $(this).toggleClass('selected'); });
     */
});


function createDialogConfirm(text, ok) {
    var confirm = false;
    var dialog = $("<div>").html(text).dialog({
        title : "WARNING",
        resizable : false,
        modal : true,
        buttons : {
            "Đồng ý" : function() {
                ok();
                dialog.dialog("close");
            },
            "Không" : function() {
                dialog.dialog("close");
            }
        }
    });

    return confirm;
}

function getDefaultDate(date) {

    var now = date;
    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var today = now.getFullYear() + "-" + (month) + "-" + (day);

    return today;
}
