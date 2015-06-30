/**
 * Created by Nika on 6/27/2015.
 */

function writeText(text, date){
    var newDate = new Date();
    if (text == "") return;
    $("#messages").append(
        '<div class="direct-chat-msg right">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-right">' + myNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-left">' + newDate.getMonth() + '-' + newDate.getDay() + ' ' + newDate.getHours() +
        ':' + newDate.getMinutes() + ':' + newDate.getSeconds() + '</span>'+
        '</div>'+
        ' <img class="direct-chat-img"  src="' + myPic + '"" alt="message user image">' +
        '<div class="direct-chat-text">' +
        text + ' </div> </div>');

    $("#messages").animate({ scrollTop: $('#messages')[0].scrollHeight}, 10);

    $("#messageText").val("");
}

function writeTextFrom(text, date){
    var newDate = new Date(date);
    $("#messages").append(
        '<div class="direct-chat-msg">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-left">' + profileToNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-right">' + newDate.getMonth() + '-' + newDate.getDay() + ' ' + newDate.getHours() +
        ':' + newDate.getMinutes() + ':' + newDate.getSeconds() + '</span>'+
        '</div>'+
        ' <img class="direct-chat-img" src="' + friendPic + '" alt="message user image">' +
        '<div class="direct-chat-text">' +
        text + ' </div> </div>');
    $("#messages").animate({ scrollTop: $('#messages')[0].scrollHeight}, 10);
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

    myPic= 'http://' + window.location.host + '/images?nickname=' + myNick;
    friendPic =  $("#friendPic").val();
    friendPic= 'http://' + window.location.host + '/images?nickname=' + profileToNick;

    $.ajaxSetup({
        scriptCharacter: "utf-8",
        contentType: "application/json; charset=utf-8"
    });
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


$(document).ready(function() {
    checkNots();
    setInterval(checkNots, 5000);
});
var months = ["แแ?แแแ?แ แ", "แแแแแ แแ?แแ", "แแ?แ แขแ", "แ?แแ แแแ", "แแ?แแกแ", "แแแแแกแ", "แแแแแกแ", "แ?แแแแกแขแ?", "แกแแฅแขแแแแแ แ", "แ?แฅแขแ?แแแแ แ", "แแ?แแแแ แ", "แแแแแแแแ แ"];

var days = ["แ?แ แจแ?แแ?แแ", "แกแ?แแจแ?แแ?แแ", "แ?แแฎแจแ?แแ?แแ", "แฎแฃแแจแ?แแ?แแ", "แแ?แ แ?แกแแแแ", "แจแ?แแ?แแ", "แแแแ แ?"];

function messageDate(sentDate, now) {
    if (sentDate > now) {
        return "แกแ?แแขแแ แแ?แฎแแ? แจแแชแแ?แแ?!"
    }

    var readableDate = getReadableDate(sentDate);

    var daysDifference = Math.floor((now - sentDate) / 1000 / 60 / 60 / 24);
    if (daysDifference >= 365
        || now.getMonth() != sentDate.getMonth()) {
        return readableDate;
    }

    if (daysDifference > 0) {
        return daysDifference + " แแฆแแก แฌแแ";
    }

    var hoursDifference = Math.floor((now - sentDate) / 1000 / 60 / 60);
    if (hoursDifference > 0) {
        return hoursDifference + " แกแ?แ?แแแก แฌแแ";
    }

    var minutesDifference = Math.floor((now - sentDate) / 1000 / 60);
    if (minutesDifference > 0) {
        return minutesDifference + " แฌแฃแแแก แฌแแ";
    }

    return "แ แ?แแแแแแแ แฌแ?แแแก แฌแแ";
}
function getReadableDate(date) {
    var hours = date.getHours();
    if (hours < 10) hours = "0" + hours;
    var minutes = date.getMinutes();
    if (minutes < 10) minutes = "0" + minutes;
    return days[date.getDay()] + " " + months[date.getMonth()] + " " + date.getDate() + " " + date.getFullYear() + ", " + hours + ":" + minutes;
}

