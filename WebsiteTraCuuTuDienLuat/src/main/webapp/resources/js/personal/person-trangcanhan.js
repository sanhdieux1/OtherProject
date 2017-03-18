
//event submit update information
$(function() {
    var informationForm = $("#informationForm"), validateTips = $("#validateTips");
    informationForm
	    .submit(function(e) {
		e.preventDefault();
		var username = $("#usernameAttribute").attr("data-prodnumber"), fullName = $(
			"#fullNameEdit").val(), sex = $("#sex:checked").val(), daySelect = $(
			".daySelect").val(), monthSelect = $(".monthSelect")
			.val(), yearSelect = $(".yearSelect").val(), email = $(
			"#email").val(), address = $("#address").val(), phoneNumber = $(
			"#phoneNumber").val();
		var birthDay = new Date(yearSelect, monthSelect - 1, daySelect);
		var dataSubmit = {
		    'username' : username,
		    'fullName' : fullName,
		    'sex' : sex,
		    'birthDay' : birthDay.getTime(),
		    'email' : email,
		    'address' : address,
		    'phoneNumber' : phoneNumber
		}
		console.log(dataSubmit);

		// call ajax submit
		$.ajax({
		    type : 'POST',
		    url : 'user/editUser',
		    data : JSON.stringify(dataSubmit),
		    contentType : "application/json; charset=utf-8",
		    dataType : "json",
		    async : false,
		    success : function(data) {
			console.log(data);
			if (data.MESSAGE == 'SUCCESS') {
			    updateTips("Cập nhật thông tin cá nhân thành công",
				    "alert alert-success");
			} else {
			    updateTips("Cập nhật thông tin thất bại",
				    "alert alert-danger");
			}
		    },
		    error : function(e) {
			updateTips("Cập nhật thông tin thất bại",
				"alert alert-danger");
		    }
		});
	    });

    function updateTips(t, c) {
	validateTips.text(t).addClass(c);
    }
});

// event select date
$(function() {
    var yearSelect = $(".yearSelect"), monthSelect = $(".monthSelect"), daySelect = $(".daySelect");

    // set option Year
    var optionYear = "", currentYear = new Date().getFullYear();
    fisrtYear = currentYear - 150;
    for (var i = fisrtYear; i <= currentYear; i++) {
	optionYear += "<option value=" + i + ">" + i + "</option>";
    }
    yearSelect.html(optionYear);

    // set year of birthday
    var yearOfBirthDay = $("#hiddenYearSelect").val();
    yearSelect.val(yearOfBirthDay);
    // set month of birthday
    var monthOfBirthDay = $("#hiddenMonthSelect").val();
    monthSelect.val(monthOfBirthDay);

    // set option Day
    setOptionDay(monthOfBirthDay, yearOfBirthDay);

    // set day of birthday
    var dayOfBirthDay = $("#hiddenDaySelect").val();

    daySelect.val(dayOfBirthDay);

    // add event month change
    monthSelect.change(function() {
	var year = yearSelect.val();
	var month = $(this).val();
	setOptionDay(month, year);
    });
    // add event year change
    yearSelect.change(function() {
	var year = $(this).val();
	var month = monthSelect.val();
	setOptionDay(month, year);
    });

    function setOptionDay(month, year) {
	// select day
	var options = "", day;
	switch (month) {
	case '2':
	    if ((year % 4) == 0) {
		if ((year % 100) == 0 && (year % 400) == 0)
		    day = 29;
		else
		    day = 28;
	    } else
		day = 28;
	    break;
	case '1':
	case '3':
	case '5':
	case '7':
	case '8':
	case '10':
	case '12':
	    day = 31;
	    break;
	case '4':
	case '6':
	case '9':
	case '11':
	    day = 30;
	    break;
	default:
	    day = 31;
	    break;
	}

	for (var i = 1; i <= day; i++) {
	    options += "<option value=" + i + ">" + i + "</option>";
	}

	daySelect.html(options);
    }
});

$(function(){
    var btnRefreshWordTable = $("#btnRefreshWordTable");
    btnRefreshWordTable.on('click',function(event){
        event.preventDefault();
        $.ajax({
            type : 'GET',
            url : 'user/getAll',
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            async : false,
            success : function(respone) {
            handleRespone(respone);
            },
            error : function(e) {
            updateTips("Cập nhật thông tin thất bại",
                "alert alert-danger");
            }
        });
    });
});


var wordPersonalContent = $("#word-personal-content");
var listContent= wordPersonalContent.find("div:first-child");
function handleRespone(respone){
    console.log(respone);
    var data = respone.data;
    var length = data.length;
    listContent.html('');
    console.log(listContent);
    var iconDelete = '<a href="#" class="remove_word ui-icon ui-icon ui-icon-circle-close">Remove</a>';
    for(var i=0 ; i < length ; i++){
        var wordItem = data[i];
        var div = $('<div>');
        var a = $('<a href="'+ wordItem.id+'" class="list-group-item">'+wordItem.keySearch+'</a>');
        div.append(a,iconDelete);
        listContent.append(div);
        
    }
    wordPersonalContent.append(listContent);
    
    setHandleDeleteIcon();
}

function setHandleDeleteIcon(){
    var wapper;
    listContent.on('click',".remove_word",function(event){
        wapper = $(this).parent("div");
        var wordPersonal = wapper.find("a:first-child");
        var idWord = wordPersonal.attr('href');
        var idWordPersonal = "idWordPersonal="+idWord;
        event.preventDefault();
        $.ajax({
            url : "user/deleteWordPersonal",
            data : idWordPersonal,
            type : "GET",
            async : false,
            success : function(data) {
                if (data) {
                    updateTips("Xóa từ thành công", "success",mainTips);
                    wapper.remove();
                }
            },
            error : function(e) {
                updateTips("Lỗi:" + e, "", mainTips);
            }
        });
    });
}


/**
 * Change pass word
 * 
 */

$(function(){
    var btnChangePass =$("#btnChangePass");
    var password = $("#password");
    var newPass = $("#newPass");
    var newPassAgain = $("#newPassAgain");
    
    btnChangePass.on('click',function(e){
        e.preventDefault();
        if(newPass.val() == newPassAgain.val()){
            var fromdata = {
             password : password.val(),
             newPass : newPass.val()
            }
            
            $.ajax({
                url : "user/editpass",
                data : fromdata,
                type : "POST",
                async : false,
                success : function(data) {
                    if (data.MESSAGE == "SUCCESS") {
                        updateTips("Đổi mật khẩu thành công", "success",mainTips);
                        password.val("");
                        newPass.val("");
                        newPassAgain.val("");
                    }else if(data.MESSAGE == "ERROR"){
                        updateTips("Lỗi: Xảy ra lỗi trong quá trình đổi mật khẩu", "", mainTips);
                    }else if(data.MESSAGE == "ERROR"){
                        updateTips("Mật khẩu cũ không chính xác", "", mainTips);
                    }
                },
                error : function(e) {
                    updateTips("Lỗi:" + e, "", mainTips);
                }
            });
        }else{
            updateTips("Nhập lại mật khẩu không đúng", "", mainTips);
        }
    });
});