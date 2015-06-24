var self = this,
	characters = {}, // players in the world
	gameConfig = {},
	circles = {}, //sizes
	potions = {}, //potions
	player1,
	connection,
	distanceR,

	playerTypes = {} ;
myId = getCookie("playerID");
var Client = IgeClass.extend({
	updateHandler: {},
	classId: 'Client',
	init: function () {
		self = this  ;
		self.playerNum = 0 ;
		self.textures = {};
		ige.showStats(1);
		ige.globalSmoothing(true);
		self.implement(ClientWorld);
		// Wait for our textures to load before continuing
		ige.addComponent(IgeBox2dComponent)
			.box2d.sleep(true)
			.box2d.createWorld()
			.box2d.start();

		self.textures.potion = new IgeTexture('./assets/potion.png');
		self.textures.wall= new IgeTexture('./assets/wall.js');
		self.textures.circle = new IgeTexture('./assets/Circle.js');
		self.textures.water = new IgeTexture('./assets/water.png');
		self.textures.fire = new IgeTexture('./assets/fire.png');
		self.textures.ground = new IgeTexture('./assets/ground.png');
		self.textures.wind = new IgeTexture('./assets/wind.png');
		self.textures.font = new IgeFontSheet('./assets/agency_fb_20pt.png',3);


		ige.on('texturesLoaded', function (){
			// Create the HTML canvas
			ige.createFrontBuffer(true);
			ige.start(function (success) {
				// Check if the engine started successfully
				if (success) {
					self.createWorld();
					self.UI = 	new UI();
					self.UI.createBackScene();
					self.updateHandler = new Handler();
					gameOn();
				}
			});
		});


	}





});

function gameOn() {

		connection = initSocket();

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


function endGame() {
	ige.client.objectScene.destroy();
	//self.textures.wall.destroy();
	//ige.stop();
	ige.isOFF = true;
	//alert("GAME OVER");
}


function showGameStats(results) {

	self.UI.createStatscene(results)

}
function handler(snapShot){

	var addPots = snapShot.potions;

	if(snapShot.type&&snapShot.type=="UPDATE"){
		/** @namespace snapShot.removePots */
		/** @namespace snapShot.addPots */
		addPots = snapShot.addPots ;
		var removePots= snapShot.removePots;
		//console.log(distanceR);
		/** @namespace snapShot.finished */
		if(snapShot.finished){
			if(!ige.isOFF) {
				showGameStats(snapShot.results);
				setTimeout(function() {
					endGame();
				}, 10)
			}

		}else {

			/** @namespace snapShot.potNum */
			ige.$('scoreText').text(snapShot.potNum+' potions');
			/** @namespace snapShot.players */
			parsePlayers(snapShot.players);
			parsePotions(addPots,removePots);
			if (snapShot.distance != distanceR) {
				distanceR = snapShot.distance;
				mountCircles();
			}
		}
	}
	if(snapShot.type&&snapShot.type=="INIT") {
		console.log(JSON.stringify(snapShot));
		self.gameConfig = gameConfig =  createGameConfig(snapShot);
		self.playerTypes=snapShot.playerTypes;
		new Maze(snapShot,self,self.gameConfig).createMaze();

	}
}
function parsePlayers(players) {
	for (var i = 0; i < players.length; i++) {
		var onePlayer = players[i],
			name = onePlayer.name,
			position = onePlayer.position;
		if (typeof (characters[name]) == 'undefined') {
			/** @namespace gameConfig.pRadius */
			var newPlayer = characters[name] = new Character(gameConfig,name,myId,self.textures,connection,position)
				.id(name)
				.transTo(position.x, position.y)
				.setType(self.playerTypes[name])
				.mount(self.objectScene)
				.depth(2);
			if (name == myId) {
				player1 = newPlayer;
				player1.addComponent(PlayerComponent);
			}
		} else {
			if(!onePlayer.active){
				if(!characters[name].destroed) {
					characters[name].destroy();
					characters[name].destroed=true;
				}
			}else {
				if (name != myId) {
					characters[name]. /*transTo(position.x, position.y);*/
						addUpdate(position);
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
			circles[char]=new Circle(distanceR,self.gameConfig.pRadius)
				.mount(character)
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
			.texture(self.textures.potion)
			.width(gameConfig.potRadius * 2)
			.height(gameConfig.potRadius * 2)
			.transTo(x, y, gameConfig)
			.mount(self.objectScene);
	}
	for(i = 0 ; i < removePots.length; i ++){
		//console.log(removePots[i].id);
		id = removePots[i];
		potions[id].destroy();
	}

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