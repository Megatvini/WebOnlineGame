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
        var self = this;
        IgeUiElement.prototype.init.call(this);

        //mount(self.scene1)
    },

    createBackScene: function(){
        ige.ui.style('#topNav', {
            'backgroundColor': '#212121',
            'top': 0,
            'left': 0,
            'right': 0,
            'height': 42
        });

        ige.ui.style('#leftNav', {
            'backgroundColor': '#3d3d3d',
            'top': 42,
            'left': 0,
            'width': 225,
            'bottom': 0
        });

        ige.ui.style('#main', {
            'backgroundColor': '#ffffff',
            'left': 225,
            'right': 0,
            'top': 42,
            'bottom': 0
        });

        /* ige.ui.style('#logo', {
         'backgroundImage': self.gameTexture.metronic,
         'backgroundRepeat': 'no-repeat',
         'middle': 0,
         'left': 20,
         'width': 86,
         'height': 14
         });*/

        ige.ui.style('.title', {
            'font': '3em Open Sans',
            'color': '#666666',
            'width': 200,
            'height': 40,
            'top': 10,
            'left': 10
        });

        ige.ui.style('.subTitle', {
            'font': 'lighter 16px Open Sans',
            'color': '#666666',
            'width': 400,
            'height': 40,
            'top': 40,
            'left': 11
        });

        ige.ui.style('IgeUiTextBox', {
            'backgroundColor': '#ffffff',
            'borderColor': '#212121',
            'borderWidth': 1,
            'bottom': null,
            'right': null,
            'width': 300,
            'height': 30,
            'left': 15,
            'font': '12px Open Sans',
            'color': '#000000'
        });

        ige.ui.style('#textBox1', {
            'top': 140
        });

        ige.ui.style('#textBox2', {
            'top': 180
        });

        ige.ui.style('#textBox1:focus', {
            'borderColor': '#00ff00'
        });

        ige.ui.style('#textBox2:focus', {
            'borderColor': '#00ff00'
        });

        ige.ui.style('#dashBar', {
            'backgroundColor': '#eeeeee',
            'top': 80,
            'left': 15,
            'right': 15,
            'height': 40
        });

        ige.ui.style('IgeUiLabel', {
            'font': '12px Open Sans',
            'color': '#000000'
        });

        ige.ui.style('#homeLabel', {
            'font': '14px Open Sans',
            'color': '#333333'
        });

        var topNav = new IgeUiElement()
            .id('topNav')
            .mount(ige.client.uiScene);

        /* new IgeUiElement()
         .id('logo')
         .mount(topNav);*/

        var leftNav = new IgeUiElement()
            .id('leftNav')
            .mount(ige.client.uiScene);



    },

    createStatscene: function () {
        var main = new IgeUiElement()
            .id('main')
            .mount(self.uiScene);

        new IgeUiLabel()
            .value('Dashboard')
            .styleClass('title')
            .mount(main);

        new IgeUiLabel()
            .value('Login with your username and password')
            .styleClass('subTitle')
            .mount(main);


        var dashBar = new IgeUiElement()
            .id('dashBar')
            .mount(main);

        new IgeUiLabel()
            .id('homeLabel')
            .value('Home')
            .width(100)
            .height(40)
            .left(0)
            .top(0)
            .mount(dashBar);
    }

});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = UI; }