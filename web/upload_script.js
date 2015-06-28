/**
 * Created by rezo on 6/28/15.
 */

window.onload = function() {

} ;





function upload() {
    document.getElementById("status").innerHTML = "Uploading " ;

    var formData = document.getElementById("myForm");


    var xhr = new XMLHttpRequest();
    xhr.upload.addEventListener("progress", uploadProgress, false);
    xhr.addEventListener("load", uploadComplete, false);
    xhr.open("POST", "upload", true); // If async=false, then you'll miss progress bar support.
    xhr.send(new FormData(formData));
}

function uploadProgress(event) {
    // Note: doesn't work with async=false.
    var progress = Math.round(event.loaded / event.total * 100);
    document.getElementById("status").innerHTML = "Progress " + progress + "%";
}

function uploadComplete(event) {
    document.getElementById("status").innerHTML = event.target.responseText;
}