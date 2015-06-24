/**
 * Created by rezo on 6/16/15.
 */
var Handler = IgeClass.extend({
    classId: 'Handler',
    updateBuffer: [] ,

    init: function () {
        var self = this;
        IgeEntity.prototype.init.call(this);
        // Load the character texture file
        //.mount(self.scene1)
    },

    transTo : function(x,y,config){
        this.translateTo(x-config.width/2+config.potRadius,
            y-config.height/2+config.potRadius,0);
        return this;

    },
    /**
     * on each frame, takes one update from updateBuffer. update contains information about
     * all players
     * @param ctx
     */
    update: function (ctx) {
        // Set the depth to the y co-ordinate which basically
        // makes the entity appear further in the foreground
        // the closer they become to the bottom of the screen
        //this.depth(this._translate.y);
        if(this.located) {
            if (!this.isMainCharacter) {
                var update = this.getNextUpdate();
                if (update != null) {
                    this.transTo(update.x, update.y);
                }
            }
        }
        IgeEntityBox2d.prototype.update.call(this, ctx);
    },



    getNextUpdate: function(){
        var y;
        var x;
        var currentTime = new Date().getTime();
        var lastUpdate = this.lastUpdate;
        var lastUpdate2 = this.lastUpdate2;
        if (this.updateBuffer.length != 0) {
            var update1 = JSON.parse(JSON.stringify(this.updateBuffer[0]));
            var differense = currentTime - update1.date;
            var update = update1.snapShot;
            //console.log("bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
            if (differense >= this.interpolation) {
                this.lastUpdate2= JSON.parse(JSON.stringify(this.lastUpdate));
                this.lastUpdate=update;
                this.updateBuffer.shift();
                //console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
                return update
            } else if (lastUpdate != null) { // inerpolating
                x = (lastUpdate.x + (update.x - lastUpdate.x) / 2);
                y = (lastUpdate.y + (update.y - lastUpdate.y) / 2);
                //console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y);
                return {x: x, y: y};
            }
        }
        else if (lastUpdate2 != null && lastUpdate != null) {   //  prediction
            x = (lastUpdate.x + (lastUpdate.x - lastUpdate2.x) / 2);
            y = (lastUpdate.y + (lastUpdate.y - lastUpdate2.y) / 2);
            //console.log(" $ no buffer $ bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y)
            return {x: x, y: y};
        }
        return null;


    },


    /**
     * adds update (snapshot) in array.
     * @param data
     */
    addUpdate: function(data){
        var d = new Date().getTime();
        var update = {
            date:d,
            snapShot:data
        };
        this.updateBuffer.push(update);

    },

});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Handler; }