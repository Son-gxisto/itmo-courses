'use strict';

const weather = require('./weather');
const w2 = require('./w2')
const fetch = require("node-fetch");

/**
 * Как выбрать geoid для тестирования функции:
 * Заходим на https://yandex.ru/pogoda, в поиске вводим желаемый город
 * Получаем урл вида https://yandex.ru/pogoda/10451 - 10451 это geoid
 */
const geoids2 = [37, 10451, 54];
const geoids1 = [10451, 54];
async function main() {
  const path = await weather
    .planTrip(geoids2)
    .sunny(1)
    .cloudy(4)
    .max(3)
    .build();
  console.info(path);

  const path2 = await w2
    .planTrip(geoids2)
    .sunny(1)
    .cloudy(4)
    .max(3)
    .build();

  console.info(path2);
}

main().catch(console.error);

