/**
 * Created by Nika on 6/27/2015.
 */

function writeText(text, date){
    $("#messages").append('<li style="background-color: rgb(186, 223, 255); font-size: 19px; text-align: right;" ' +
        '">' + text + ' </li>');
    $("#messageText").val("");
}
var profileToNick;
var profileToID;

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
        writeText(oneMessage.text, oneMessage.date);
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

    check();
    setInterval(check, 500);
});

