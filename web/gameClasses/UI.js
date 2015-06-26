/**
 * Created by rezo on 6/20/15.
 */
/**
 * Created by rezo on 6/6/15.
 * this is potion class. creates new potion element . texture should be specified outside the class
 */
var UI = IgeUiElement.extend({
    classId: 'UI',
    init: function () {
        this.UITexture = { };
        var self = this;
        IgeUiElement.prototype.init.call(this);
        this.UITexture.nameFont = new IgeFontSheet('../assets/agency_fb_20pt.png', 0);
        this.UITexture.potNumFont = new IgeFontSheet('../assets/agency_fb_20pt.png', 0);
        this.UITexture.home = new IgeTexture('../assets/home.png');

    },

    createBackScene: function(){
        var backScene = this ;
       var muter =  new IgeUiElement()
            .texture(self.textures.soundON)
            .width(15)
            .height(15)
            .mount(self.graphicalUiScene)
            .top(80)
            .right(10);
        muter.togle = true;

       muter.mouseUp(function(){
           if(muter.togle){
               backScene.switchSound(true,muter);
               muter.texture(self.textures.soundOFF)
           }
           else{
               backScene.switchSound(false,muter);
               muter.texture(self.textures.soundON)
           }
       })



    },
    switchSound: function( b , muter ){
        var soundIndex
        for(soundIndex = 0 ; soundIndex < self.sounds.length; soundIndex++){
            self.sounds[soundIndex].muted = b;
        }
        self.sounds.winSound.muted= b;
        self.sounds.loop.muted= b;
        muter.togle = !b;

    },



    createStyles :  function(n){

    },

    addPlayers : function (playerTypes) {

        var ind  = 0 ;
        for(var statKey  in playerTypes){
            var oneType = playerTypes[statKey]
            new IgeUiElement()
                .texture(self.textures[oneType])
                .width(20)
                .height(20)
                .mount(self.graphicalUiScene)
                .top(5+(ind*(50)))
                .left(10);

            new IgeFontEntity()
                .texture(ige.client.textures.font)
                .textAlignX(0)
                .textAlignX(0)
                .width(150)
                .height(21)
                .text(statKey)
                .top(5+(ind*(50)))
                .left(33)
                .mount(self.graphicalUiScene);



            ind ++ ;

        }

    },

    removePlayer : function (playerId) {

    },


    createStatscene: function (results) {
        var uiInstance = this;
        ige.ui.style('.LstatSpot', {
            'backgroundColor': 'yellow',
            'borderColor': '#212121',
            'borderWidth': 1,
            'width': 180,
            'height': 50,
            'left': 25

        });

        ige.ui.style('.RstatSpot', {
            'backgroundColor': 'green',
            'width': 150,
            'height': 50,
            'left': 230
        });

        ige.ui.style('.Rplace', {
            'backgroundColor': 'white',
            'color' : 'blue',
            'width': 20,
            'height': 50,
            'left': 2
        });


        ige.ui.style('.LstatSpot:hover', {
            'backgroundColor': '#000011'


        });

        ige.ui.style('.RstatSpot:hover', {
            'backgroundColor': '#000011'


        });

        ige.ui.style('#main', {
            'backgroundColor': '#ffffff',
            'center': 0,
            'width': 400,
            'height': 400,
            'top': '25%'
        });


        ige.ui.style('.playerNames', {
            'color': 'blue',
            'width': 200,
            'height': 40,
            'left': 5

        });
        ige.ui.style('.playerPots', {
            'color': 'red',
            'width': 100,
            'height': 40,
            'left': 5

        });


        ige.ui.style('#home', {
            'backgroundImage': this.UITexture.home,
            'backgroundRepeat': 'no-repeat',
            'bottom': 0,
            'center': 0,
            'height': 70,
            'width': 70

        });

        var main = new IgeUiElement()
            .id('main')
            .mount(self.uiScene);

        var arr = Object.keys(results).map(function (key) {
            results[key]['id'] =key;
            return results[key];

        });

        arr.sort(function(a, b){
            /** @namespace a.place */
            return  -(b.place - a.place) ;
        });

        results=arr

        var startY = 10;

        for (var statKey  = 0 ; statKey < results.length ; statKey++  ) {

            var potNum = results[statKey].potNum;
            var place = results[statKey].place.toString();
            var id = results[statKey].id.toString();

            if(statKey==0&&id==self.myId){
                self.sounds.winSound.play();
            }

            var L = new IgeUiElement()
                .styleClass('LstatSpot')
                .top(startY)
                .mount(main);


            new IgeUiLabel()
                .styleClass('playerNames')
                .font(this.UITexture.nameFont)
                .mount(L)
                .value(id);

            new IgeUiLabel()
                .styleClass('Rplace')
                .font(this.UITexture.nameFont)
                .mount(main)
                .top(startY)
                .value(place);

            var R = new IgeUiElement()
                .styleClass('RstatSpot')
                .top(startY)
                .mount(main);

            new IgeUiLabel()
                .styleClass('playerPots')
                .font(this.UITexture.potNumFont)
                .mount(R)
                .value(potNum.toString());


            startY += 53;
        }




        uiInstance.redirectHome = function () {
            window.location.assign("http://localhost:8080")

        }

        var homeIcon = new IgeUiElement()
            .id('home')
            .mount(main)
            .mouseUp(uiInstance.redirectHome);

        homeIcon.mouseOver(function () {
            uiInstance.UITexture.home.applyFilter(IgeFilters.brighten, {value: 100});
        })


        homeIcon.mouseDown(function () {
            uiInstance.UITexture.home.applyFilter(IgeFilters.threshold, {value: 80});
        })
    }
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = UI; }