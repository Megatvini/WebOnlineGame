/**
 * Created by Nika on 6/27/2015.
 */

function writeText(text, date){
    text = replaceAll(':D', '<img src="dist/img/smiles/smile.gif" />',  text );
    text = replaceAll('(.Y.)', '<img src="dist/img/smiles/boob.gif" />',  text );
    var newDate = new Date();
    if (text == "") return;
    $("#messages").append(
        '<div class="direct-chat-msg right">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-right">' + myNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-left">' +  newDate.getHours() +
        ':' + newDate.getMinutes() + ':' + newDate.getSeconds() + '</span>'+
        '</div>'+
        ' <img class="direct-chat-img"  src="' + myPic + '"" alt="message user image">' +
        '<div class="direct-chat-text">' +
        text + ' </div> </div>');

    $("#messages").animate({ scrollTop: $('#messages')[0].scrollHeight}, 10);

    $("#messageText").val("");
}

function writeTextFrom(text, date){
    text = replaceAll(':D', '<img src="dist/img/smiles/smile.gif" />',  text );
    text = replaceAll('(.Y.)', '<img src="dist/img/smiles/boob.gif" />',  text );
    var newDate = new Date(date);
    $("#messages").append(
        '<div class="direct-chat-msg">' +
        '<div class="direct-chat-info clearfix">' +
        ' <span class="direct-chat-name pull-left">' + profileToNick + '</span>'+
        ' <span class="direct-chat-timestamp pull-right">' + newDate.getHours() +
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
var months = ["ი�?ნვ�?რი", "თებერვ�?ლი", "მ�?რტი", "�?პრილი", "მ�?ისი", "ივნისი", "ივლისი", "�?გვისტ�?", "სექტემბერი", "�?ქტ�?მბერი", "ნ�?მბერი", "დეკემბერი"];

var days = ["�?რშ�?ბ�?თი", "ს�?მშ�?ბ�?თი", "�?თხშ�?ბ�?თი", "ხუთშ�?ბ�?თი", "პ�?რ�?სკევი", "შ�?ბ�?თი", "კვირ�?"];

function messageDate(sentDate, now) {
    if (sentDate > now) {
        return "ს�?იტზე მ�?ხდ�? შეცდ�?მ�?!"
    }

    var readableDate = getReadableDate(sentDate);

    var daysDifference = Math.floor((now - sentDate) / 1000 / 60 / 60 / 24);
    if (daysDifference >= 365
        || now.getMonth() != sentDate.getMonth()) {
        return readableDate;
    }

    if (daysDifference > 0) {
        return daysDifference + " დღის წინ";
    }

    var hoursDifference = Math.floor((now - sentDate) / 1000 / 60 / 60);
    if (hoursDifference > 0) {
        return hoursDifference + " ს�?�?თის წინ";
    }

    var minutesDifference = Math.floor((now - sentDate) / 1000 / 60);
    if (minutesDifference > 0) {
        return minutesDifference + " წუთის წინ";
    }

    return "რ�?მდენიმე წ�?მის წინ";
}
function getReadableDate(date) {
    var hours = date.getHours();
    if (hours < 10) hours = "0" + hours;
    var minutes = date.getMinutes();
    if (minutes < 10) minutes = "0" + minutes;
    return days[date.getDay()] + " " + months[date.getMonth()] + " " + date.getDate() + " " + date.getFullYear() + ", " + hours + ":" + minutes;
}

function replaceAll(find, replace, str) {
    return str.replace(new RegExp(find, 'g'), replace);
}
