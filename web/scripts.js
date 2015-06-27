/**
 * Created by Nika on 6/27/2015.
 */

function writeText(text, date){
    $("#messages").append(
        '<div class="direct-chat-msg right">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-right">' + myNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-left">' + "todo" + '</span>'+
        '</div>'+
        ' <img class="direct-chat-img"  src="' + friendPic + '"" alt="message user image">' +
        '<div class="direct-chat-text">' +
        text + ' </div> </div>');
    $("#messageText").val("");
}

function writeTextFrom(text, date){
    $("#messages").append(
        '<div class="direct-chat-msg">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-left">' + profileToNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-right">' + date + '</span>'+
        '</div>'+
        ' <img class="direct-chat-img" src="' + myPic + '" alt="message user image">' +
        '<div class="direct-chat-text">' +
        text + ' </div> </div>');
}

var profileToNick;
var profileToID;
var myPic;
var friendPic;
var myNick;

function sendMessage(){
    var text = $("#messageText").val();
    writeText(text);
    if(!profileToID)
        return;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST","SendMessage",true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("profileTo="+profileToID+"&message="+text);
}

function update(data){
    var j = data ;
    if(j==null)
        return;
    var list  =  j[profileToNick] ;
    if(list==null)
        return ;
    console.log(list + " "+ profileToNick) ;
    var i ;
    for(i = 0 ; i < list.length; i ++ ){
        var oneMessage = list[i] ;
        writeTextFrom(oneMessage.text, oneMessage.date);
    }

    for(var m in j ){
        if(j.hasOwnProperty(m)) {
            var oneM = j[m];
            $("#"+m).text(oneM.length.toString());
        }
    }

}

function check() {
    $.get("http://"+window.location.host + "/MessageUpdate?msgsfrom="+profileToNick, function(resp) {
        console.log(resp);
        if (resp != null)
            update(resp)
    });
}

$(document).ready(function() {
    profileToID = $("#profileToID").val();
    profileToNick = $("#profileToNick").val();
    myNick =  $("#myNick").val();
    myPic =  $("#myPic").val();
    friendPic =  $("#friendPic").val();

    check();
    setInterval(check, 500);
});

$(document).ready(function() {
    $('.commentarea').keydown(function(event) {
        if (event.keyCode == 13) {
            this.form.submit();
            return false;
        }
    });
});

