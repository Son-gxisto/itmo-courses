const { getEmitter } = require('./emitter');

let students = {
    Sam: {
        focus: 100,
        wisdom: 50
    }
};

let lecturer = getEmitter();

// С началом лекции у всех резко повышаются показатели
lecturer.on('begin', students.Sam, function () {
        this.focus += 10;
        this.wisdom += 1;
    })
lecturer.on('a', students.Sam, function() {
    this.wisdom += 20;
})
lecturer.emit('begin')
lecturer.emit('begin')
lecturer.emit('a')
lecturer.emit('a')