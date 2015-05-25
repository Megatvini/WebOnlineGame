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
			itemNum = 0 ;

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
						var res = server_message.split(",");

						if(res[0]=="initialization"){        ///      vnaxot vin tamashobs
							//initialization,id:10:10,id:300:300
							//chavtvirtot motamasheebi
							for(var i = 1 ; i < res.length;i++){
								var object = res[i].split(":");
								self.obj[i-1]=new Character()
									.id(""+(i-1))
									.setType(i-1)
									.translateTo(Number(object[0]),Number(object[1]),0)
									.mount(self.scene1);
								self.playerNum++;
							}
							self.player1=self.obj[self.playerNum-1]
								.addComponent(PlayerComponent)
								.drawBounds(false)
								.mount(self.scene1);
							// Translate the camera to the initial player position
							self.vp1.camera.lookAt(self.player1);

							// Tell the camera to track our player character with some
							// tracking smoothing (set to 20)
							self.vp1.camera.trackTranslate(self.player1, 20);
							setInterval(sendUpdate,66);
						}
						if(res[0]=="update"){
							//console.log("U >>--" +server_message);
							for(var j = 1 ; j < res.length;j++){
								var objects = res[j].split(":");
								var id = Number(objects[0]);
								var newX = Number(objects[1]);
								var newY = Number(objects[2]);
								self.obj[id].translateTo(newX,newY,0);
							}
						}
						if(res[0]=="freshman") {
							console.log("F >>--" +server_message);
							var ob1 = res[1].split(":");
							self.obj[self.playerNum] = new Character()
								.id("" + self.playerNum)
								.setType(self.playerNum)
								.translateTo(Number(ob1[0]), Number(ob1[1]),0)
								.mount(self.scene1);
							self.playerNum++;
						}
						if(res[0]=="itemCreated"){
							console.log("before created "+items);
							console.log(server_message);
							var cos = res[1].split(":");
							items[cos[0]]= new IgeEntity()
								.id("item"+itemNum)
								.texture(self.gameTexture.simpleBox)
								.depth(1)
								.translateTo(Number(cos[1]),Number(cos[2]),0)
								.mount(self.scene1);
							console.log("after created "+items);
							itemNum++;
							// Create an entity that will follow the mouse

						}
						if(res[0]=="itemRemoved"){
							console.log("before removed "+items);
							console.log(server_message);
							cos = res[1];
							var itemId = Number(cos[0]);
							items[itemId].destroy();
							console.log("after removed "+items);
						}

					};
					function sendUpdate(){
						var t = self.player1.worldPosition();
						connection.send(t);
					}




				}
			});
		});
	}
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }