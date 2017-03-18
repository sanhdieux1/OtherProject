//hien popup cho html
function bindMagnificPopup() {
    $('.simple-ajax-popup-align-top').magnificPopup({
        type : 'ajax',
        alignTop : true,
        overflowY : 'scroll' // as we know that popup content is tall we set
    // scroll overflow by default to avoid jump
    });
}

// add event search
$(function() {
    // create tab search result
    var tabs = $("#tabsResultSearch");
    var tabstabsResultSearch = tabs.tabs();
    // Set click event on search button
    $("#btnSearch").click(function() {
        var tabs = $("#tabsResultSearch");
        search(tabs);
        $("#display").hide();
    })

})

function search(tabs,isPersonal) {
    var key = $("#keyComplete").val();
    var formData = {
       keySearch :key,
       isPersonal : isPersonal?true:false
    }
    console.log(JSON.stringify(formData));
    $.ajax({
        url : 'search',
        data : formData,
        type : 'POST',
        async : false,
        success : function(data) {
            console.log(data);
            var source = data.source, arrayDoc = data.arrayDoc;
            if (data['final'] == 'true') {
                var keySearch = source.keySearch;
                var id = source.id;
                var byUser = source.by_user;
                var description = source.description;
                var date = source.date;
                var multimedia = source.multimedia;
                var previous = source.previous;
                var image = multimedia.image;
                var audio = multimedia['audio'];
                var linked = multimedia['linked'];
                var tabDescription = tabs.find("#description");
                var tabImage = tabs.find("#image");
                var tabAudio = tabs.find("#audio");
                var tabDocument = tabs.find("#document");
                tabDescription.html('<b>Mô Tả:</b><br>' + description);
                var lengImage = image.length;
                var divImage = $("<div>");
                for (var j = 0; j < lengImage; j++) {
                    var taga = $("<a>").attr('href', image[j]);
                    taga.addClass("image-popup-no-margins");
                    var img = $("<img height='160' width='160'>");
                    img.attr('src', image[j]);
                    taga.append(img);
                    divImage.append(taga);
                }
                tabImage.html(divImage);
                //
                var lengAudio = audio.length;
                var divAudio = $("<div>");
                for (var j = 0; j < lengAudio; j++) {
                    audioTag = $('<audio controls>');
                    source = $("<source type='audio/mpeg'>");
                    source.attr('src', audio[j]);
                    audioTag.append(source);
                    divAudio.append(audioTag);
                }
                tabAudio.html(divAudio);

                /*
                 * var lenglinked = linked.length; var divDocument = $("<div>");
                 * for (var j = 0; j < lenglinked; j++) { aTag = $("<a>");
                 * aTag.attr('href', linked[j]); divDocument.append(aTag);
                 * divDocument.append("<br>"); }
                 */
                var arrayDocLength = arrayDoc.length;
                var divDoc = $("<div>");
                for (var i = 0; i < arrayDocLength; i++) {
                    var docItem = arrayDoc[i];
                    var idDoc = docItem.idDoc;
                    var byUser = docItem.byUser;
                    var dateCreate = new Date(docItem.dateCreate);
                    var title = docItem.title;
                    var itemContent = '<div class="itemContent">' + '<div class="title"><a class="simple-ajax-popup-align-top mypupcuatao" href="html/' + idDoc + '.html"><h4>'
                            + title + '</h4></a></div>' + '<div class="info">Đăng bởi:<span>' + byUser + '&nbsp</span>Ngày đăng:<span>'
                            + $.datepicker.formatDate('dd/mm/yy', dateCreate) + '</span></div>' + '&nbsp<a href="download/' + idDoc
                            + '"><font color="blue"><i>Tải về</i></font></a>' + '</div>';
                    divDoc.append(itemContent);
                }
                tabDocument.html(divDoc);

                tabs.addClass('show');
                bindMagnificPopup();
            } else {
                console.log(data);
                var ulListResultSearch = tabs.parent().find("#listResultSearch ul");
                ulListResultSearch.html('');
                var arraySource = data.source;
                var length = arraySource.length;
                for (var i = 0; i < length; i++) {
                    var itemSource = arraySource[i];
                    ulListResultSearch.append('<li><a href="#">' + itemSource.keyword + '</a></li>')
                }
                ulListResultSearch.on('click', 'a', function(e) {
                    var keyComplete = $("#keyComplete");
                    e.preventDefault();
                    keyComplete.val($(this).text());
                    $("#listResultSearch").removeClass('show');
                    search(tabs);
                });
                tabs.removeClass("show");
                $("#listResultSearch").addClass('show');
            }
            presetEvent();
        },
        error : function(stt) {
            alert("error" + stt.message);
        }
    });
}
// Set keypress event input text
$(function() {
    var keyComplete = $("#keyComplete");
    var ulSuggest = $(".ulSuggest");
    keyComplete.on("keyup", function() {
        var keyComplete = $(this).val();
        autoComplete(keyComplete);

    });

    function autoComplete(keyword) {
        var keySearch = "keySearch=" + keyword;
        var display = $('#display');
        $.ajax({
            url : "autocomplete",
            data : keySearch,
            type : "GET",
            success : function(data) {
                var jsonArray = data;
                var leng = jsonArray.length;
                ulSuggest.html('');
                for (var i = 0; i < leng; i++) {
                    var liItem = $("<li>");
                    var aItem = $("<a href='#'>");
                    aItem.append(jsonArray[i]);
                    liItem.append(aItem);
                    ulSuggest.append(liItem);
                }
                $('#display').show();

                // add event click on a
                $(".ulSuggest a").on("click", function(e) {
                    e.preventDefault();
                    keyComplete.val($(this).text());
                    $('#display').hide();
                    $("#listResultSearch").removeClass('show');
                    var tabs = $("#tabsResultSearch");
                    search(tabs);
                });
            },
            error : function(stt) {
                console.log(stt);
            }
        });
    }

});

// set even submit login
$(function() {

    $("#submitLogin").click(function() {
        $.post("login", $("#loginBeanForm").serialize(), function(data) {
            var result = $("#result");
            result.html(data);
        });
    });
});

// set even menu tab
$(function() {
    $(".art-hmenu a").on('click', function(e) {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        $(".art-hmenu").find('a.active').removeClass('active');
        jQuery(this).addClass('active').siblings();
        // show/hide content
        $(".tab-content").find('div.active:first').removeClass('active')
        $(".tab-content " + currentAttrValue).addClass('active').siblings();
        // change url browser
        var urlPage = $("#urlPage").val();
        window.history.pushState("", "", urlPage + "?page=" + currentAttrValue.substring(1));

        if (currentAttrValue == "#trangcanhan") {
            $(".art-vmenublock").addClass('active').siblings();
        } else {
            $(".art-vmenublock").removeClass('active').siblings();
        }
        e.preventDefault();
    });

    $(".art-vmenu a").on('click', function(e) {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        $(".art-vmenu").find('a.active').removeClass('active');
        jQuery(this).addClass('active').siblings();

        $(".tab-content-person").find('div.active').removeClass('active')
        $(".tab-content-person " + currentAttrValue).addClass('active').siblings();

        e.preventDefault();
    });

});

//popup image
function presetEvent() {
    $('.image-popup-no-margins').magnificPopup({
        type : 'image',
        closeOnContentClick : true,
        closeBtnInside : false,
        fixedContentPos : true,
        mainClass : 'mfp-no-margins mfp-with-zoom', // class to remove default
        // margin from left and
        // right side
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
