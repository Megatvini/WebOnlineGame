/**
 * Created by rezo on 6/6/15.
 *
 */
var Circle = IgeEntity.extend({
    classId: 'Cyrcle',
    init: function (distanceR,radius) {
        this.radius = radius ;
        var self = this;
        IgeEntity.prototype.init.call(this);
        // Load the character texture file
        this._circleTexture = ige.client.textures.circle;
        self.width(distanceR)
            .height(distanceR)
            .texture(this._circleTexture);
    }

});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Circle; }