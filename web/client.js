var Client = IgeClass.extend({
	classId: 'Client',
	init: function () {
		ige.showStats(1);
		ige.globalSmoothing(true);

		// Load our textures
		var self = this,
			characters = {};

		self.playerNum = 0 ;
		var items = {},
			itemNum = 0,
			myIid,
			player1;

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
				function initSocket() {
					var connection = new WebSocket('ws://192.168.78.111:8080/app');
					connection.onopen = function () {
						console.log("connection opened");


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

					var connection = initSocket();
					connection.onmessage = function(e){
						var data = e.data;
						if(data.substring(0, 4)!="init"){
							var snapShot = JSON.parse(data);
							var players = snapShot.players,
								potions = snapShot.potions,
								onePotion,
								x,
								y,
								id,
								onePlayer ;
							//{"players":{"0":{"x":100.0,"y":33.0,"id":0},"1":{"x":12.0,"y":33.0,"id":1}},"potions":{"0":{"x":-1000.0,"y":-1000.0,"id":-1},"1":{"x":12.0,"y":12.0,"id":1}}}

							for (var key in players) {
								if(players.hasOwnProperty(key)) {
									//console.log(key + ': ' + players[key]);
									onePlayer = players[key];
									x= Number(onePlayer.x);
									y= Number(onePlayer.y);
									id = Number(onePlayer.id);
									if(typeof (characters[id])=='undefined'){
										var newPlayer = new Character()
											.id("player"+(id))
											.setType(id)
											.translateTo(x,y,0)
											.mount(self.scene1);
										characters[id]=newPlayer;
										if(typeof(myIid)!='undefined'&& id==myIid){
											newPlayer.addComponent(PlayerComponent)
												.drawBounds(false)
												.mount(self.scene1);
											player1=newPlayer;
											self.vp1.camera.lookAt(player1);

											// Tell the camera to track our player character with some
											// tracking smoothing (set to 20)
											self.vp1.camera.trackTranslate(player1, 20);
											setInterval(sendUpdate,16);
										}

									}else{
										if(id!=myIid)
											characters[id].translateTo(x,y,0);

									}


								}
							}
							////////////////////////////////////////////////////
							////POTIONS/////////////////////////////////////////
							for ( key in potions) {
								if(potions.hasOwnProperty(key)) {
									//console.log(key + ': ' + players[key]);
									onePotion = potions[key];
									x= Number(onePotion.x);
									y= Number(onePotion.y);
									id = Number(onePotion.id);
									if(typeof (items[id])=='undefined'){
										items[id]=new IgeEntity()
											.id("item" + (id))
											.texture(self.gameTexture.simpleBox)
											.depth(1)
											.translateTo(x, y, 0)
											.mount(self.scene1);
									}
									else{
										//console.log(x+" <> "+id);
										if(x==-10000)
											items[id].destroy();

									}
								}
							}

						}else{
							var init = data.split("#");
							myIid=Number(init[1]);
						}

					};
					function sendUpdate(){
						var t = player1.worldPosition();
						connection.send(t);
					}

				}
			});
		});
	}
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }