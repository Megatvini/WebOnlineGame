/**
 * Created by rezo on 6/26/15.
 */

var image = {
    render: function (ctx, entity) {
        ctx.beginPath();
        ctx.rect(-entity._geometry.x2,-entity._geometry.y2,entity._geometry.x,entity._geometry.y);
        ctx.fillStyle = "yellow";
        ctx.fill();
        ctx.stroke();

    }
};