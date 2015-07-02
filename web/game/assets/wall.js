/**
 * Created by rezo on 6/22/15.
 */
var image = {
    render: function (ctx, entity) {
        ctx.beginPath();
        ctx.rect(-entity._geometry.x2,-entity._geometry.y2,entity._geometry.x,entity._geometry.y);

        if(entity.isFrame){

            ctx.fiilStyle =  'rgba(0,0,0,0)'  ;
        }else{
            ctx.fiilStyle =  'black' ;
        }
        ctx.strokeStyle = 'rgba(0,0,0,0)';
        ctx.fill();
        ctx.stroke();


    }
};