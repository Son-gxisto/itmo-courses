'use strict';

/*
* Складывает два целых числа
* @param {Number} a Первое целое
* @param {Number} b Второе целое
* @throws {TypeError} Когда в аргументы переданы не числа
* @returns {Number} Сумма аргументов
*/
function abProblem(a, b) {
    if (typeof a === 'number' && typeof b === 'number') {
        return a + b;
    }
    throw TypeError('TypeError');
}

/* Определяет век по году
* @param {Number} year Год, целое положительное число
* @throws {TypeError} Когда в качестве года передано не число
* @throws {RangeError} Когда год – отрицательное значение
* @returns {Number} Век, полученный из года
*/
function centuryByYearProblem(year) {
    if (typeof year !== 'number') {
        throw TypeError('TypeError');
    }
    if (year < 0 || !Number.isInteger(year)) {
        throw RangeError('RangeError');
    }
    return Math.floor((year - 1) / 100) + 1;
}

/* Переводит цвет из формата HEX в формат RGB
* @param {String} hexColor Цвет в формате HEX, например, '#FFFFFF'
* @throws {TypeError} Когда цвет передан не строкой
* @throws {RangeError} Когда значения цвета выходят за пределы допустимых
* @returns {String} Цвет в формате RGB, например, '(255, 255, 255)'
*/
function colorsProblem(hexColor) {
    if (typeof hexColor !== 'string') {
        throw TypeError('TypeError');
    }
    let t = hexColor;
    if (hexColor.length !== 7 && hexColor.startsWith('#')) {
        t = hexColor.substr(1);
        for (let i = t.length; i < 6; i++) {
            t = '0' + t;
        }
        t = '#' + t;
    }
    let arr = /^#([A-F\d]{2})([A-F\d]{2})([A-F\d]{2})$/i.exec(t);
    if (arr !== null) {
        const r = parseInt(arr[1], 16);
        const g = parseInt(arr[2], 16);
        const b = parseInt(arr[3], 16);
        return `(${r}, ${g}, ${b})`;
    }
    throw RangeError('RangeError');
}

/* Находит n-ое число Фибоначчи
* @param {Number} n Положение числа в ряде Фибоначчи
* @throws {TypeError} Когда в качестве положения в ряде передано не число
* @throws {RangeError} Когда положение в ряде не является целым положительным числом
* @returns {Number} Число Фибоначчи, находящееся на n-ой позиции
*/
function fibonacciProblem(n) {
    if (typeof n !== 'number') {
        throw TypeError('TypeError');
    }
    if (!Number.isInteger(n) || n < 0) {
        throw RangeError('RangeError')
    }
    let a = 1, b = 1;
    if (n < 3) {
        return 1;
    }
    for (let i = 2; i < n; i++) {
        let t = a + b;
        a = b;
        b = t;
    }
    return b;
}

/* Транспонирует матрицу
* @param {(Any[])[]} matrix Матрица размерности MxN
* @throws {TypeError} Когда в функцию передаётся не двумерный массив
* @returns {(Any[])[]} Транспонированная матрица размера NxM
*/
function matrixProblem(matrix) {
    let n = -1;
    if (Array.isArray(matrix)) {
        if (Array.isArray(matrix[0])) {
            n = matrix[0].length;
        }
    }
    if (n === -1) {
        throw TypeError('TypeError');
    }
    let res = [];
    for (let i = 0; i < matrix.length; i++) {
        if (matrix[i].length !== n) {
            //throw TypeError('TypeError;
        }
    }
    for (let i = 0; i < n; i++) {
        res.push([]);
    }
    for (let i = 0; i < matrix.length; i++) {
        for (let j = 0; j < matrix[i].length; j++) {
            res[j].push(matrix[i][j]);
        }
    }
    return res;
}
/* Переводит число в другую систему счисления
* @param {Number} n Число для перевода в другую систему счисления
* @param {Number} targetNs Система счисления, в которую нужно перевести (Число от 2 до 36)
* @throws {TypeError} Когда переданы аргументы некорректного типа
* @throws {RangeError} Когда система счисления выходит за пределы значений [2, 36]
* @returns {String} Число n в системе счисления targetNs
*/
function numberSystemProblem(n, targetNs) {
    if (typeof n !== "number" || typeof targetNs !== 'number') {
        throw TypeError('TypeError');
    }
    if (!(2 <= targetNs && targetNs <= 36)) {
        throw RangeError('RangeError');
    }
    return n.toString(targetNs);
}

/* Проверяет соответствие телефонного номера формату
* @param {String} phoneNumber Номер телефона в формате '8–800–xxx–xx–xx'
* @throws {TypeError} Когда в качестве аргумента передаётся не строка
* @returns {Boolean} Если соответствует формату, то true, а иначе false
*/
function phoneProblem(phoneNumber) {
    if (typeof phoneNumber !== 'string') {
        throw TypeError('TypeError');
    }
    return (phoneNumber.match(/^8-800-\d{3}-\d\d-\d\d$/) !== null);
}

/* Определяет количество улыбающихся смайликов в строке
* @param {String} text Строка в которой производится поиск
* @throws {TypeError} Когда в качестве аргумента передаётся не строка
* @returns {Number} Количество улыбающихся смайликов в строке
*/
function smilesProblem(text) {
    if (typeof text !== 'string') {
        throw TypeError('TypeError');
    }
    let t = text.match(/([\(]-:)|(:-[\)])/g);
    return t === null ? 0 : t.length;
}
/**
 * Определяет победителя в игре "Крестики-нолики"
 * Тестами гарантируются корректные аргументы.
 * @param {(('x' | 'o')[])[]} field Игровое поле 3x3 завершённой игры
 * @returns {'x' | 'o' | 'draw'} Результат игры
 */
function ticTacToeProblem(field) {
    let xc = 0, oc = 0, x = 'x', o='o';
    for (let i = 0; i < 3; i++) {
        for (let j = 0; j < 3; j++) {
            if (field[i][j]===x) {
                xc++;
            } else if (field[i][j] === o) {
                oc++;
            }
        }
        if (xc === 3) {
            return x;
        } else if (oc === 3) {
            return o;
        }
        xc = 0;
        oc = 0;
    }
    for (let i = 0; i < 3; i++) {
        for (let j = 0; j < 3; j++) {
            if (field[j][i]===x) {
                xc++;
            } else if (field[j][i] === o) {
                oc++;
            }
        }
        if (xc === 3) {
            return x;
        } else if (oc === 3) {
            return o;
        }
        xc = 0;
        oc = 0;
    }
    for (let i = 0; i < 3; i++) {
        if (field[i][i] === x) {
            xc++;
        } else if (field[i][i] === o) {
            oc++;
        }
    }
    if (xc === 3) {
        return x;
    } else if (oc === 3) {
        return o;
    }
    xc = 0;
    oc = 0;
    for (let i = 0; i < 3; i++) {
        if (field[2 - i][i] === x) {
            xc++;
        } else if (field[2 - i][i] === o) {
            oc++;
        }
    }
    if (xc === 3) {
        return x;
    } else if (oc === 3) {
        return o;
    }
    return 'draw';
}
module.exports = {
    abProblem,
    centuryByYearProblem,
    colorsProblem,
    fibonacciProblem,
    matrixProblem,
    numberSystemProblem,
    phoneProblem,
    smilesProblem,
    ticTacToeProblem
};