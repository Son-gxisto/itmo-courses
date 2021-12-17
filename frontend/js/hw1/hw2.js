'use strict';

/**
 * Телефонная книга
 */
const phoneBook = new Map();
const mails = "mails"
const phones = "phones"


/**
 * +5556667788 -> 7 (555) 666-77-88
 * @param num {String}
 * @returns {string}
 */
function phoneToStr(num) {
    return `+7 (${num.substr(0, 3)}) ${num.substr(3, 3)}-${num.substr(6, 2)}-${num.substr(8, 2)}`;
}

/**
 * Вызывайте эту функцию, если есть синтаксическая ошибка в запросе
 * @param {number} lineNumber – номер строки с ошибкой
 * @param {number} charNumber – номер символа, с которого запрос стал ошибочным
 */
function syntaxError(lineNumber, charNumber) {
    throw new Error(`SyntaxError: Unexpected token at ${lineNumber}:${charNumber}`);
}

function get(name) {
    return phoneBook.get(name);
}

function exist(name) {
    return get(name) !== undefined;
}

function addContact(name) {
    if (!exist(name)) {
        phoneBook.set(name, {phones: new Set(), mails: new Set()});
    }
}

function removeContact(name) {
    return phoneBook.delete(name);
}

function add(name, key, value) {
    if (exist(name)) {
        get(name)[key].add(value);
    }
}

function addMail(name, mail) {
    add(name, mails, mail);
}

function addPhone(name, phone) {
    add(name, phones, phone)
}

function remove(name, key, value) {
    if (exist(name)) {
        get(name)[key].delete(value);
    }
}

function removeMail(name, mail) {
    remove(name, mails, mail)
}

function removePhone(name, phone) {
    remove(name, phones, phone);
}

function check(name, req) {
    if (exist(name)) {
        let flag = name.includes(req);
        let value = get(name);
        if (!flag) {
            value[mails].forEach(value => flag = flag || value.includes(req));
            if (!flag) {
                value[phones].forEach(value => flag = flag || value.includes(req));
            }
        }
        return flag;
    }
    return false;
}

function show(req, ...keys) {
    let res = [];
    if (req === '') {
        return res;
    }
    let tmp;
    phoneBook.forEach((value, key) => {
        tmp = [];
        if (check(key, req)) {
            keys.forEach((k, i)=> {
                if (k === 'почты') {
                    let tmp2 = [];
                    tmp2.push(...value.mails);
                    tmp.push(tmp2.join(','));
                } else if (k === 'телефоны') {
                    let tmp2 = [];
                    value.phones.forEach(v => {
                        tmp2.push(phoneToStr(v));
                    })
                    tmp.push(tmp2.join(','));
                } else if (k === 'имя') {
                    tmp.push(key);
                }
            });
            res.push(tmp.join(';'));
        }
    });
    return res;
}

function removeReq(req) {
    if (req === '') {
        return;
    }
    phoneBook.forEach((value, key) => {
        if (check(key, req)) {
            removeContact(key);
        }
    });
}

function parseReq(q, ind) {
    if (ind > q.length) {
        return -1;
    }
    return q.slice(ind);
}

const queriesPrefix = {
    removeReq_: "Удали контакты, где есть",
    removeContact_: "Удали контакт",
    removeData_: "Удали",
    addContact_: "Создай контакт",
    addData_: "Добавь",
    show_: "Покажи"
};

//количество аргументов, 2 здесь 2+
const nums = {
    removeContact_: 1,
    removeReq_: 1,
    addContact_: 1,
    removeData_: 2,
    addData_: 2,
    show_: 2
};
const funcs = {
    removeContact_: removeContact,
    addContact_: addContact,
    removeReq_: removeReq
}

/**
 * @param line{query}
 * @param ind{index}
 * @param word{string}
 * @returns {(number|*)[]} [2, word end index] or [1, ind] or [0, indexOfSE] if syntaxError
 */
function checkWord(line, ind, word) {
    let sdw = 0;
    if (word[sdw] !== line[ind + sdw]) {
        return [1, ind + sdw];
    }
    while (ind + sdw < line.length && word[sdw] === line[ind + sdw]) {
        sdw++;
    }
    if (sdw === word.length) {
        return [2, ind + sdw];
    }
    if (sdw === line.length) {
        return [3, ind + sdw];
    }
    return [0, ind + sdw];
}

function parseQuery(query) {
    let res = 1;
    for (const [key, value] of Object.entries(queriesPrefix)) {
        if (query === value) {
            return -1;
        }
        if (query.startsWith(value) && res <= value.length + (value === "Удали" ? 1 : 0)) {
            return key;
        }
        let tmp = checkWord(query, 0, value);
        if (tmp[0] === 0) {
            if (tmp[1] === query.length) {
                return -1;
            }
            res = Math.max(res, tmp[1]);
        } else {
            res = Math.max(res, tmp[1]);
        }
    }
    return res;
}

