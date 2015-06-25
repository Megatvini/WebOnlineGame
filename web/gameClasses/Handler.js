/**
 * Created by rezo on 6/16/15.
 */
var characters = {}, // players in the world
    gameConfig = {},
    circles = {}, //sizes
    potions = {}, //potions
    player1,
    connection,
    distanceR ;
potNum  =  -1 ;
removedPots = 0 ;
var Handler = IgeEntity.extend({
    classId: 'Handler',
    lastUpdate:  null,
    lastUpdate2:  null,
    interpolation : 80 ,
    extrapolation: 100,

    init: function () {
        this. updateBuffer = []
        var self = this;
        IgeEntity.prototype.init.call(this);
        self.mount(ige)
            .width(0)
            .height(0);
        // Load the character texture file
        //.mount(self.scene1)
    },

    /**
     * on each frame, takes one update from updateBuffer. update contains information about
     * all players
     * @param ctx
     */
    update: function (ctx) {

        this.getNextUpdate();

        IgeEntity.prototype.update.call(this, ctx);
    },



    getNextUpdate: function(){

        var currentTime = new Date().getTime();
        var lastUpdate = this.lastUpdate;
        var lastUpdate2 = this.lastUpdate2;
        if (this.updateBuffer.length != 0) {
            var update1 = JSON.parse(JSON.stringify(this.updateBuffer[0]));
            var differense = currentTime - update1.date;
            var update = update1.snapShot;
            //console.log("bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
            if (differense >= this.interpolation) {
                //console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
                /*if(update.removePots.length>0)
                    console.log("removed ");*/
                this.handler(update);
                this.lastUpdate2= JSON.parse(JSON.stringify(this.lastUpdate));
                this.lastUpdate=update;
                this.updateBuffer.shift();

            } else if (lastUpdate != null) { // inerpolating
               /* if(update.removePots.length>0)
                    console.log("removed ");*/
                this.handler(update,lastUpdate);

            }
        }
        else if (lastUpdate2 != null && lastUpdate != null) {   //  prediction
           /* if(update.removePots.length>0)
                console.log("removed ");*/
            this.handler(lastUpdate,lastUpdate,lastUpdate2);
        }
        return null;


    },

    interpolate: function(update,lastUpdate){
        var y;
        var x;
        x = (lastUpdate.x + (update.x - lastUpdate.x) / 2);
        y = (lastUpdate.y + (update.y - lastUpdate.y) / 2);
        //console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y);
        return {x: x, y: y};

    } ,

    extrapolate: function(lastUpdate,lastUpdate2){
        var y;
        var x;
        x = (lastUpdate.x + (lastUpdate.x - lastUpdate2.x) / 2);
        y = (lastUpdate.y + (lastUpdate.y - lastUpdate2.y) / 2);
        //console.log(" $ no buffer $ bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y)
        return {x: x, y: y};
    } ,


    handler: function(snapShot, interpolated,extrapolated){

        /** @namespace snapShot.removePots */
        /** @namespace snapShot.addPots */
        var  addPots = snapShot.addPots ;
        var removePots= snapShot.removePots;
        //console.log(distanceR);
        /** @namespace snapShot.finished */
        if(snapShot.finished){
            if(!ige.isOFF) {
                ige.isOFF = true;
                this.showGameStats(snapShot.results);
                var self = this ;
                setTimeout(function() {
                    self.endGame();
                }, 60)
            }

        }else {
            /** @namespace snapShot.potNum */

            if(snapShot.potNum!=potNum) {
                potNum=snapShot.potNum;
                ige.$('scoreText').text(potNum + '  potions');
            }
            /** @namespace snapShot.players */
            this.parsePlayers(snapShot.players,interpolated,extrapolated);
            this.parsePotions(addPots,removePots);
            if (snapShot.distance != distanceR) {
                distanceR = snapShot.distance;
                this.mountCircles();
            }
        }


    },

    parsePlayers : function (players, lastUpdate,lastUpdate2) {
        var players1;

        for (var i in players) {
            if (players.hasOwnProperty(i)) {
                var onePlayer = players[i],
                    name = i,
                    position = onePlayer.position,
                    interpolatedposition = position;
                if (typeof (characters[name]) == 'undefined') {
                    /** @namespace gameConfig.pRadius */
                    var newPlayer = characters[name] = new Character(gameConfig, name, self.myId, self.textures, connection, position)
                        .id(name)
                        .transTo(position.x, position.y)
                        .setType(self.playerTypes[name])
                        .mount(self.objectScene)
                        .depth(2);
                    if (name == self.myId) {
                        player1 = newPlayer;
                        player1.addComponent(PlayerComponent);
                    }
                } else {
                    if (!onePlayer.active) {
                        if (!characters[name].destroed) {
                            characters[name].destroy();
                            characters[name].destroed = true;
                        }
                    } else {
                        if (name != self.myId) {
                            if (lastUpdate) {
                                if (lastUpdate2)
                                    interpolatedposition = this.interpolate(lastUpdate.players[name].position, position)
                            } else if (lastUpdate) {
                                interpolatedposition = this.extrapolate(lastUpdate.players[name].position, lastUpdate2.players[name].position)
                            }
                            characters[name].transTo(interpolatedposition.x,interpolatedposition.y);

                            //TODO apply directly.
                        }
                    }
                }
            }
        }

    },

    mountCircles: function(){
        for(var char in characters) {
            if (characters.hasOwnProperty(char)){
                if(circles[char])
                    circles[char].destroy();
                var character   = characters[char];
                circles[char]=new Circle(distanceR,self.gameConfig.pRadius)
                    .mount(character)
            }

        }
    },
    parsePotions : function(addPots,removePots,interpolated,extrapolated) {
        var i;
        for(i = 0 ; i < addPots.length; i ++){
            var onePotion = addPots[i];
            var x = onePotion.x,
                y = onePotion.y,
                id= onePotion.id.toString();
            console.log("potion parser add id " + id);
            if(typeof (potions[id]) == 'undefined')
                potions[id]=new Potion()
                    .texture(self.textures.potion)
                    .width(self.gameConfig.potRadius * 2)
                    .height(self.gameConfig.potRadius * 2)
                    .transTo(x, y, self.gameConfig)
                    .mount(self.objectScene);
        }
        for(i = 0 ; i < removePots.length; i ++){
            //console.log(removePots[i].id);
            id = removePots[i].toString();
            console.log("potion parser remove id " + id);
            potions[id].destroy();
        }

    },




    /**
     * adds update (snapshot) in array.
     * @param data
     */
    addUpdate: function(data){
        /* if(data.addPots.length>0){
         console.log("+1")
         }*/
        /*if(data.removePots.length>0){
         removedPots++;
         console.log(removedPots)
         }*/
        var d = new Date().getTime();
        var map = {} ;
        var i, p  ;
        for(i = 0 ; i < data.players.length; i ++){
            p = data.players[i] ;
            map[p.name] = {'active': p.active, 'position': p.position} ;
        }
        data.players=JSON.parse(JSON.stringify(map));
/*

        if(data.removePots.length>0)
            console.log("id ->> " +data.removePots[0]);
*/

        this.handler(data);
        var update = {
         date:d,
         snapShot:data
         };
         this.updateBuffer.push(update);

    },


    /**
     * Shows who won the game
     * @param results
     */
    showGameStats: function(results) {

        self.UI.createStatscene(results)

    },


    endGame: function() {
        ige.client.objectScene.destroy();
        //self.textures.wall.destroy();
        //ige.stop();

        //alert("GAME OVER");
    }





});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Handler; }