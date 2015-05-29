// Define our player Enemy classes
var Enemy = IgeEntity.extend({
    classId: 'Enemy',

    init: function () {
        var self = this;
        IgeEntity.prototype.init.call(this);

        // Setup the entity
        self.addComponent(IgeAnimationComponent)
            .addComponent(IgeVelocityComponent)
            .depth(1);

        // Load the Enemy texture file
        this._EnemyTexture = new IgeCellSheet('../assets/textures/sprites/vx_chara02_c.png', 12, 8);

        // Wait for the texture to load
        this._EnemyTexture.on('loaded', function () {
            self.texture(self._EnemyTexture)
                .dimensionsFromCell();
        }, false, true);
    },

    /**
     * Sets the type of Enemy which determines the Enemy's
     * animation sequences and appearance.
     * @param {Number} type From 0 to 7, determines the Enemy's
     * appearance.
     * @return {*}
     */
    setType: function (type) {
        switch (type) {
            case 0:
                this.animation.define('walkDown', [4, 5, 6, 5], 8, -1)
                    .animation.define('walkLeft', [16, 17, 18, 17], 8, -1)
                    .animation.define('walkRight', [28, 29, 30, 29], 8, -1)
                    .animation.define('walkUp', [40, 41, 42, 41], 8, -1)
                    .cell(4);

                this._restCell = 4;
                break;

        }

        this._EnemyType = type;

        return this;
    },

    /**
     * Tweens the Enemy to the specified world co-ordinates.
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

    tick: function (ctx) {
        // Set the depth to the y co-ordinate which basically
        // makes the entity appear further in the foreground
        // the closer they become to the bottom of the screen
        this.depth(this._translate.y);
        IgeEntity.prototype.tick.call(this, ctx);
    },

    destroy: function () {
        // Destroy the texture object
        if (this._EnemyTexture) {
            this._EnemyTexture.destroy();
        }

        // Call the super class
        IgeEntity.prototype.destroy.call(this);
    }
});

if (typeof(module) !== 'undefined' && typeof(module.exports) !== 'undefined') { module.exports = Enemy; }