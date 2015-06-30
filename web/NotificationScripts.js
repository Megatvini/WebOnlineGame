/**
 * Created by Annie on 27-Jun-15.
 */
function addToFriends(nickname, pic){
    var val =   '<li class="addedF">' +
                '<a href="Friends.jsp">' +
                '<i ></i>' +
                '<img src="/default.png"  data-path="'+nickname+'" alt="Smiley face" style="width: 50px; height: 50px; border-radius: 50%;">' +
                nickname +
                '</a>' +
                '</li>';

    $("#notFriends").append(val);


    $('img').each(function() {
        var self = $(this),
            nickname =  $(this).attr('data-path'),
            fullPath;
        if(self.attr("src")=='default.png'||self.attr("src")=='/default.png') {
            fullPath = 'http://' + window.location.host + '/images?nickname=' + nickname;
        }
        self.attr("src",fullPath);

    });
}

function addToMessages(nickname, pic, lastMessage, date){
var val = '<li class="addedM">'+
        '<a href="Messages.jsp?friend='+ nickname + '">'+
        '<div class="pull-left">'+
    '<img src="/default.png"  data-path="'+nickname+'" alt="Smiley face" style="width: 50px; height: 50px; border-radius: 50%;">'+
        '</div>'+
        '<h4>'+
    nickname+
    '<small><i class="fa fa-clock-o"></i>' + date + '</small>'+
    '</h4>'+
    '<p>'+lastMessage+'</p>'+
    '</a>'+
    '</li>';

    $("#notMessages").append(val);

    $('img').each(function() {
        var self = $(this),
            nickname =  $(this).attr('data-path'),
            fullPath;
        if(self.attr("src")=='default.png'||self.attr("src")=='/default.png') {
            fullPath = 'http://' + window.location.host + '/images?nickname=' + nickname;
        }
        self.attr("src",fullPath);

    });
}

function addToGames(text, date){

}

function notificationCounts(requestsCount, messagesCount, invatesCount){
    $("#requestsSpan").append('<div class="deletable" >' + requestsCount + '</div>');
    $("#messagesSpan").append('<div class="deletable" >' + messagesCount + '</div>');
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

    $('.deletable').remove();
    notificationCounts(friendRequests.length, messages.length);
    //var gameInvites = data[2];

    $('.addedF').remove();
    for(var id in friendRequests){
        addToFriends(friendRequests[id].nickname, friendRequests[id].picPath);
    }

    $('.addedM').remove();
    for(var id in messages){
        var obj = messages[id];
        var lastIndex = obj['messages'].length - 1;
        var dateText = obj['messages'][lastIndex]['date'];
        var date1 =  new Date(dateText);
        var date2 = new Date();
        var dif = messageDate(date1, date2);
        addToMessages(obj['sender']['nickname'],obj['sender']['picPath'], obj['messages'][lastIndex]['text'], dif);
    }
}

$(document).ready(function() {
    checkNots();
    setInterval(checkNots, 5000);
});
var months = ["იანვარი", "თებერვალი", "მარტი", "აპრილი", "მაისი", "ივნისი", "ივლისი", "აგვისტო", "სექტემბერი", "ოქტომბერი", "ნომბერი", "დეკემბერი"];
 
var days = ["ორშაბათი", "სამშაბათი", "ოთხშაბათი", "ხუთშაბათი", "პარასკევი", "შაბათი", "კვირა"];
 
function messageDate(sentDate, now) {
   if (sentDate > now) {
      return "საიტზე მოხდა შეცდომა!"
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
      return hoursDifference + " საათის წინ";
   }
 
   var minutesDifference = Math.floor((now - sentDate) / 1000 / 60);
   if (minutesDifference > 0) {
      return minutesDifference + " წუთის წინ";
   }
 
   return "რამდენიმე წამის წინ";
}
function getReadableDate(date) {
   var hours = date.getHours();
   if (hours < 10) hours = "0" + hours;
   var minutes = date.getMinutes();
   if (minutes < 10) minutes = "0" + minutes;
   return days[date.getDay()] + " " + months[date.getMonth()] + " " + date.getDate() + " " + date.getFullYear() + ", " + hours + ":" + minutes;
}