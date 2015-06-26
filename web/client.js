var self = this,
	gameConfig = {},
	connection;

var Client = IgeClass.extend({
	updateHandler: {},
	classId: 'Client',
	init: function () {
		self = this;
		self.myId = getCookie("playerID");
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

		self.textures[0] = new IgeTexture('./assets/water.png');
		self.textures[1] = new IgeTexture('./assets/fire.js');
		self.textures[2] = new IgeTexture('./assets/ground.png');
		self.textures[3] = new IgeTexture('./assets/wind.png');

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
			"name": self.myId
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


/**
 * Update play maker
 * @param snapShot
 */
function handler(snapShot){

	var pots = snapShot.potions;

	if(snapShot.type&&snapShot.type=="UPDATE"){
		self.updateHandler.addUpdate(snapShot);


	}
	if(snapShot.type&&snapShot.type=="INIT") {
		console.log(JSON.stringify(snapShot));
		self.gameConfig = gameConfig =  createGameConfig(snapShot);
		self.playerTypes=snapShot.playerTypes;
		self.UI.addPlayers(self.playerTypes);
		new Maze(snapShot,self,self.gameConfig).createMaze();
		self.updateHandler.parsePotions(pots,[]);

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