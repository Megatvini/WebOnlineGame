/**
 * Created by rezo on 6/16/15.
 */
var Handler = IgeClass.extend({
    classId: 'Handler',
    init: function () {
        var self = this;
        IgeEntity.prototype.init.call(this);
        // Load the character texture file
        //.mount(self.scene1)
    },
    /**
     *Function that calculates legal coordinates of potion object. this is kind of override of function translateTo
     * @param x x coordinate in MODEL system
     * @param y y coordinate in MODEL system
     * @param config sizes, radius . needed for calculations
     * @returns {Potion}
     */
    transTo : function(x,y,config){
        this.translateTo(x-config.width/2+config.potRadius,
            y-config.height/2+config.potRadius,0);
        return this;

    }
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Handler; }