/** Event click download **/
$(function() {
   $("#itemContent").find("a").click(function(e) {
      $.get($(this).attr("href"), function(data) {
      }).fail(function() {
         e.preventDefault();
      });
   });
});