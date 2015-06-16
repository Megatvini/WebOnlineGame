// Define our player character classes
var Wall = IgeEntityBox2d.extend({
    classId: 'Wall',
    init: function (width,height,self) {

        IgeEntityBox2d.prototype.init.call(this);

        this.width(width)
            .height(height)
            .texture(self.gameTexture.wall)
            .box2dBody({
                type: 'static',
                allowSleep: true,
                fixtures: [{
                    shape: {
                        type: 'rectangle'
                    }
                }]
            });

    }


});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Wall; }