// Define our player character classes
var Character = IgeEntityBox2d.extend({
	classId: 'Character',
	textures: {} ,
	config : {},
	myId: '',
	connection : {},
	isMainCharacter : false ,
	lastUpdate:  null,
	lastUpdate2:  null,
	interpolation : 80 ,
	extrapolation: 100,

	init: function (data,name,myId,gametexture,connection,position) {
		this.updateBuffer  = [],
		//console.log(JSON.stringify(this.lastUpdate));
		this.isMainCharacter = name==myId;
		this.connection=connection;
		this.config = data;
		this.myId=name;
		var self = this;
		this.textures = gametexture;
		IgeEntityBox2d.prototype.init.call(this);
		// Setup the entity
		self.addComponent(IgeAnimationComponent)
			.addComponent(IgeVelocityComponent);
		self.width(data.pRadius * 2)
			.height(data.pRadius * 2)
		if(this.isMainCharacter) {
			self.box2dBody({
				type: 'dynamic',
				linearDamping: 0.0,
				angularDamping: 0.1,
				allowSleep: true,
				bullet: false,
				gravitic: false,
				fixedRotation: true,
				fixtures: [{
					density: 1.0,
					friction: 0.5,
					restitution: 0.2,
					shape: {
						type: 'circle',
						data: {
							// The position of the fixture relative to the body
							x: 0,
							y: 0
						}
					}
				}]
			})
		}
	},

	/**
	 * Sets the type of character which determines the character's
	 * animation sequences and appearance.
	 * @param {Number} type From 0 to 7, determines the character's
	 * appearance.
	 * @return {*}
	 */
	setType: function (type) {
		var self ;
		switch (type) {
			case 0:
				self = this ;
				this._characterTexture = this.textures.fire;

				self.texture(self._characterTexture);

				this.animation.define('walkDown', [1], 8, -1)
					.animation.define('walkLeft', [1], 8, -1)
					.animation.define('walkRight', [1], 8, -1)
					.animation.define('walkUp', [1], 8, -1)
					.cell(1);

				this._restCell = 1;
				break;

			case 1:
				self = this ;
				this._characterTexture = this.textures.water;
				self.texture(self._characterTexture);

				this.animation.define('walkDown', [1], 8, -1)
					.animation.define('walkLeft', [1], 8, -1)
					.animation.define('walkRight', [1], 8, -1)
					.animation.define('walkUp', [1], 8, -1)
					.cell(1);

				this._restCell = 4;
				break;

			case 2:
				self = this ;
				this._characterTexture = this.textures.wind;

				// Wait for the texture to load
				self.texture(self._characterTexture);
				this.animation.define('walkDown', [1], 8, -1)
					.animation.define('walkLeft', [1], 8, -1)
					.animation.define('walkRight', [1], 8, -1)
					.animation.define('walkUp', [1], 8, -1)
					.cell(1);

				this._restCell = 1;
				break;

			case 3:
				self = this ;
				this._characterTexture = this.textures.ground;

				// Wait for the texture to load

				self.texture(self._characterTexture);

				this.animation.define('walkDown', [1], 8, -1)
					.animation.define('walkLeft', [1], 8, -1)
					.animation.define('walkRight', [1], 8, -1)
					.animation.define('walkUp', [1], 8, -1)
					.cell(1);

				this._restCell = 1;
				break;


		}

		this._characterType = type;

		return this;
	},

	/**
	 * Tweens the character to the specified world co-ordinates.
	 * @param x
	 * @param y
	 * @return {*}
	 */
	walkTo: function (x, y) {
		var self = this,
			distX = x - this.translate().x(),
			distY = y - this.translate().y(),
			distance = Math.distance(
				this.translate().x(),
				this.translate().y(),
				x,
				y
			),
			speed = 0.1,
			time = (distance / speed);

		// Set the animation based on direction
		if (Math.abs(distX) > Math.abs(distY)) {
			// Moving horizontal
			if (distX < 0) {
				// Moving left
				this.animation.select('walkLeft');
			} else {
				// Moving right
				this.animation.select('walkRight');
			}
		} else {
			// Moving vertical
			if (distY < 0) {
				// Moving up
				this.animation.select('walkUp');
			} else {
				// Moving down
				this.animation.select('walkDown');
			}
		}

		// Start tweening the little person to their destination
		this._translate.tween()
			.stopAll()
			.properties({x: x, y: y})
			.duration(time)
			.afterTween(function () {
				self.animation.stop();
				// And you could make him reset back
				// to his original animation frame with:
				//self.cell(10);
			})
			.start();

		return this;
	},

	update: function (ctx) {
		// Set the depth to the y co-ordinate which basically
		// makes the entity appear further in the foreground
		// the closer they become to the bottom of the screen
		//this.depth(this._translate.y);
		if(this.located) {
			if (!this.isMainCharacter) {
				var update = this.getNextUpdate();
				if (update != null) {
					this.transTo(update.x, update.y);
				}
			}
		}
		IgeEntityBox2d.prototype.update.call(this, ctx);
	},

	destroy: function () {
		// Destroy the texture object
		if (this._characterTexture) {
			this._characterTexture.destroy();
		}

		// Call the super class
		IgeEntityBox2d.prototype.destroy.call(this);
	},
	transTo: function (x,y){
		var config = this.config;
		var x1 = x - config.width / 2 + config.pRadius;
		var y1 = y - config.height / 2 + config.pRadius;
		if(!this.isMainCharacter&&this.located) {

			this._translate.x=x1;
			this._translate.y=y1;

			//console.log(this.myId+" x "+this._translate.x+"  y "+this._translate.y);
			/*this._translate.x = x1;
			 this._translate.y = y1;*/
		}
		else{
			this.translateTo(x1,y1,0);
			this.located=true;
		}
		return this ;
	}
	,
	modelPos: function(){
		var config = this.config;
		var pos = this.worldPosition();
		var x1 = pos.x,//coordinates in engine system
			y1 = pos.y,//coordinates in engine system
			x= x1+config.width/2-config.pRadius,//coordinates in model system
			y= y1+config.height/2-config.pRadius; //coordinates in model system

		return {
			x: x,
			y: y
		}


	},

	/**
	 *Sends update to server. coordinates are in MODEL system
	 */
	sendUpdate: function(){
		var pos = this.modelPos();
		this.connection.send(JSON.stringify({
			"type": "update",
			"name":this.myId,
			"coordinates":{
				"x":pos.x,
				"y":pos.y
			}
		}));
	},
	getNextUpdate: function(){
		var y;
		var x;
		var currentTime = new Date().getTime();
		var lastUpdate = this.lastUpdate;
		var lastUpdate2 = this.lastUpdate2;
		if (this.updateBuffer.length != 0) {
			var update1 = JSON.parse(JSON.stringify(this.updateBuffer[0]));
			var differense = currentTime - update1.date;
			var update = update1.snapShot;
			//console.log("bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
			if (differense >= this.interpolation) {
				this.lastUpdate2= JSON.parse(JSON.stringify(this.lastUpdate));
				this.lastUpdate=update;
				this.updateBuffer.shift();
				//console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense);
				return update
			} else {
				if(lastUpdate!=null) {
					x = (lastUpdate.x+ (update.x - lastUpdate.x) / 2);
					y = (lastUpdate.y+ (update.y - lastUpdate.y) / 2);
					//console.log(" bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y);
					return {x: x, y: y};
				}
			}
		}
		else{
			if(lastUpdate2!=null&&lastUpdate!=null) {
				x = (lastUpdate.x+(lastUpdate.x - lastUpdate2.x) / 2);
				y = (lastUpdate.y+(lastUpdate.y - lastUpdate2.y) / 2);
				//console.log(" $ no buffer $ bufferSize  " +this.updateBuffer.length+ " diff  " +differense+ " x " + x + " y "+ y)
				return {x: x, y: y};
			}
		}
		return null;


	},
	addUpdate: function(data){
		//console.log(this.myId+" "+data.x+"  "+data.y)
		var d = new Date().getTime();
		var update = {
			date:d,
			snapShot:data
		};
		this.updateBuffer.push(update);

	}



});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Character; }