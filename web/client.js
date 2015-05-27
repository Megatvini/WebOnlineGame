var Client = IgeClass.extend({
	classId: 'Client',
	init: function () {
		ige.showStats(1);
		ige.globalSmoothing(true);

		// Load our textures
		var self = this;
		self.obj = [];
		self.playerNum = 0 ;
		var items = [],
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
				if (success) {
					// Create the scene
					self.scene1 = new IgeScene2d()
						.id('scene1');

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

					// Create a new character, add the player component
					// and then set the type (setType() is defined in
					// gameClasses/Character.js) so that the entity has
					// defined animation sequences to use.

					var connection = new WebSocket('ws://localhost:8080/app');
					connection.onopen = function(){
						//console.log("connection opened");
						connection.send('need');

					};
					connection.onclose = function(){
						console.log('Connection closed');

					};
					connection.onerror = function(error){
						console.log('Error detected: ' + error);
					};
					connection.onmessage = function(e){
						var server_message = e.data;
						//console.log(server_message);
						var res = server_message.split("#");
						if(res[0]=="init"){
							myIid=Number(res[1]);
							setInterval(sendUpdate(),30);
						}
						//0:100.0:33.0,1:12.0:33.0,#0:null,1:12.0:12.0,
						var players = res[0];
						for(var i  = 0 ; i < players.length;i++){
							var onePlayer = players[i].split(":");
							var id = Number(onePlayer[0]),
								x = Number(onePlayer[1]),
								y = Number(onePlayer[2]);

							if(typeof (self.obj[id])=='undefined'){
								self.obj[id]=new Character()
									.id(""+(id))
									.setType(id)
									.translateTo(x,y,0)
									.mount(self.scene1);
								if(id==myIid){
									self.obj[myIid]
										.addComponent(PlayerComponent)
										.drawBounds(false);
								}
							}else{
								if(id!=myIid) {
									self.obj[id].translateTo(x, y, 0);
								}
							}
						}
						/*var potions = res[1];
						for( i  = 0 ; i < potions.length;i++){

						}*/



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