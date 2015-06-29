/**
 * Created by rezo on 6/28/15.
 */

window.onload = function() {

} ;


function showError() {
    document.getElementById("status").innerHTML = "large file";
}
function upload() {
    document.getElementById("status").innerHTML = "Uploading " ;

    var formData = document.getElementById("imageForm");


    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.addEventListener("error", showError, false);
    xhr.open("POST", "uploadPic", true); // If async=false, then you'll miss progress bar support.
    xhr.send(new FormData(formData));
}

function uploadProgress(event) {
    // Note: doesn't work with async=false.
    var progress = Math.round(event.loaded / event.total * 100);
    document.getElementById("status").innerHTML = "Progress " + progress + "%";
}
var source ;
function uploadComplete(event) {
    document.getElementById("status").innerHTML = event.target.responseText;
    var s = document.getElementById("nickname").value;
        source = 'http://'+window.location.host+'/images?nickname='+s+'&date='+new Date().getTime();

    setTimeout(changeImage,20);

}
function changeImage(){
    $("#profPic").attr("src",source)
}