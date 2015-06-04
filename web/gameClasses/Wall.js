// Define our player character classes
var Wall = IgeEntity.extend({
    classId: 'Wall',

    init: function (x,y,width,heigth) {
        var self = this;
        IgeEntity.prototype.init.call(this);


        // Load the character texture file
        this._characterTexture = new IgeCellSheet('assets/wall.png');

        // Wait for the texture to load
        this._characterTexture.on('loaded', function () {
            self.texture(self._characterTexture);

        }, false, true);
        self = new IgeEntityBox2d()
            .translateTo(x, y, 0)
            .width(width)
            .height(heigth)
            .drawBounds(true)
            //.mount(self.scene1)
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