function parseNumber(s) {
    let match = /^ \d{10} $/.exec(s);
    return !!match;
}

function firstW(s, ind) {
    for (let i = ind; i > 0; i--) {
        if (s[i] === ' ') {
            return i;
        }
    }
    return -1;
}

/**
 * Выполнение запроса на языке pbQL
 * @param {string} query
 * @returns {string[]} - строки с результатами запроса
 */
function run(query) {
    if (query === "") {
        syntaxError(1, 1);
    }
    let queries = query.split(";");
    let res = [];
    let lineNumber = 0;
    let l = 0;
    let totalL = 0;
    //разбили на запросы
    queries.forEach(q => {

        totalL += l;
        l = 0;
        lineNumber++;
        if (q === "" && lineNumber === queries.length) {
            return;
        } else if (q === "") {
            syntaxError(lineNumber, 1);
        }


        let tmp = q;
        let parseRes = parseQuery(tmp);
        if (parseRes === -1) {
            syntaxError(lineNumber, q.length + 1);
        }
        if (typeof parseRes === 'number') {
            syntaxError(lineNumber, Math.max(-1, firstW(q, parseRes - 1)) + 2);
        }
        l = queriesPrefix[parseRes].length;
        let forName = "";
        tmp = q.slice(l);
        if (tmp === "") {
            syntaxError(lineNumber, q.length + 1);
        }
        if (tmp[0] === ' ') {
            tmp = tmp.slice(1);
            l++;
        } else {
            syntaxError(lineNumber, firstW(q, l) + 2);
        }
        let expectS = false;
        if (tmp === '' && !query.includes(';')) {
            syntaxError(lineNumber, q.length + 1);
        }
        if (nums[parseRes] === 1) {
            //если возможен только один аргумент, парсим до конца строки
            let token = parseReq(q, l);
            funcs[parseRes](token);
            l = q.length + 1;
        } else if (parseRes === "addData_" || parseRes === "removeData_") {
            //Добавь \ Удали
            let tokens = {
                phones: [],
                mails: []
            };
            for (let i = 0; i < tmp.length; i++) {
                //пытаемся распарсить телефон {телефон}
                if (!expectS) {
                    //если не ожидаем "и" или конец строки
                    let chRes = checkWord(tmp, i, "телефон ");
                    switch (chRes[0]) {
                        case 2:
                            expectS = true;
                            let r = parseNumber(tmp.substr(chRes[1] - 1, 12));

                            if (r) {
                                r = chRes[1] + 10;
                                //нашли корректный {телефон}
                                tokens.phones.push(tmp.substring(chRes[1], r));
                                i = -1;
                                l += r + 1;
                                tmp = tmp.slice(r + 1); //+1 for remove whitespace
                            } else {
                                //не нашли
                                syntaxError(lineNumber, l + chRes[1] + 1);
                            }
                            break;
                        case 0:
                            syntaxError(lineNumber, l + 1);
                            break;
                    }
                    if (chRes[0] === 1) {
                        chRes = checkWord(tmp, i, "почту ");
                        switch (chRes[0]) {
                            case 2:
                                //ищем почту
                                expectS = true;
                                let j;
                                for (j = chRes[1]; j < tmp.length && tmp[j] !== ' '; j++) {
                                }
                                tokens.mails.push(tmp.substring(chRes[1], j));
                                i = -1;
                                l += j + 1;
                                tmp = tmp.slice(j + 1);
                                break;
                            case 0:
                                syntaxError(lineNumber, l + 1);
                                break;
                        }
                    }
                    if (chRes[0] === 1) {
                        syntaxError(lineNumber, l + 1);
                    }
                } else {
                    //ожидаем "и" или "для {name}"
                    if (tmp !== "") {
                        let chRes;
                        chRes = checkWord(tmp, i, "и ");
                        switch(chRes[0]) {
                            case 2:
                                i = -1;
                                l += 2;
                                tmp = tmp.slice(2);
                                expectS = false;
                                break;
                            case 0:
                                syntaxError(lineNumber, l + 3);
                                break;
                        }
                        if (chRes[0] === 1) {
                            chRes = checkWord(tmp, i, "для контакта");
                            switch (chRes[0]) {
                                case 0:
                                    syntaxError(lineNumber, l + firstW(tmp, chRes[1] - 1) + 2);
                                    break;
                                case 2:
                                    if (tmp[chRes[1]] !== ' ') {
                                        if (chRes[1] !== tmp.length) {
                                            syntaxError(lineNumber, l + firstW(tmp, chRes[1] - 1) + 3);
                                        } else {
                                            syntaxError(lineNumber, q.length + 1);
                                        }
                                    }
                                    forName = parseReq(tmp, chRes[1] + 1);
                                    if (forName === -1) {
                                        syntaxError(lineNumber, l + firstW(tmp, chRes[1] - 1) + 3);
                                    }
                                    expectS = false;
                                    tmp = "";
                                    l = q.length + 1;
                                    break;
                            }
                        }
                        if (chRes[0] === 1) {
                            syntaxError(lineNumber, l + 1);
                        }
                    } else {
                        break;
                    }
                }
            }
            if (expectS) {
                syntaxError(lineNumber, l + 1);
            }

            if (parseRes === "addData_") {
                tokens.mails.forEach(value => addMail(forName, value));
                tokens.phones.forEach(value => addPhone(forName, value));
            } else if (parseRes === "removeData_") {
                tokens.mails.forEach(value => removeMail(forName, value));
                tokens.phones.forEach(value => removePhone(forName, value));
            }
        } else {
            let tokens = [];
            for (let i = 0; i < tmp.length; i++) {
                if (!expectS) {
                    //если не ожидаем "и" или конец строки
                    let token = "";
                    let chRes = checkWord(tmp, i, "телефоны ");
                    if (chRes[0] === 2) {
                        token = "телефоны";
                    }
                    if (chRes[0] === 1) {
                        chRes = checkWord(tmp, i, "почты ");
                        if (chRes[0] === 2) {
                            token = "почты";
                        }
                    }
                    if (chRes[0] === 1) {
                        chRes = checkWord(tmp, i, "имя ");
                        if (chRes[0] === 2) {
                            token = "имя";
                        }
                    }
                    switch (chRes[0]) {
                        case 2:
                            expectS = true;
                            tokens.push(token);
                            let r = chRes[1];
                            i = -1;
                            l += r;
                            tmp = tmp.slice(r);
                            break;
                        case 0:
                            syntaxError(lineNumber, l + 1);
                            break;
                    }
                    if (chRes[0] === 1) {
                        syntaxError(lineNumber, l + 1);
                    }
                } else {
                    //ожидаем "и" или "для {name}"
                    if (tmp !== "") {
                        let chRes;
                        chRes = checkWord(tmp, i, "и ");
                        switch(chRes[0]) {
                            case 2:
                                i = -1;
                                l += 2;
                                tmp = tmp.slice(2);
                                expectS = false;
                                break;
                            case 0:
                                syntaxError(lineNumber, l + 1);
                                break;
                        }
                        if (chRes[0] !== 2) {
                            chRes = checkWord(tmp, i, "для контактов, где есть");
                            switch (chRes[0]) {
                                case 0:
                                    syntaxError(lineNumber, l + firstW(tmp, chRes[1] - 1) + 2);
                                    break;
                                case 2:
                                    if (tmp[chRes[1]] !== ' ') {
                                        if (chRes[1] !== tmp.length) {
                                            syntaxError(lineNumber, l + firstW(tmp, chRes[1] - 1) + 3);
                                        } else {
                                            syntaxError(lineNumber, q.length + 1);
                                        }
                                    }
                                    forName = parseReq(tmp, chRes[1] + 1);
                                    expectS = false;
                                    if (forName === -1) {
                                        syntaxError(lineNumber, q.length + 1);
                                    }
                                    tmp = "";
                                    l = q.length + 1;
                                    break;
                            }
                        }
                        if (chRes[0] !== 2) {
                            syntaxError(lineNumber, l + 1);
                        }
                    } else {
                        break;
                    }
                }
            }
            if (expectS) {
                syntaxError(lineNumber, l + 1);
            }
            if (parseRes === "addData_") {
                tokens.mails.forEach(value => addMail(forName, value));
                tokens.phones.forEach(value => addPhone(forName, value));
            } else if (parseRes === "removeData_") {
                tokens.mails.forEach(value => removeMail(forName, value));
                tokens.phones.forEach(value => removePhone(forName, value));
            } else if (parseRes === "show_") {
                res.push(...show(forName, ...tokens));
            }
        }
        if (query[totalL + q.length] !== ';') {
            syntaxError(lineNumber, q.length + 1);
        }
    })
    return res;
}

module.exports = { phoneBook, run };

/*
console.log(run(
    'Создай контакт Григорий;' +
    'Добавь телефон 5556667788 для контакта Григорий;' +
    'Удали телефон 5556667788 для контакта Григорий;' +
    'Покажи имя и телефоны для контактов, где есть ий;'
));
console.log(phoneBook);
/*
Удали - 6
Удали; - 6
Удали контакты, где;
Удали контакты, где ест
 */