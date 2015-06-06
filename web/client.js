var self = this,
	characters = {}, // players in the world
	gameConfig = {}, //sizes
	items = [], //potions
	player1,
	connection,
	distance, // pointer to active player
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
					gameOn(connection);
				}
			});
		});


	}





});

function gameOn(){
	initial();
	/*
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
	 self.vp1.camera.trackTranslate(self.player1, 20);*/
	//connection = initSocket();
	connection = new WebSocket("ws://"+ window.location.host + "/game");
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
		console.log(e.data);
		var snapShot = JSON.parse(e.data);
		handler(snapShot);


	};
}
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
function createGameConfig(snapShot) {
	/** @namespace snapShot.configuration */
	return snapShot.configuration;
}

function handler(snapShot){
	if(snapShot.type&&snapShot.type=="UPDATE"){
		var players = snapShot.players,
			potions = snapShot.potions;
		var ss = {"type":"UPDATE","gameOn":true,"potNum":0,"players":[{"active":true,"name":"room1player1","position":{"x":8.8,"y":-3.1999999999999993}},{"active":true,"name":"room1player2","position":{"x":1103.2,"y":-3.1999999999999993}}],"potions":[{"x":430.2331184493332,"y":5.684165279438203},{"x":1045.6141967022984,"y":106.58223464093126},{"x":527.1172908746726,"y":577.0656553648109},{"x":484.1870282715431,"y":207.55881712027767},{"x":412.99717495965973,"y":39.34350517508462},{"x":1056.6955940355167,"y":442.5149048044682},{"x":832.2480951470386,"y":543.3563451799431},{"x":944.9820093407832,"y":241.1811411431445},{"x":868.1042771720871,"y":476.1570608681552},{"x":769.6752484408707,"y":140.20459652018823}],"distance":120.0}
		parsePlayers(players);
		parsePotions(potions);

	}
	if(snapShot.type&&snapShot.type=="INIT") {
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
			var newPlayer = characters[name] = new Character(gameConfig,name,myId)
				.id(name)
				.translateTo(position.x - gameConfig.width/2+gameConfig.pRadius/2,
				position.y-gameConfig.height/2+gameConfig.pRadius, 0)
				.mount(self.scene1);
			if (name == myId) {

				self.player1 = newPlayer;
				self.player1
					.addComponent(PlayerComponent)
					.mount(self.scene1);
				setInterval(sendUpdate,60);
				self.vp1.camera.lookAt(self.player1);
				self.vp1.camera.trackTranslate(self.player1, 20);

			}
		} else {
			if (name != myId)
				characters[name].translateTo(position.x, position.y,0);
		}
	}
}
function parsePotions(potions) {
	for (var k  = 0 ; k < items.length ; k ++ ) {
		if(items[k]!=null) {
			items[k].destroy();
			items[k] = null;
		}
	}
	for (var j = 0 ; j < potions.length ; j ++) {
		//console.log(key + ': ' + players[key]);
		var onePotion = potions[j],
			x = onePotion.x,
			y = onePotion.y;
		new IgeEntity()
			//.id("item" + (j))
			.texture(self.gameTexture.potion)
			.translateTo(x, y, 0)
			.mount(self.scene1);

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
if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Client; }