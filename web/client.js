var self = this,
	characters = {}, // players in the world
	gameConfig = {},
	circles = {}, //sizes
	potions = {}, //potions
	player1,
	connection,
	debugOn,
	distanceR,
	potNum,// pointer to active player
	myId = getCookie("playerID");
var Client = IgeClass.extend({
	classId: 'Client',
	init: function () {
		self.playerNum = 0 ;
		self.gameTexture = {};
		ige.showStats(1);
		ige.globalSmoothing(true);

		// Wait for our textures to load before continuing
		ige.addComponent(IgeBox2dComponent)
			.box2d.sleep(true)
			.box2d.createWorld()
			.box2d.start();

		self.gameTexture.potion = new IgeTexture('./assets/potion.png');
		self.gameTexture.wall= new IgeTexture('./assets/wall.png');
		self.gameTexture.circle = new IgeTexture('./assets/Circle.png');
		self.gameTexture.water = new IgeTexture('./assets/water.png');
		self.gameTexture.fire = new IgeTexture('./assets/fire.png');
		self.gameTexture.ground = new IgeTexture('./assets/ground.png');
		self.gameTexture.wind = new IgeTexture('./assets/wind.png');


		ige.on('texturesLoaded', function (){
			// Create the HTML canvas
			ige.createFrontBuffer(true);
			ige.start(function (success) {
				// Check if the engine started successfully
				if (success) {
					gameOn();
				}
			});
		});


	}





});

function gameOn() {
	initial();

	var debugConfig = {
		"planeMaze": {
			"numRows": 12,
			"numCols": 12,
			"walls": []
		},

		"configuration": {
			"width": 1136, "height": 600, "wallWidth": 6, "pRadius": 12, "potRadius": 6
		}
	};

	var c = 0;
	for (var t = 0; t < 3; t++) {
		for (var p = 0; p < 4; p++) {
			debugConfig.planeMaze.walls[c] =
			{
				"cell1": {
					"row": t,
					"col": p
				},
				"cell2": {
					"row": t,
					"col": p + 1
				}
			};

			debugConfig.planeMaze.walls[c + 2] =
			{
				"cell1": {
					"row": t,
					"col": p
				},
				"cell2": {
					"row": t + 1,
					"col": p
				}
			};

		}
	}

	var debugUpdate = {
		"type": "UPDATE",
		"gameOn": true,
		"potNum": 0,
		"players": [{"active": true, "name": "debug", "position": {"x": 8.8, "y": 3.1999999999999993}}
			, {
				"active": true, "name": "room1player2",
				"position": {"x": 1000.2, "y": 3.1999999999999993}
			}],
		"potions": [{"x": 430.2331184493332, "y": 5.684165279438203},
			{"x": 0, "y": 0},
			{"x": 527.1172908746726, "y": 577.0656553648109},
			{"x": 484.1870282715431, "y": 207.55881712027767},
			{"x": 412.99717495965973, "y": 39.34350517508462},
			{"x": 1056.6955940355167, "y": 442.5149048044682},
			{"x": 832.2480951470386, "y": 543.3563451799431},
			{"x": 944.9820093407832, "y": 241.1811411431445}, {
				"x": 868.1042771720871,
				"y": 476.1570608681552
			}, {"x": 769.6752484408707, "y": 140.20459652018823}],
		"distance": 120.0
	};

	function debugGame(debugConfig, debugUpdate) {
		gameConfig = createGameConfig(debugConfig);
		createMaze(debugConfig);
		handler(debugUpdate);
	}
	potNum = new IgeFontEntity()
		.id('font1')
		.width(213)
		.height(110)
		.depth(1)
		.textAlignX(0)
		.colorOverlay('#ffffff')
		.nativeFont('26pt Arial')
		.textLineSpacing(0)
		.text('Align Left\nAnother Line\nAnd one more')
		.center(100)
		.middle(-100)
		.mount(self.scene1);
	function realGame() {

		connection = initSocket();
	}

	if (myId == 'debug') {
		debugOn = true;
		debugGame(debugConfig, debugUpdate);
	}
	else {
		debugOn=false;
		realGame();

	}

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
	connection.onmessage = function(e){
		//console.log(e.data);
		var snapShot = JSON.parse(e.data);
		handler(snapShot);


	};
	return connection;
}
function createGameConfig(snapShot) {
	/** @namespace snapShot.configuration */
	return snapShot.configuration;
}

