/**
 * Created by rezo on 6/30/15.
 */

$('img').each(function() {
    var self = $(this),
        nickname =  $(this).attr('data-path'),

        fullPath;
    if(typeof (nickname)=='undefined')
        return;
    if(self.attr("src")=='default.png'||self.attr("src")=='/default.png') {
        fullPath = 'http://' + window.location.host + '/images?nickname=' + nickname;
    }
    self.attr("src",fullPath);

});

