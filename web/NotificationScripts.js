/**
 * Created by Annie on 27-Jun-15.
 */
function addToFriends(nickname, pic){
    var val =   '<li>' +
                '<a href="Friends.jsp">' +
                '<i ></i>' +
                '<img src="'+ pic +'"  alt="Smiley face" style="width: 50px; height: 50px; border-radius: 50%;">' +
                'nickname' +
                '</a>' +
                '</li>';

    $("#notFriends").append(val);
}

function addToMessages(nickname, pic, lastMessage){
var val = '<li>'+
        '<a href="Messages.jsp?friend='+ nickname + '">'+
        '<div class="pull-left">'+
        '<img src="'+pic+'" class="img-circle" alt="User Image"/>'+
        '</div>'+
        '<h4>'+
    nickname+
    '<small><i class="fa fa-clock-o"></i> 5 mins</small>'+
    '</h4>'+
    '<p>'+lastMessage+'</p>'+
    '</a>'+
    '</li>';

    $("#notMessages").append(val);
}

function addToGames(text, date){

}

function checkNots() {
    $.get("http://"+window.location.host + "/MessageUpdate", function(resp) {
        console.log(resp);
        if (resp != null)
            updateNots(resp)
    });
}

function updateNots(data){
    var j = data ;
    if(j==null)
        return;

    var messageNots = data[0];
    var friendmessageNots = data[0];
    var gameNots = data[0];

}

$(document).ready(function() {
    checkNots();
    setInterval(checkNots, 2000);
});