function handler(snapShot){
	if(snapShot.type&&snapShot.type=="UPDATE"){
		/** @namespace snapShot.removePots */
		/** @namespace snapShot.addPots */
		var addPots = snapShot.addPots,
			removePots= snapShot.removePots;
		//console.log(distanceR);
		if(!snapShot.gameOn){
			ige.stop();
		}else {

			potNum.text(snapShot.potNum+"potnum");
			parsePlayers(snapShot.players);
			//parsePotions(addPots,removePots);
			if (snapShot.distance != distanceR) {
				distanceR = snapShot.distance;
				mountCircles();
			}
		}
	}
	if(snapShot.type&&snapShot.type=="INIT") {
		console.log(JSON.stringify(snapShot));
		gameConfig = createGameConfig(snapShot);
		createMaze(snapShot);
	}
}
function parsePlayers(players) {
	for (var i = 0; i < players.length; i++) {
		var onePlayer = players[i],
			name = onePlayer.name,
			position = onePlayer.position;
		if (typeof (characters[name]) == 'undefined') {
			/** @namespace gameConfig.pRadius */
			var newPlayer = characters[name] = new Character(gameConfig,name,myId,self.gameTexture,connection)
				.id(name)
				.transTo(position.x, position.y,gameConfig)
				.mount(self.scene1);
			if (name == myId) {
				player1 = newPlayer;
				player1.addComponent(PlayerComponent);
				self.vp1.camera.lookAt(self.scene1);
			}else{
				if(debugOn) {
					newPlayer.setType(1);
					var d = -1;
					characters['room1player2'].translateTo(150, characters['room1player2'].worldPosition().y, 0);
					setInterval(function () {
						distanceR++;
						mountCircles();
						var pl = characters['room1player2'],
							pos = pl.worldPosition();
						var x = pos.x;
						var y = pos.y;

						//console.log(pos.x,pos.y);
						if (x <= 0)
							d = 1;
						if (x >= 200)
							d = -1;
						pl.translateTo(x + d, y, 0);


					}, 66)
				}
			}
		} else {
			if(!onePlayer.active){
				characters[name].destroy();
			}else {
				if (name != myId) {
					characters[name].addUpdate(position);
				}
			}
		}
	}
}
function mountCircles(){
	for(var char in characters) {
		if (characters.hasOwnProperty(char)){
			if(circles[char])
				circles[char].destroy();
			var character   = characters[char];
			circles[char]=new Circle()
				.texture(self.gameTexture.circle)
				.width(distanceR)
				.height(distanceR)
				.depth(1)
				.mount(character);
		}

	}
}
function parsePotions(addPots,removePots) {
	for(var i = 0 ; i < addPots.length; i ++){
		var onePotion = addPots[i];
		var x = onePotion.x,
			y = onePotion.y,
			id= onePotion.id;
		potions[id]=new Potion()
			.texture(self.gameTexture.potion)
			.width(gameConfig.potRadius * 2)
			.height(gameConfig.potRadius * 2)
			.transTo(x, y, gameConfig)
			.mount(self.scene1);
	}
	for(i = 0 ; i < removePots.length; i ++){
		id = removePots[i].id;
		potions[id].destroy();
	}



}
function createWall(wWidth, wHeight) {
	return new IgeEntityBox2d()
		.width(wWidth)
		.height(wHeight)
		.texture(self.gameTexture.wall)
		.drawBounds(true)
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
function createFrame() {

	///marcxena
	var startCx = -gameConfig.width / 2 - gameConfig.wallWidth / 2;
	var startCy = 0;
	createWall(gameConfig.wallWidth, gameConfig.height)
		.translateTo(startCx, startCy, 0);

/////////////////////////////////////////////////////////////////////////////////////

	startCx = 0;
	startCy = -gameConfig.height / 2 - gameConfig.wallWidth / 2;

	createWall(gameConfig.width + 2 * gameConfig.wallWidth, gameConfig.wallWidth)
		.translateTo(startCx, startCy, 0);

///////////////////////////////////////////////////////////////////////////////////////
	startCx = gameConfig.width / 2 + gameConfig.wallWidth / 2;
	startCy = 0;
	createWall(gameConfig.wallWidth, gameConfig.height)
		.translateTo(startCx, startCy, 0);

//////////////////////////////////////////////////////////////////////////////////////////
	startCx = 0;
	startCy = gameConfig.height / 2 + gameConfig.wallWidth / 2;
	createWall(gameConfig.width + 2 * gameConfig.wallWidth, gameConfig.wallWidth)
		.translateTo(startCx, startCy, 0);
}
function createInnerMaze(walls, startX, cellHoryzSize, startY, cellVertSize, zeroX, zeroY) {
	for (var wall1 in walls) {
		if (walls.hasOwnProperty(wall1)) {
			var wall = walls[wall1];
			var col1 = wall.cell1.col;
			var row1 = wall.cell1.row;
			var col2 = wall.cell2.col;
			var row2 = wall.cell2.row;
			var drawX;
			var drawY;
			var wWidth;
			var wHeight;
			var H = true;
			startX = (col1) * cellHoryzSize + col1 * gameConfig.wallWidth;
			startY = row1 * cellVertSize + row1 * gameConfig.wallWidth;

			//ixateba zemot an qvemot
			if (col1 == col2) {
				if (row1 < row2) {

					// ixateba qvemot

					wHeight = gameConfig.wallWidth;
					wWidth = cellHoryzSize + gameConfig.wallWidth;
					drawY = startY + cellVertSize + gameConfig.wallWidth / 2;
					drawX = startX + wWidth / 2;

				} else {

					//ixateba zemot

					wHeight = gameConfig.wallWidth;
					wWidth = cellHoryzSize + gameConfig.wallWidth;
					;
					drawY = startY - wHeight / 2;
					drawX = startX + wWidth / 2;
				}

			} else {///ixateba marjvniv an marcxniv
				if (col1 < col2) { //marjvniv
					wHeight = cellVertSize + gameConfig.wallWidth;
					;
					wWidth = gameConfig.wallWidth;
					drawY = startY + wHeight / 2;
					drawX = startX + cellHoryzSize + wWidth / 2;
				}
				else { // marcxniv
					wHeight = cellVertSize + gameConfig.wallWidth;
					;
					wWidth = gameConfig.wallWidth;
					drawY = startY + wHeight / 2;
					drawX = startX - wWidth / 2;
				}
			}
			//x,y,width,height

			createWall(wWidth, wHeight)
				.translateTo(drawX - zeroX, drawY - zeroY, gameConfig);


		}
//				drawOneWall(wall,cellHoryzSize,cellVertSize,)
	}
}
function createMaze(snapShot) {
	var maze = snapShot.planeMaze,
		cellHoryzSize = (gameConfig.width-(maze.numCols-1)*gameConfig.wallWidth)/maze.numCols,
		cellVertSize = (gameConfig.height-(maze.numRows-1)*gameConfig.wallWidth) /maze.numRows,
		walls = maze.walls,
		zeroX = gameConfig.width/ 2,
		zeroY = gameConfig.height/ 2,
		startX = 0,
		startY = 0;
	createFrame();
	createInnerMaze(walls, startX, cellHoryzSize, startY, cellVertSize, zeroX, zeroY);


}





/**
 * creates main scene and viw point
 */
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

/**
 * gets name from cookie
 * @param cname key name
 * @returns {*} returns name of player
 */
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
if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }