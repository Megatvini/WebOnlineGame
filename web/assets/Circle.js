/**
 * Created by rezo on 6/17/15.
 */

var image = {
    render: function (ctx, entity) {
        // Draw the player entity
        /*var ga = ctx.globalAlpha ;
        ctx.globalAlpha = 0.3;*/
        //ctx.fillStyle = '#b400ff';
       // ctx.strokeStyle = '#e371ff';
        var centre = {
            x:entity._translate.x,
            y:entity._translate.y
        };

        var grd = ctx.createRadialGradient(centre.x, centre.y, 1, centre.x, centre.y, entity._geometry.x);
        grd.addColorStop(.2, 'rgba(0,0,0,0)');
        grd.addColorStop(.3, 'rgba(0,0,255,.6)');
        grd.addColorStop(1, 'rgba(0,0,0,.01)');

        ctx.beginPath();
        ctx.arc(centre.x,centre.y,entity._geometry.x,0,2*Math.PI);
        ctx.fillStyle = grd;
        ctx.fill();
        ctx.stroke();

    }
};