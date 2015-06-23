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
        var circleRadius = entity._geometry.x ;
        var playerRadius = entity.radius;
        var dr = playerRadius/circleRadius;
        grd.addColorStop(dr, 'rgba(0,0,0,0)');
        grd.addColorStop(dr, 'rgba(0,0,255,.6)');
        grd.addColorStop(1, 'rgba(0,0,255,.1)');

        ctx.beginPath();
        ctx.arc(centre.x,centre.y,entity._geometry.x,0,2*Math.PI);
        ctx.fillStyle = grd;
        ctx.strokeStyle = 'rgba(0,0,255,0)' ;
        ctx.fill();
        ctx.stroke();

    }
};