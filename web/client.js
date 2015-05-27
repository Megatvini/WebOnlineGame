var Client = IgeClass.extend({
	classId: 'Client',
	init: function () {
		ige.showStats(1);
		ige.globalSmoothing(true);

		// Load our textures
		var self = this;
		self.obj = {};
		self.playerNum = 0 ;
		var items = {},
			itemNum = 0,
			myIid;

		self.gameTexture = {};

		// Wait for our textures to load before continuing
		ige.addComponent(IgeBox2dComponent)
			.box2d.sleep(true)
			.box2d.gravity(0, 0)
			.box2d.createWorld()
			.box2d.start();
		self.gameTexture.simpleBox = new IgeTexture('./assets/textures/objects/cardBack.png');
		ige.on('texturesLoaded', function (){
			// Create the HTML canvas
			ige.createFrontBuffer(true);
			ige.start(function (success) {
				// Check if the engine started successfully
				function extracted() {
					var connection = new WebSocket('ws://localhost:8080/app');
					connection.onopen = function () {
						//console.log("connection opened");
						connection.send('need');

					};
					connection.onclose = function () {
						console.log('Connection closed');

					};
					connection.onerror = function (error) {
						console.log('Error detected: ' + error);
					};
					return connection;
				}

				function initial() {
					self.scene1 = new IgeScene2d()
						.id('scene2');

					// Create the main viewport
					self.vp1 = new IgeViewport()
						.id('vp1')
						.autoSize(true)
						.scene(self.scene1)
						.drawBounds(true)
						.mount(ige);

					// Create the texture maps and load their map data
					self.backgroundLayer1 = new IgeTextureMap()
						.id('backMap1')
						.depth(0)
						.tileWidth(40)
						.tileHeight(40)
						.translateTo(0, 0, 0)
						//.drawGrid(10)
						.drawBounds(false)
						.loadMap(BackgroundLayer1)
						.autoSection(20)
						.mount(self.scene1);

					self.staticObjectLayer1 = new IgeTextureMap()
						.id('backMap2')
						.depth(1)
						.tileWidth(40)
						.tileHeight(40)
						.translateTo(0, 0, 0)
						//.drawGrid(10)
						.drawBounds(false)
						.mount(self.scene1)
						.autoSection(20)
						.loadMap(StaticObjectLayer1);
				}

				if (success) {

					initial();

					var connection = extracted();
					connection.onmessage = function(e){
						var data = e.data();
						if(data.substring(0, 4)!="need"){
							var snapShot = JSON.parse(data),
								players = snapShot.players,
								points = snapShot.potions;





						}else{
							var init = data.split("#");
							myIid=Number(init[1]);
						}

					};
					function sendUpdate(){
						var t = self.obj[myIid].worldPosition();
						connection.send(t);
					}

				}
			});
		});
	}
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }