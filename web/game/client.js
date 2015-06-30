var self = this,
	gameConfig = {},
	distanceR1,
	firstDist,
	limit = 1.3,
	connection;

var Client = IgeClass.extend({
	updateHandler: {},
	classId: 'Client',
	init: function () {
		self = this;
		self.myId = getCookie("playerID");
		self.playerNum = 0 ;
		self.textures = {};
		self.sounds = [] ;

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


		self.textures.soundOFF = new IgeTexture('./assets/sound_mute.png');
		self.textures.soundON = new IgeTexture('./assets/sound.png');

		//potion remove sounds
		self.sounds[0] = document.getElementById("p1");
		self.sounds[1] = document.getElementById("p2");
		self.sounds[2] = document.getElementById("p3");
		self.sounds[3]= document.getElementById("p4");


		self.sounds.winSound = document.getElementById("I_WON_SOUND");
		self.sounds.loop = document.getElementById("loop");

		self.textures[0] = new IgeTexture('./assets/water.png');
		self.textures[1] = new IgeTexture('./assets/fire.js');
		self.textures[2] = new IgeTexture('./assets/ground.png');
		self.textures[3] = new IgeTexture('./assets/wind.png');

		self.textures.font = new IgeFontSheet('./assets/agency_fb_20pt.png',3);

		self.nameFont = new IgeFontSheet('./assets/agency_fb_20pt.png', 0);
		self.potNumFont = new IgeFontSheet('./assets/agency_fb_20pt.png', 0);
		self.homePic = new IgeTexture('./assets/home.png');

		self.floorTexture = 	new IgeTexture('./assets/floor.js');

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
	self.sounds.loop.play();
	connection = initSocket();

}

function initSocket() {
	var connection = new WebSocket("ws://"+ window.location.host + "/gameServer");
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
		//console.log(snapShot.distance);
		self.updateHandler.addUpdate(snapShot);
		if (snapShot.distance != distanceR1){
			if(!this.was){
				this.was=true;
				firstDist=snapShot.distance;
			}
			//self.vp1.camera.scaleTo(0.3,0.3, 0);
			//this.zoomUot(snapShot.distance,distanceR);
			zoonOut(snapShot.distance,distanceR1);
			distanceR1 = snapShot.distance;
		}

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

function zoonOut (distanceR,distansRold) {
	if(distanceR>distansRold){
		if(distanceR/firstDist<=limit) {
			var dif = distansRold / distanceR;
			var dif1 = 1 - dif;
			console.log(dif);
			self.vp1.camera.scaleBy(-dif1, -dif1, 0);
		}
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