/**
 * Created by rezo on 6/17/15.
 */
var Maze = IgeEntityBox2d.extend({
    classId: 'Maze',
    creator : {},
    gameConfig: {},
    snapshot: {},
    init: function (snapshot,creator) {
        this.snapshot=snapshot;
        this.creator = creator;
        this.gameConfig= creator.gameConfig;
        var self = this ;
        IgeEntity.prototype.init.call(this);



        //.mount(self.scene1)
    },

    /*createGoodMaze: function(){
        var maze,
            cellHeight,
            cellWidth,
            snapShot =   this.snapshot,
            gameConfig = this.gameConfig,
            map,
            height,
            width,
            tileheight,
            data1 = [],
            data2 = [],
            tilewidth;



        maze = snapShot.planeMaze;


        //number of spots
        width = gameConfig.width / gameConfig.wallWidth+2; // number of  cals
        height = gameConfig.height / gameConfig.wallWidth+2; // number of  rows

        //size of one spot
        tileheight = gameConfig.wallWidth; //one spot size
        tilewidth = gameConfig.wallWidth; //one spot size

        //number of spots in one cell
        cellWidth = ( width - maze.numCols + 1 ) / maze.numCols;
        cellHeight = ( height - maze.numRows + 1 ) / maze.numRows;


        var i,j;

        for(i = 0; i < width*height; ++i) {
           // data1[i] = 1;
            data2[i] = 0;
        }

        for(i = 0; i < maze.walls.length; ++i){
            var wall = maze.walls[i],
                col1 = wall.cell1.col,
                row1 = wall.cell1.row,
                col2 = wall.cell2.col,
                row2 = wall.cell2.row;

            var x  = col1*cellWidth+col1*tilewidth; //left most upper
            var y  = row1*cellHeight+row1*tileheight;//left most upper

            var x1 = x , y1 = y , endY = y1 , endX = x1 ;


            //up or down
            if( col1== col2 ){
                if( row1< row2 ){//down

                    x1 = x;                // starting x coordinate
                    y1+=cellHeight;        // starting y coordinate
                    endX=x1+cellWidth;     // we draw horizontally
                    endY=y1;               // y remains



                } else {//up

                    x1 = x;                // starting x coordinate
                    y1-=1;                 // starting y coordinate
                    endX=x1+cellWidth;     // we draw horizontally
                    endY=y1;               // y remains



                }
            } else if (col1 < col2) { //right

                x1 += cellWidth;       // starting x coordinate
                y1 += y;               // starting y coordinate
                endX = x1;             // x remains
                endY += cellHeight;    // we draw vertically


            } else { //left

                x1 -= 1;                               // starting x coordinate
                y1 += y;                           // starting y coordinate
                endX = x1;                           //x remains
                endY = y1 + cellHeight;             // we draw vertically


            }


            for(var k = x1; k <=endX; k ++){
                for(j = y1; j <=endY ; j++){
                    data2[j*width+k]=2;
                }
            }

        }

        var data= {
            data1:data1,
            data2:data2,
            tileWidth:tilewidth,
            tileHeight:tileheight
        };
        map = new Map(height,width,data);


        ige.addComponent(IgeTiledComponent)
            .tiled.loadJson(map.getData() /!* you can also use a url: 'maps/example.js'*!/, function (layerArray, layersById){
                // The return data from the tiled component are two arguments,
                // the first is an array of IgeTextureMap instances, each one
                // representing one of the Tiled map's layers. The ID of each
                // instance is the same as the name assigned to the Tiled
                // layer it represents. The second argument contains the same
                // instances but each instance is stored in a property that is
                // named after the layer it represents so instead of having to
                // loop the array you can simply pick the layer you want via
                // the name assigned to it like layersById['layer name']

                // We can add all our layers to our main scene by looping the
                // array or we can pick a particular layer via the layersById
                // object. Let's give an example:
                var i, destTileX = - 1, destTileY = -1,
                    tileChecker = function (tileData, tileX, tileY) {
                        // If the map tile data is set, don't path along it
                        return !tileData;
                    };

                for (i = 0; i < layerArray.length; i++) {
                    // Check if the layer is a tile layer
                    if (layerArray[i].type === 'tilelayer') {
                        // Before we mount the layer we will adjust the size of
                        // the layer's tiles because Tiled calculates tile width
                        // based on the line from the left-most point to the
                        // right-most point of a tile whereas IGE calculates the
                        // tile width as the length of one side of the tile square.
                        layerArray[i]
                            .tileWidth(tilewidth)
                            .tileHeight(tileheight)
                            .hoverColor('#ffffff')

                            //.isometricMounts(false)
                            .drawBounds(false)
                            .drawBoundsData(false)
                            .mount(ige.client.mainScene)
                            .translateTo(-150,-150,0);
                    }

                    // Check if the layer is an "object" layer
                    if (layerArray[i].type === 'objectlayer') {
                        //layerArray[i].mount(self.backScene);
                    }
                }

                // Or if we wanted to only use the "DirtLayer" from the example
                // map data, we could do this:
                //layersById.DirtLayer.mount(self.mainScene);

                // Create static box2d objects from the dirt layer
                ige.box2d.staticsFromMap(layersById.DirtLayer);


            });

    },*/


    transTo : function(x,y,config){
        this.translateTo(x-config.width/2+config.potRadius,
            y-config.height/2+config.potRadius,0);
        return this;

    },


    createMaze: function() {
        var snapShot = this.snapshot;
        var gameConfig = this.gameConfig;
        var maze = snapShot.planeMaze,
            cellHoryzSize = (gameConfig.width-(maze.numCols-1)*gameConfig.wallWidth)/maze.numCols,
            cellVertSize = (gameConfig.height-(maze.numRows-1)*gameConfig.wallWidth) /maze.numRows,
            walls = maze.walls,
            zeroX = gameConfig.width/ 2,
            zeroY = gameConfig.height/ 2,
            startX = 0,
            startY = 0;
        this.createFrame();
        this.createInnerMaze(walls, startX, cellHoryzSize, startY, cellVertSize, zeroX, zeroY);
    },
    createInnerMaze: function(walls, startX, cellHoryzSize, startY, cellVertSize, zeroX, zeroY) {
        var gameConfig = this.gameConfig;
        for (var wall1 in walls) {
            if (walls.hasOwnProperty(wall1)) {
                var wall = walls[wall1];
                var col1 = wall.cell1.col;
                var row1 = wall.cell1.row;
                var col2 = wall.cell2.col;
                var row2 = wall.cell2.row;
                var drawX;
                var drawY;
                var wWidth;
                var wHeight;
                var H = true;
                startX = (col1) * cellHoryzSize + col1 * gameConfig.wallWidth;
                startY = row1 * cellVertSize + row1 * gameConfig.wallWidth;

                //ixateba zemot an qvemot
                if (col1 == col2) {
                    if (row1 < row2) {

                        // ixateba qvemot

                        wHeight = gameConfig.wallWidth;
                        wWidth = cellHoryzSize + gameConfig.wallWidth;
                        drawY = startY + cellVertSize + gameConfig.wallWidth / 2;
                        drawX = startX + wWidth / 2;

                    } else {

                        //ixateba zemot

                        wHeight = gameConfig.wallWidth;
                        wWidth = cellHoryzSize + gameConfig.wallWidth;

                        drawY = startY - wHeight / 2;
                        drawX = startX + wWidth / 2;
                    }

                } else {///ixateba marjvniv an marcxniv
                    if (col1 < col2) { //marjvniv
                        wHeight = cellVertSize + gameConfig.wallWidth;

                        wWidth = gameConfig.wallWidth;
                        drawY = startY + wHeight / 2;
                        drawX = startX + cellHoryzSize + wWidth / 2;
                    }
                    else { // marcxniv
                        wHeight = cellVertSize + gameConfig.wallWidth;

                        wWidth = gameConfig.wallWidth;
                        drawY = startY + wHeight / 2;
                        drawX = startX - wWidth / 2;
                    }
                }
                //x,y,width,height

                this.createWall(wWidth, wHeight)
                    .translateTo(drawX-zeroX, drawY-zeroX, gameConfig);


            }
            //				drawOneWall(wall,cellHoryzSize,cellVertSize,)
        }
    },

    createFrame: function() {
        var gameConfig = this.gameConfig;
        ///marcxena
        var startCx = -gameConfig.width / 2 - gameConfig.wallWidth / 2;
        var startCy = 0;
        this.createWall(gameConfig.wallWidth, gameConfig.height)
            .translateTo(startCx, startCy, 0);

        /////////////////////////////////////////////////////////////////////////////////////

        startCx = 0;
        startCy = -gameConfig.height / 2 - gameConfig.wallWidth / 2;

        this.createWall(gameConfig.width + 2 * gameConfig.wallWidth, gameConfig.wallWidth)
            .translateTo(startCx, startCy, 0);

        ///////////////////////////////////////////////////////////////////////////////////////
        startCx = gameConfig.width / 2 + gameConfig.wallWidth / 2;
        startCy = 0;
        this.createWall(gameConfig.wallWidth, gameConfig.height)
            .translateTo(startCx, startCy, 0);

        //////////////////////////////////////////////////////////////////////////////////////////
        startCx = 0;
        startCy = gameConfig.height / 2 + gameConfig.wallWidth / 2;
        this.createWall(gameConfig.width + 2 * gameConfig.wallWidth, gameConfig.wallWidth)
            .translateTo(startCx, startCy, 0);
        return this ;
    },

    createWall:function(wWidth, wHeight) {
        var gameConfig= this.gameConfig ;
        return new IgeEntityBox2d()
            .width(wWidth)
            .height(wHeight)
            .texture(this.creator.textures.wall)
            .drawBounds(true)
            .mount(this.creator.objectScene)
            .box2dBody({
                type: 'static',
                allowSleep: true,
                fixtures: [{
                    shape: {
                        type: 'rectangle'
                    }
                }]
            });
        return this
    }





});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Maze; }