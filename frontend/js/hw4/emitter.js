const debug = false;
function log(...args) {
    if (debug) {
        console.info(...args);
    }
}
class Event {
    constructor(context, handler, ...args) {
        this.context = context;
        this.handler = handler;
        this.type = "def";
        this.n = 0;
        if (args.length > 0) {
            this.n = args[0];
            this.type = 'count';
            if (this.n < 1) {
                this.type = "def";
            }
        }

        if (args.length > 1) {
            this.type = 'nth';
            this.counter = 0;
            if (this.n < 1) {
                this.type = "def";
            }
        }
    }
    do() {
        this.handler.call(this.context);
    }
    emit() {
        //TODO: emit некорректный для доп. заданий
        if (this.type === 'count') {
            if (this.n > 0) {
                    this.do();
                    this.n--;
            }
        } else if (this.type ==='nth') {
            if (this.counter % this.n === 0) {
                this.do();
            }
            this.counter++;
        } else {
            this.do();
        }
    }
}
class EventManager {
    constructor(name) {
        this.name = name;
        this.events = new Set();
    }
    add(context, handler, ...args) {
        this.events.add(new Event(context, handler, ...args))
    }
    emit() {
        this.events.forEach((it) => {
            it.emit();
        })
    }
    del(context) {
        this.events.forEach((it) => {
           if (it.context === context) {
               this.events.delete(it);
           }
        })
    }
}
/**
 * Возвращает новый emitter
 * @returns {Object}
 */
function getEmitter() {
    let events = new Map()
    function has(event) {
        return events.has(event);
    }
    function child(a, b) {
        let ara = a.split('.');
        let arb = b.split('.');
        if (ara.length > arb.length) {
            return false;
        }
        for (let i = 0; i < ara.length; i++) {
            if (ara[i] !== arb[i]) {
                return false;
            }
        }
        return true;
    }
    return {
        /**
         * Подписаться на событие
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         */
        on: function (event, context, handler) {
            log(event);
            if (!has(event)) {
                events.set(event, new EventManager(event));
            }
            events.get(event).add(context, handler);
            return this;
        },
        /**
         * Отписаться от события
         * @param {String} event
         * @param {Object} context
         */
        off: function (event, context) {
            log('Отписан от ', event, context);
            if (has(event)) {
                events.forEach((value, key) => {
                    if (child(event, key)) {
                        events.get(key).del(context)
                    }
                })
            }
            return this;
        },

        /**
         * Уведомить о событии
         * @param {String} event
         */
        emit: function (event) {
            let cur = event + '.';
            log('Вызвано ', event)
            while (true) {
                let last = cur.lastIndexOf('.');
                if (last === -1) {
                    break;
                }
                cur = cur.slice(0, last);
                log('Произошло событие', cur);
                if (has(cur)) {
                    events.get(cur).emit()
                    log(events.get(cur).events);
                }
            }
            return this;
        },

        /**
         * Подписаться на событие с ограничением по количеству полученных уведомлений
         * @star
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         * @param {Number} times – сколько раз получить уведомление
         */
        several: function (event, context, handler, times) {
            log(event);
            if (!has(event)) {
                events.set(event, new EventManager(event));
            }
            events.get(event).add(context, handler, times);
            return this;
        },

        /**
         * Подписаться на событие с ограничением по частоте получения уведомлений
         * @star
         * @param {String} event
         * @param {Object} context
         * @param {Function} handler
         * @param {Number} frequency – как часто уведомлять
         */
        through: function (event, context, handler, frequency) {
            log(event);
            if (!has(event)) {
                events.set(event, new EventManager(event));
            }
            events.get(event).add(context, handler, frequency, 'nth');
            return this;
        }
    };
}

module.exports = {
    getEmitter
};
