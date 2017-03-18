/** Tin Tuc content * */
var tintucContent = $("#tintucContent");
// Lay danh sach cac tin tuc, van ban phap luat
$(function() {
	$.ajax({
		url : "getAllDoc",
		async : false,
		type: "GET",
		aync: false,
		success : function(result) {
			var data = result.data;
			var length = data.length;
			tintucContent.html('');
			for (var i = 0; i < length; i++) {
				var itemData = data[i], byUser = itemData.byUser, dateCreate = new Date(itemData.dateCreate), idDoc = itemData.id, title = itemData.title;
				// build html
				var itemContent = '<div class="itemContent">' + '<div class="title"><a class="simple-ajax-popup-align-top mypupcuatao" href="html/' + idDoc + '.html"><h4>' + title
				+ '</h4></a></div>' + '<div class="info">Đăng bởi:<span>' + byUser + '&nbsp</span>Ngày đăng:<span>' + $.datepicker.formatDate('dd/mm/yy', dateCreate)
				+ '</span></div>' 
				+ '&nbsp<a href="download/' +idDoc+ '"><font color="blue"><i>Tải về</i></font></a>'
				+ '</div>';
				tintucContent.append(itemContent);
			}
		},
		error: function(err){
			
		}
	});

	
	// goi ham Bat su kien cho tung title
	bindMagnificPopup();

	$('.simple-ajax-popup').magnificPopup({
		type: 'ajax'
	});
});

