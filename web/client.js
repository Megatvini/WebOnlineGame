var Client = IgeClass.extend({
	classId: 'Client',
	init: function () {
		ige.showStats(1);
		ige.globalSmoothing(true);

		// Load our textures
		var self = this,
			characters = {}, // players in the world
			gameConfig = {}, //sizes
			items = {}, //potions
			myId, //
			player1,
			connection; // pointer to active player


		myId = getCookie("playerID");
		function getCookie(cname) {
			var name = cname + "=";
			var ca = document.cookie.split(';');
			for(var i=0; i<ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0)==' ') c = c.substring(1);
				if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
			}
			return "";
		}



		self.playerNum = 0 ;


		self.gameTexture = {};

		// Wait for our textures to load before continuing
		ige.addComponent(IgeBox2dComponent)
			.box2d.sleep(true)
			.box2d.gravity(0, 0)
			.box2d.createWorld()
			.box2d.start();

		self.gameTexture.potion = new IgeTexture('./assets/potion.png');
		self.gameTexture.wall= new IgeTexture('./assets/wall.png');




        ige.on('texturesLoaded', function (){
			// Create the HTML canvas
			ige.createFrontBuffer(true);
			ige.start(function (success) {
				// Check if the engine started successfully
				if (success) {
					myId = getCookie("playerID");
					function getCookie(cname) {
						var name = cname + "=";
						var ca = document.cookie.split(';');
						for(var i=0; i<ca.length; i++) {
							var c = ca[i];
							while (c.charAt(0)==' ') c = c.substring(1);
							if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
						}
						return "";
					}
					initial();

					var  s = {
						"planeMaze": {
							"numRows":6,
							"numCols":8,
							"walls":[


							]
						},

						"configuration":{
							"width":1136, "height":656, "wallWidth":6, "pRadius":12, "potRadius":6
						}
					};
					var c = 0 ;
					for(var t = 0 ; t < 12 ; t ++){
						for(var  p = 0 ; p < 12 ; p ++){
							s.planeMaze.walls[c]=
							{
								"cell1":{
									"row":t,
									"col":p
								},
								"cell2":{
									"row":t,
									"col":p+1
								}
							};
							s.planeMaze.walls[c+1]=
							{
								"cell1":{
									"row":t,
									"col":p
								},
								"cell2":{
									"row":t,
									"col":p-1
								}
							};
							s.planeMaze.walls[c+2]=
							{
								"cell1":{
									"row":t,
									"col":p
								},
								"cell2":{
									"row":t+1,
									"col":p
								}
							};
							s.planeMaze.walls[c+3]=
							{
								"cell1":{
									"row":t,
									"col":p
								},
								"cell2":{
									"row":t-1,
									"col":p
								}
							};
							c+=4;
						}
					}
					createGameConfig(s);
					createMaze(s);

					self.player1 = new Character()
						.mount(self.scene1);

					self.vp1.camera.lookAt(self.player1);

					// Tell the camera to track our player character with some
					// tracking smoothing (set to 20)
					self.vp1.camera.trackTranslate(self.player1, 20);
					connection = initSocket();
					connection.onmessage = function(e){
						console.log("received " + e.data);
						var snapShot = JSON.parse(e.data);
						handler(snapShot);
					};
				}
			});
		});
		function createGameConfig(snapShot) {
			gameConfig=snapShot.configuration;

		}

		/**
		 * creates maze object from JSON
		 * @param snapShot
		 */
		function createMaze(snapShot) {
			var maze = snapShot.planeMaze,

				cellHoryzSize = (gameConfig.width-(maze.numCols-1)*gameConfig.wallWidth)/maze.numCols,

				cellVertSize = (gameConfig.height-(maze.numRows-1)*gameConfig.wallWidth) /maze.numRows,
				walls = maze.walls,
				zeroX = gameConfig.width/ 2,
				zeroY = gameConfig.height/ 2,
				startX = 0,
				startY = 0;

			for(var wall1 in walls){
				if(walls.hasOwnProperty(wall1)) {
					var wall = walls[wall1];
					var col1 = wall.cell1.col;
					var row1 = wall.cell1.row;
					var col2 = wall.cell2.col;
					var row2 = wall.cell2.row;
					var drawX   ;
					var drawY  ;
					var wWidth;
					var wHeight;
					var H  = true;
					startX = (col1)*cellHoryzSize + col1*gameConfig.wallWidth;
					startY = row1*cellVertSize + row1*gameConfig.wallWidth;



					//ixateba zemot an qvemot
					if(col1==col2){
						if(row1<row2){

							// ixateba qvemot

							wHeight = gameConfig.wallWidth;
							wWidth = cellHoryzSize+gameConfig.wallWidth;
							drawY  = startY+cellVertSize+gameConfig.wallWidth/2;
							drawX = startX+wWidth/2 ;

						}else{

							//ixateba zemot

							wHeight = gameConfig.wallWidth;
							wWidth = cellHoryzSize +  gameConfig.wallWidth;;
							drawY  = startY-wHeight/2;
							drawX = startX+wWidth/2 ;
						}

					}else{///ixateba marjvniv an marcxniv
						if(col1<col2){ //marjvniv
							wHeight = cellVertSize +  gameConfig.wallWidth; ;
							wWidth = gameConfig.wallWidth;
							drawY  = startY+wHeight/2;
							drawX = startX+cellHoryzSize+wWidth/2 ;
						}
						else{ // marcxniv
							wHeight = cellVertSize + gameConfig.wallWidth;;
							wWidth = gameConfig.wallWidth;
							drawY  = startY+wHeight/2;
							drawX = startX-wWidth/2;
						}
					}
					//x,y,width,height

					new IgeEntityBox2d()
						.translateTo(drawX-zeroX, drawY-zeroY, 0)
						.width(wWidth)
						.height(wHeight)
						.texture(self.gameTexture.wall)
						.drawBounds(false)
						.mount(self.scene1)
						.box2dBody({
							type: 'static',
							allowSleep: true,
							fixtures: [{
								shape: {
									type: 'rectangle'
								}
							}]
						});



				}
//				drawOneWall(wall,cellHoryzSize,cellVertSize,)
			}


		}
		function sendUpdate(){
			var t = player1.worldPosition();
			var x = t.x;
			var y = t.y;
			connection.send(JSON.stringify({
				"type": "update",
				"name":myId,
				"coordinates":{
					"x":x,
					"y":y
				}
			}));
		}

		function initSocket() {
			var connection = new WebSocket("ws://"+ window.location.host + "/game");
			connection.onopen = function () {
				console.log("connection opened");
				connection.send(JSON.stringify({
					"type": "init",
					"name": myId
				}));


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
				.id('scene1');

			// Create the main viewport
			self.vp1 = new IgeViewport()
				.id('vp1')
				.autoSize(true)
				.scene(self.scene1)
				.drawBounds(true)
				.mount(ige);

		}
		function handler(snapShot){
			if(snapShot.type&&snapShot.type!="INIT"){
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
						console.log("key>>---"+key);
						x= Number(onePlayer.x);
						y= Number(onePlayer.y);
						id = Number(onePlayer.id);
						if(typeof (characters[id])=='undefined'){
							var newPlayer = new Character()
								.translateTo(x,y,0)
								.mount(self.scene1);
							characters[id]=newPlayer;
							if(typeof(myId)!='undefined'&& id==myId){
								newPlayer.addComponent(PlayerComponent)
									.drawBounds(false)
									.id()
									.mount(self.scene1);
								player1=newPlayer;
								self.vp1.camera.lookAt(player1);

								// Tell the camera to track our player character with some
								// tracking smoothing (set to 20)
								self.vp1.camera.trackTranslate(player1, 20);
								setInterval(sendUpdate,60);
							}

						}else{
							if(id!=myId)
								characters[id].translateTo(x,y,0);

						}


					}
				}
				////////////////////////////////////////////////////
				////POTIONS/////////////////////////////////////////
				for (var k in items) {
					if (items.hasOwnProperty(k))
						items[k].destroy();
				}

				for ( key in potions) {
					if(potions.hasOwnProperty(key)) {
						//console.log(key + ': ' + players[key]);
						onePotion = potions[key];
						x = Number(onePotion.x);
						y = Number(onePotion.y);
						id = Number(onePotion.id);
						new IgeEntity()
							.id("item" + (id))
							.texture(self.gameTexture.potion)
							.translateTo(x, y, 0)
							.mount(self.scene1);

					}
				}

			}
			if(snapShot.type&&snapShot.type=="UPDATE") {
				createGameConfig(snapShot);
				createMaze(snapShot);
			}
		}
	}

});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }