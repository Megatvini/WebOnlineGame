/**
 * Created by Annie on 27-Jun-15.
 */
function addToFriends(nickname, pic){
    $('.addedF').remove();
    var val =   '<li class="addedF">' +
                '<a href="Friends.jsp">' +
                '<i ></i>' +
                '<img src="'+ pic +'"  alt="Smiley face" style="width: 50px; height: 50px; border-radius: 50%;">' +
                'nickname' +
                '</a>' +
                '</li>';

    $("#notFriends").append(val);
}

function addToMessages(nickname, pic, lastMessage){
    $('.addedM').remove();
var val = '<li class="addedM">'+
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
    $.get("http://"+window.location.host + "/NotificationsUpdate", function(resp) {
        console.log(resp);
        if (resp != null)
            updateNots(resp)
    });
}

function updateNots(data){
    var j = data ;
    if(j==null)
        return;

    var friendRequests = data.friendRequestsFrom;
    var messages = data.newMessages;
    //var gameInvites = data[2];

    for(var friend in friendRequests){
        addToFriends(friend);
    }

    for(var message in messages){
        var list = messages[message];

        var lastIndex = list.length - 1;
        addToMessages(message,' ', list[lastIndex].text);
    }
}

$(document).ready(function() {
    checkNots();
    setInterval(checkNots, 5000);
});