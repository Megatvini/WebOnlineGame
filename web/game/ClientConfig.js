var igeClientConfig = {
	include: [
		/* Your custom game JS scripts */
		'./gameClasses/UI.js',
		'./gameClasses/ClientWorld.js',
		'./gameClasses/Character.js',
		'./gameClasses/PlayerComponent.js',
		'./gameClasses/Wall.js',
		'./gameClasses/Potion.js',
		'./gameClasses/Circle.js',
		'./gameClasses/Handler.js',
		'./gameClasses/Map.js',
		'./gameClasses/Maze.js',
		/* Standard game scripts */
		'./client.js',
		'./index.js'
	]
};

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = igeClientConfig; }