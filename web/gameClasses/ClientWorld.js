/**
 * Created by rezo on 6/17/15.
 */
var ClientWorld = {
    createWorld: function () {
        var self = this;
        this.mainScene = new IgeScene2d()
            .id('mainScene');


        this.backScene = new IgeScene2d()
            .id('backScene')
            .mount(this.mainScene);

        this.objectScene = new IgeScene2d()
            .id('objectScene')
            .mount(this.backScene);


        this.uiScene = new IgeScene2d()
            .id('uiScene')
            .ignoreCamera(true)
            .mount(this.objectScene);

        // Create UI elements
        new IgeFontEntity()
            .texture(ige.client.textures.font)
            .width(100)
            .text('Score')
            .top(5)
            .right(10)
            .mount(this.uiScene);

        new IgeFontEntity()
            .id('scoreText')
            .texture(ige.client.textures.font)
            .width(100)
            .text('0 potions')
            .colorOverlay('#ff6000')
            .top(35)
            .right(10)
            .mount(this.uiScene);



        // Create the main viewport and set the scene
        // it will "look" at as the new scene1 we just
        // created above
        this.vp1 = new IgeViewport()
            .id('vp1')
            .autoSize(true)
            .scene(this.mainScene)
            .drawBounds(false)
            .drawBoundsData(true)
            .mount(ige);

        //////////////////////////////////////////////////////////////////


    }
};
