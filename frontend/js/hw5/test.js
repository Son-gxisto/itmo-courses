const assert = require('assert');
const lib = require('./lib');
const marriage = require('./marriage');

const friends1 = [
    {
        name: 'A',
        gender: 'male',
        friends: ['D'],
        best: true
    },
    {
        name: 'B',
        gender: 'female',
        best: true
    },
    {
        name: 'D',
        friends: ['E', 'A'],
        gender: 'female'
    },
    {
        name: 'E',
        friends: ['D', "M1"],
        gender: 'female'
    },
    {
        name: 'M1',
        friends: ['E', 'M2'],
        gender: 'male'
    },
    {
        name: 'M2',
        friends: ['M1'],
        gender: 'male'
    }
];

const friends2 = [
    {
        name: 'A',
        gender: 'female',
        friends: ['M1'],
        best: true
    },
    {
        name: 'M1',
        friends: ['A', 'M2'],
        gender: 'female'
    },
    {
        name: 'M2',
        friends: ['M1', 'M3'],
        gender: 'female'
    },
    {
        name :'M3',
        friends: ['M2'],
        gender: 'male'
    }
];
const friends3 = [];

let friends = friends1;

function friend(name) {
    let len = friends.length;

    while (len--) {
        if (friends[len].name === name) {
            return friends[len];
        }
    }
}
const size = 4;
const maleFilter = new lib.MaleFilter();
const femaleFilter = new lib.FemaleFilter();
const maleIterator = new lib.LimitedIterator(friends, maleFilter, size);
const femaleIterator = new lib.Iterator(friends, femaleFilter);

const maRmaleFilter = new marriage.MaleFilter();
const maRfemaleFilter = new marriage.FemaleFilter();
const maRmaleIterator = new marriage.LimitedIterator(friends, maRmaleFilter, size);
const maRfemaleIterator = new marriage.Iterator(friends, maRfemaleFilter);

const invitedFriends = [];
const maRIF = [];

const allf = new lib.Filter();
const marf = new marriage.Filter();
const alli = new lib.Iterator(friends, allf);
const mari = new marriage.Iterator(friends, marf);
const allinv = [];
const marinv = [];

while (!alli.done()) {
    allinv.push(alli.next());
}
while (!mari.done()) {
    marinv.push(mari.next());
}
assert.deepStrictEqual(allinv, marinv);

while (!maleIterator.done() && !femaleIterator.done()) {
    invitedFriends.push([
        maleIterator.next(),
        femaleIterator.next()
    ]);
}
while (!maRmaleIterator.done() && !maRfemaleIterator.done()) {
    maRIF.push([
        maRmaleIterator.next(),
        maRfemaleIterator.next()
    ]);
}

while (!femaleIterator.done()) {
    invitedFriends.push(femaleIterator.next());
}

while (!maRfemaleIterator.done()) {
    maRIF.push(maRfemaleIterator.next());
}

assert.deepStrictEqual(invitedFriends, maRIF);
console.info(invitedFriends);