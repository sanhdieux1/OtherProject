var mainTips = $('#mainTips');
// add fields URL
$(document).ready(function() {
    var max_fields = 10;
    var max_fields_image = max_fields; // maximum input image
    var max_fields_audio = max_fields; // maximum input audio
    var max_fields_linked = max_fields; // maximum input linked
    var add_button = $(".add_field_button"); // Add button ID

    var num_field_image = 1; // initlal text box count
    var num_field_audio = 1;
    var num_field_linked = 1;

    $(add_button).click(function(e) { // on add input button click
        e.preventDefault();
        var wrapper = $(this).parent();
        if (wrapper.hasClass('image-content')) {
            var inputField = $("<input type=text>");
            inputField.attr('name', 'image[]');
            // inputField.addClass("insert");
            inputField.attr("required", true);
            inputField.attr("placeholder", "URL");

            var divProvisional = $("<div>");
            divProvisional.append(inputField);

            divProvisional.append('<a href="#" class="remove_field ui-icon ui-icon ui-icon-circle-close">Remove</a>');

            wrapper.find('.fields').append(divProvisional);
        } else if (wrapper.hasClass('audio-content')) {

            var inputField = $("<input type=text>");
            inputField.attr('name', 'audio[]');
            // inputField.addClass("insert");
            inputField.attr("required", true);
            inputField.attr("placeholder", "URL");
            var divProvisional = $("<div>");
            divProvisional.append(inputField);
            divProvisional.append('<a href="#" class="remove_field ui-icon ui-icon-circle-close">Remove</a>');
            wrapper.find('.fields').append(divProvisional); // add

        } else if (wrapper.hasClass('linked-content')) {

            var inputField = $("<input type=text>");
            inputField.attr('name', 'linked[]');
            // inputField.addClass("insert");
            inputField.attr("required", true);
            inputField.attr("placeholder", "URL");
            var divProvisional = $("<div>");
            divProvisional.append(inputField);
            divProvisional.append('<a href="#" class="remove_field ui-icon ui-icon-circle-close">Remove</a>');
            wrapper.find('.fields').append(divProvisional);

        }
    });
    var multimedia = $(".multimedia");
    multimedia.on("click", ".remove_field", function(e) { // user
        e.preventDefault();
        $(this).parent('div').remove();
    })
});

/** 
 * Get all word personal
 * **/
var dialogEditWord = $("#dialog-edit-word");
var tips = $(".validateTipsInsertWordDialog");
// dialog edit word
var form;
var btnAdd = $("#btnAddWordPersonal");
btnAdd.on('click',function(){
    dialogEditWord.dialog("open");
});

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
            url : 'user/insertWordPersonal',
            data : JSON.stringify(formData),
            contentType : "application/json; charset=utf-8",
            // dataType: "json",
            async : false,
            success : function(data) {
                if (data.message == 'SUCCESS') {
                    updateTips("Thêm thành công", "success",mainTips)
                } else {
                    updateTips("Lỗi:" + data.message, "",mainTips);
                }
            },
            error : function(e) {
                updateTips("Lỗi:" + e, "",mainTips);
            }
        });
        setTimeout(function() {
            $("#btnRefreshWordTable").click();
        }, 1000);
        dialogEditWord.dialog("close");
    }
    return valid;
}

function updateTips(t, c, tip){
    var timeBeginEffect = 2000;
    var timeEffect = 2500;
    var timeEndEffect = timeBeginEffect + timeEffect;
    tip.text("");
    tip.removeClass("alert-danger");
    tip.removeClass("alert-success");

    if (c == "success") {
        tip.text(t).addClass("alert-success");
        setInterval(function() {
            tip.removeClass("alert-success", timeEffect);
        }, timeBeginEffect);
        setInterval(function() {
            tip.html('');
        }, timeEndEffect);
    } else {
        tip.text(t).addClass("alert-danger");
        setInterval(function() {
            tip.removeClass("alert-danger", timeEffect);
        }, timeBeginEffect);
        setInterval(function() {
            tip.html('');
        }, timeEndEffect);

    }
}


