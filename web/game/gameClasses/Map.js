/**
 * Created by rezo on 6/18/15.
 */
var Map = IgeEntity.extend({
    classId: 'Map',
    init: function (height, width,data) {
        this.height = height;
        this.width = width;
        this.tileHeight=data.tileHeight;
        this.tileWidth=data.tileWidth;
        this.data1=data.data1;
        this.data2=data.data2;
        this.url='../assets/wall.png';
        this.url1='../assets/sky.png';
    },

    getData: function() {
        return {
            "height": this.height,
            "layers": [
                {

                    "data": this.data2,
                    "height": this.height ,
                    "name": "DirtLayer",
                    "opacity": 1,
                    "type": "tilelayer",
                    "visible": true,
                    "width": this.width ,
                    "x": -150,
                    "y": -150
                }],
            "orientation": "nonisometric",
            "properties": {},
            "tileheight": this.tileHeight,
            "tilesets": [
                {
                    "firstgid": 2,
                    "image": this.url,
                    "margin": 0,
                    "name": "dirtSheet",
                    "properties": {},
                    "spacing": 0,
                    "tileheight": this.tileHeight,
                    "tilewidth": this.tileWidth
                }],
            "tilewidth": this.tileWidth,
            "version": 1,
            "width": this.width
        }
    }
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Map; }