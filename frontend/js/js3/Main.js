'use strict';
//0 - 00:00 ПН
const debug = false;
const day = 24 * 60;
let bankP = 0;
let psum = 0;

class Section {
    constructor(...args) {
        if (args.length === 1) {
            this.l = timeToMinute(args[0].from);
            this.r = timeToMinute(args[0].to);
        } else {
            this.l = args[0];
            this.r = args[1];
        }
    }

    static intersect(a, b) {
        return a.l < b.r && b.l < a.r || a.l === b.l || a.r === b.r;
    }

    contains(b) {
        return this.l <= b.l && b.r <= this.r;
    }
}

function weekDay(d) {
    switch (d) {
        case "ПН":
            return 0;
        case "ВТ":
            return day;
        case "СР":
            return day * 2;
    }
}

function timeToMinute(time) {
    return weekDay(time.substr(0, 2)) +
        Number.parseInt(time.substr(3, 2)) * 60 +
        Number.parseInt(time.substr(6)) -
        Number.parseInt(time.substr(9)) * 60 + psum;
}

function timeToMinuteBank(time) {
    return Number.parseInt(time.substr(0, 2)) * 60 +
        Number.parseInt(time.substr(3)) -
        Number.parseInt(time.substr(6)) * 60 + psum;
}

function parsePerson(res, person) {
    if (person !== undefined) {
        person.forEach(i => {
            res.push(new Section(i));
        })
    }
}

/**
 * @param {Object} schedule Расписание Банды
 * @param {number} duration Время на ограбление в минутах
 * @param {Object} workingHours Время работы банка
 * @param {string} workingHours.from Время открытия, например, "10:00+5"
 * @param {string} workingHours.to Время закрытия, например, "18:00+5"
 * @returns {Object}
 */
function getAppropriateMoment(schedule, duration, workingHours) {
    let res = {
        HH: 0,
        DD: 0,
        MM: 0
    }
    let resNum = 0;
    let exist = false;
    let nonSect = [];
    bankP = Number.parseInt(workingHours.from.substr(6));
    psum = bankP * 60
    let bankSect = [new Section(timeToMinuteBank(workingHours.from), timeToMinuteBank(workingHours.to))];
    //Hack for ВТ and СР
    for (let i = 1; i < 3; i++) {
        bankSect.push(new Section(bankSect[0].l + day * i, bankSect[0].r + day * i));
    }
    // Parsing section
    parsePerson(nonSect, schedule.Danny);
    parsePerson(nonSect, schedule.Rusty);
    parsePerson(nonSect, schedule.Linus);
    function check(i) {
        let cur = new Section(i, i + duration);
        let curRes = false;
        for (let s of bankSect) {
            if (s.contains(cur)) {
                curRes = true;
                break;
            }
        }
        if (!curRes) {
            return false;
        }
        for (let s of nonSect) {
            if (Section.intersect(s, cur)) {
                curRes = false;
                break;
            }
        }
        if (!curRes) {
            return false;
        }
        if (curRes) {
            exist = curRes;
            resNum = i;
            getDDMMHH(cur.l);
            return true;
        }
    }
    // search intersect
    for (let i = bankSect[0].l; i < bankSect[2].r; i++) {
        if (check(i)) {
            break;
        }
    }
    function append0(num) {
        if (num < 10) {
            return '0' + num.toString();
        } else {
            return num.toString();
        }
    }
    function getDDMMHH(num_) {
        let num = num_ - psum + bankP * 60;
        res.HH = append0(Math.floor((num % day) / 60));
        res.MM = append0(num % day % 60);
        switch(Math.floor(num / day)) {
            case 0:
                res.DD = 'ПН';
                break;
            case 1:
                res.DD = 'ВТ';
                break;
            case 2:
                res.DD = 'СР';
                break;
        }
    }

    return {
        /**
         * Найдено ли время
         * @returns {boolean}
         */
        exists() {
            return exist;
        },

        /**
         * Возвращает отформатированную строку с часами
         * для ограбления во временной зоне банка
         *
         * @param {string} template
         * @returns {string}
         *
         * @example
         * ```js
         * getAppropriateMoment(...).format('Начинаем в %HH:%MM (%DD)') // => Начинаем в 14:59 (СР)
         * ```
         */
        format(template) {
            if (exist) {
                return template.replace(/%HH/g, res.HH).replace(/%MM/g, res.MM).replace(/%DD/g, res.DD);
            }
            return "";
        },

        /**
         * Попробовать найти часы для ограбления позже [*]
         * @note Не забудь при реализации выставить флаг `isExtraTaskSolved`
         * @returns {boolean}
         */
        tryLater() {
            if (exist) {
                for (let i = resNum + 30; i <= bankSect[2].r - duration; i++) {
                    if (check(i)) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
    };
}

if (debug) {
    const gangSchedule = {
        Danny: [{ from: 'ПН 00:00+5', to: 'СР 23:58+5' }],
        Rusty: [{ from: 'ВТ 01:30+5', to: 'ВТ 23:57+5' }],
        Linus: [
            { from: 'СР 09:00+5', to: 'СР 23:56+5' }
        ]
    };

    const bankWorkingHours = {
        from: '10:00+5',
        to: '23:59+5'
    };
    const moment = getAppropriateMoment(gangSchedule, 10, bankWorkingHours);

// Выведется `true` и `"Метим на ВТ, старт в 11:30!"`
    console.info(moment.exists());
    console.info(moment.format('Метим на %DD, старт в %HH:%MM!'));
    // Дополнительное задание
// Вернет `true`
    console.info(moment.tryLater());
// `"ВТ 16:00"`
    console.info(moment.format('%DD %HH:%MM'));

// Вернет `true`
    console.info(moment.tryLater());
// `"ВТ 16:30"`
    console.info(moment.format('%DD %HH:%MM'));

// Вернет `true`
    console.info(moment.tryLater())
// `"СР 10:00"`
    console.info(moment.format('%DD %HH:%MM'));

// Вернет `false`
    console.info(moment.tryLater())
// `"СР 10:00"`
    console.info(moment.format('%DD %HH:%MM'));
    for (let i = 0; i < 20; i++) {
        console.info(moment.tryLater())
        console.info(moment.format('%DD %HH:%MM'));
    }
    let moment2 = getAppropriateMoment(gangSchedule, 10, bankWorkingHours);
    console.log(moment2.format('%DD %HH:%MM'));
    console.info(moment2.tryLater());
    console.info(moment2.format('%DD %HH:%MM'));
    console.log(moment.tryLater());
    console.info(moment.format('%DD %HH:%MM'))
}

module.exports = {
    getAppropriateMoment
};