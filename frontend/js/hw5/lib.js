'use strict';

const friendComparator = (a, b) => a.name.localeCompare(b.name)

/**
 * Итератор по друзьям
 * @constructor
 * @param {Object[]} friends
 * @param {Filter} filter
 */
function Iterator(friends, filter) {
    if (!(filter instanceof Filter)) {
        throw new TypeError('wrong Filter');
    }
    if (this.maxLvl === undefined) {
        this.maxLvl = Infinity;
    }
    let res = [];
    let used = new Set();
    let sorted = new Set(friends.sort(friendComparator));
    let current = friends.filter(friend => friend.best);
    for (let lvl = this.maxLvl; lvl > 0 && friends.length > res.length && current.length > 0; lvl--) {
        res.push(...current);
        current.forEach(cur => used.add(cur));
        let nextNames = new Set();
        current.forEach(cur => { if (cur.friends !== undefined) { cur.friends.forEach(f => nextNames.add(f)) }});
        current = [];
        sorted.forEach(friend => {
            if (nextNames.has(friend.name) && !used.has(friend)) {
                current.push(friend);
                sorted.delete(friend);
            }
        });
        current.sort(friendComparator);
    }
    this.guests = res.filter(filter.filter);
}
Iterator.prototype.done = function() {
    return !this.guests.length;
}
Iterator.prototype.next = function() {
    if (!this.done()) {
        return this.guests.shift();
    }
    return null;
}

/**
 * Итератор по друзям с ограничением по кругу
 * @extends Iterator
 * @constructor
 * @param {Object[]} friends
 * @param {Filter} filter
 * @param {Number} maxLevel – максимальный круг друзей
 */
function LimitedIterator(friends, filter, maxLevel) {
    this.maxLvl = maxLevel;
    Iterator.call(this, friends, filter);
}
Object.setPrototypeOf(LimitedIterator.prototype, Iterator.prototype);

/**
 * Фильтр друзей
 * @constructor
 */
function Filter() {
}
Filter.prototype.filter = function () {
    return true;
}

/**
 * Фильтр друзей
 * @extends Filter
 * @constructor
 */
function MaleFilter() {
    Filter.call(this);
}
Object.setPrototypeOf(MaleFilter.prototype, Filter.prototype);
MaleFilter.prototype.filter = function(friend) {
    return friend.gender === 'male';
}

/**
 * Фильтр друзей-девушек
 * @extends Filter
 * @constructor
 */
function FemaleFilter() {
    Filter.call(this);
}
Object.setPrototypeOf(FemaleFilter.prototype, Filter.prototype);
FemaleFilter.prototype.filter = function(friend) {
    return friend.gender === 'female';
}

exports.Iterator = Iterator;
exports.LimitedIterator = LimitedIterator;

exports.Filter = Filter;
exports.MaleFilter = MaleFilter;
exports.FemaleFilter = FemaleFilter;