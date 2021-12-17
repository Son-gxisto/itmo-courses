'use strict';

const robbery = require('./robbery');
const gangSchedule = {
    Danny: [{ from: 'ПН 00:00+5', to: 'СР 23:58+5' }],
    Rusty: [{ from: 'ПН 00:00+5', to: 'СР 23:58+5' }],
    Linus: [{ from: 'ПН 00:00+5', to: 'СР 23:58+5' }]
};

const bankWorkingHours = {
    from: '00:00+5',
    to: '23:59+5'
};

// Время не существует
const longMoment = robbery.getAppropriateMoment(gangSchedule, 1, bankWorkingHours);

// Выведется `false` и `""`
console.info(longMoment.exists());
console.info(longMoment.format('Метим на %DD, старт в %HH:%MM!'));

// Время существует
const moment = robbery.getAppropriateMoment(gangSchedule, 0, bankWorkingHours);

// Выведется `true` и `"Метим на ВТ, старт в 11:30!"`
console.info(moment.exists());
console.info(moment.format('Метим на %DD, старт в %HH:%MM!'));

if (robbery.isExtraTaskSolved) {
    // Вернет `true`
    moment.tryLater();
    // `"ВТ 16:00"`
    console.info(moment.format('%DD %HH:%MM'));

    // Вернет `true`
    moment.tryLater();
    // `"ВТ 16:30"`
    console.info(moment.format('%DD %HH:%MM'));

    // Вернет `true`
    moment.tryLater();
    // `"СР 10:00"`
    console.info(moment.format('%DD %HH:%MM'));

    // Вернет `false`
    moment.tryLater();
    // `"СР 10:00"`
    console.info(moment.format('%DD %HH:%MM'));
}