$(function() {
    var imageFileProvisional = [];
    var inputImageBtn = $(".input-image-btn");
    var fileProvisional = $("#file-provisional");
    var wrapper;
    var wrapperType;
    inputImageBtn.click(function() {
        wrapper = $(this).parent();
        console.log(wrapper);
        wrapperType;
        if (wrapper.hasClass("image-content")) {
            wrapperType = "image";
            fileProvisional.attr('accept', "image/*");
        } else if (wrapper.hasClass("audio-content")) {
            wrapperType = "audio";
            fileProvisional.attr('accept', "audio/*");
        }
        console.log(wrapperType);
        fileProvisional.trigger('click');
    });

    fileProvisional.on('change', prepareLoad);
    function prepareLoad(event) {
        imageFileProvisional = event.target.files;
        var message = processUpload();
        if (message == null || message == "fail") {
            // xu ly fail
        } else {
            var urlWithContextPath = "";
            $.ajax({
                url : "getURLWithContextPath",
                async : false,
                type : 'GET',
                success : function(respone) {
                    urlWithContextPath = respone;
                },
                error : function(err) {

                }
            });
        }
        var link = urlWithContextPath + "/" + message;
        var inputField = $("<input type=text>");
        inputField.attr('name', wrapperType + '[]');
        // inputField.addClass("insert");
        inputField.attr("disabled", true);
        inputField.val(link);
        var divProvisional = $("<div>");
        divProvisional.append(inputField);
        divProvisional.append('<a href="#" class="remove_field ui-icon ui-icon ui-icon-circle-close">Remove</a>');
        wrapper.find('.fields').append(divProvisional);
    }

    function processUpload() {
        var formData = new FormData();
        var message = null;
        formData.append('file', imageFileProvisional[0]);
        $.ajax({
            url : 'uploadImages',
            data : formData,
            processData : false,
            contentType : false,
            async : false,
            type : 'POST',
            success : function(respone) {
                console.log(respone);
                message = respone.MESSAGE;
            },
            error : function(err) {
                console.log(err);
            }
        });
        return message;
    }
});
