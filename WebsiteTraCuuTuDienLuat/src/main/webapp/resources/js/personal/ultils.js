/** verify url* */
function testImageComplete(url, callback) {
   var timedOut = false, timer;
   var img = new Image();
   img.onerror = img.onabort = function() {
      callback(false);
   };
   img.onload = function() {
      callback(true);
   };
   img.src = url;
}

function testImageCanGet(url) {
   $.ajax({
      type : "GET",
      url : url,
      dataType : "image/jpg",
      success : function(img) {
         console.log("success");
      },
      error : function(error, txtStatus) {
         console.log(txtStatus);
         console.log('error');
      }
   }).always(function() {
      return true;
   });
}

function pause(milliseconds, img) {
   var dt = new Date();
   while (!img.complete && ((new Date()) - dt <= milliseconds)) { /* Do nothing */
   }
   if (img.complete) {
      return true;
   } else {
      return false;
   }
}

function testImage(url, callback) {
   timeout = 2000;
   var timedOut = false, timer;
   var img = new Image();
   img.onerror = img.onabort = function() {
      if (!timedOut) {
         clearTimeout(timer);
         callback(false);
         return;
      }
   };
   img.onload = function() {
      if (!timedOut) {
         clearTimeout(timer);
         callback(true);
         return;
      }
   };
   img.src = url;
   timer = setTimeout(function() {
      timedOut = true;
      callback(false);
      return;
   }, timeout);
}

