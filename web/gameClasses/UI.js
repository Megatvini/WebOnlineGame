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



    },



    createStyles :  function(n){

    },

    addPlayers : function (playerTypes) {

        var ind  = 0 ;
        for(var statKey  in playerTypes){
            var oneType = playerTypes[statKey]
            new IgeUiElement()
                .texture(self.textures[0])
                .width(20)
                .height(20)
                .mount(self.graphicalUiScene)
                .top(5+(ind*(30+60)))
                .left(10);

            new IgeFontEntity()
                .texture(ige.client.textures.font)
                .textAlignX(0)
                .width(150)
                .text(statKey)
                .top(5+(ind*(30+60)))
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
            'width': 200,
            'height': 50,
            'left': 15

        });

        ige.ui.style('.RstatSpot', {
            'backgroundColor': 'green',
            'width': 150,
            'height': 50,
            'left': 230
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


        var startY = 10;
        for (var statKey in results) {
            if (results.hasOwnProperty(statKey)) {
                console.log("/.//////////////..." + statKey);
                var potNum = results[statKey].potNum;

                var L = new IgeUiElement()
                    .styleClass('LstatSpot')
                    .top(startY)
                    .mount(main);


                new IgeUiLabel()
                    .styleClass('playerNames')
                    .font(this.UITexture.nameFont)
                    .mount(L)
                    .value(statKey);

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