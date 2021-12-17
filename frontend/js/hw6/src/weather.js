'use strict';

const debug = false;

const fetch = require('node-fetch');

const API_KEY = require('./key.json');

async function getWeather(id) {
  return fetch(`https://api.weather.yandex.ru/v2/forecast?hours=false&limit=7&geoid=${id}`, {
    headers: {
      'X-Yandex-API-Key': API_KEY.key
    }
  }).then(response => response.json())
}

const SUNNY = 'sunny';
const CLOUDY = 'cloudy';
/**
 * @typedef {object} TripItem Город, который является частью маршрута.
 * @property {number} geoid Идентификатор города
 * @property {number} day Порядковое число дня маршрута
 */

class TripBuilder {
  day = 0;

  constructor(geoids) {
    this.geoids = geoids;
  }

  req = [];
  maxDays = 7;

  /**
   * Метод, добавляющий условие наличия в маршруте
   * указанного количества солнечных дней
   * Согласно API Яндекс.Погоды, к солнечным дням
   * можно приравнять следующие значения `condition`:
   * * `clear`;
   * * `partly-cloudy`.
   * @param {number} daysCount количество дней
   * @returns {object} Объект планировщика маршрута
   */
  sunny(daysCount) {
    for (let i = 0; i < daysCount; i++) {
      this.req.push(SUNNY);
    }
    return this;
  }

  /**
   * Метод, добавляющий условие наличия в маршруте
   * указанного количества пасмурных дней
   * Согласно API Яндекс.Погоды, к солнечным дням
   * можно приравнять следующие значения `condition`:
   * * `cloudy`;
   * * `overcast`.
   * @param {number} daysCount количество дней
   * @returns {object} Объект планировщика маршрута
   */
  cloudy(daysCount) {
    for (let i = 0; i < daysCount; i++) {
      this.req.push(CLOUDY);
    }
    return this;
  }

  /**
   * Метод, добавляющий условие максимального количества дней.
   * @param {number} daysCount количество дней
   * @returns {object} Объект планировщика маршрута
   */
  max(daysCount) {
    this.maxDays = daysCount;
    return this;
  }

  add(geoid) {
    this.path.push({'geoid': geoid, 'day': this.day + 1});
    this.day++;
  }

  checkPath(path) {
    let visited = new Map();
    for (let i = 0; i < path.length; i++) {
      if (!visited.has(path[i].geoid)) {
        visited.set(path[i].geoid, 1);
      } else {
        if (path[i].geoid !== path[i - 1].geoid) {
          return false;
        } else {
          visited.set(path[i].geoid, visited.get(path[i].geoid) + 1);
        }
      }
    }
    for (const [key, value] of visited.entries()) {
      if (value > this.maxDays) {
        return false;
      }
    }
    return true;
  }

  prevDay = null;
  searchPath(cities, path, day) {
    if (day > 7 || day >= this.req.length) {
      if (this.checkPath(path)) {
        return path;
      } else {
        return null;
      }
    }
    let pos = cities[day]; //.filter((city) => { return !visited.has(city); })
    for (let j = 0; j < pos.length; j++) {
      if (this.prevDay !== null) {
        if (this.prevDay.forecasts[day] === this.req[day]) {
          path.push(this.prevDay);
          const sufPrev = this.searchPath(cities, path, day + 1);
          if (sufPrev !== null) {
            return path;
          }
          path.pop();
          this.prevDay = null;
        }
      }
      path.push(pos[j]);
      this.prevDay = pos[j]
      const suf = this.searchPath(cities, path, day + 1)
      if (suf === null) {
        path.pop();
      } else {
        return path;
      }
    }
    return null;
  }

  /**
   * Метод, возвращающий Promise с планируемым маршрутом.
   * @returns {Promise<TripItem[]>} Список городов маршрута
   */
  build(){
    return new Promise((resolve, reject) => {
      Promise.all(this.geoids.map(id => getWeather(id))).then(data => {
        let cities = [];
        for (let i = 0; i < data.length; i++) {
          const cur = data[i];
          const geoid = cur['info']['geoid'];
          let forecasts = [];
          for (let j = 0; j < cur['forecasts'].length; j++) {
            const day = cur['forecasts'][j];
            const pred = day['parts']['day_short']['condition'];
            if (pred === 'clear' || pred === 'partly-cloudy') {
              forecasts.push(SUNNY);
            } else if (pred ==='cloudy' || pred === 'overcast') {
              forecasts.push(CLOUDY);
            } else {
              forecasts.push('no');
            }
            cities.push({'geoid': geoid, 'forecasts': forecasts});
          }
        }
        if (debug) {
          console.log(cities);
          console.log(this.req);
        }
        let posCities = [];
        for (let i = 0; i < this.req.length; i++) {
          posCities.push(cities.filter((city) => {
            return city.forecasts[i] === this.req[i]
          }))
        }
        this.path = this.searchPath(posCities, [], this.day);

        if (this.path !== null) {
          let p = this.path;
          this.path = [];
          p.forEach((city) => {
            this.add(city.geoid);
          })
          resolve(this.path);
        } else {
          reject(new Error('Не могу построить маршрут!'))
        }
      })
        .catch(error => reject(error))
    })
  }
}

/**
 * Фабрика для получения планировщика маршрута.
 * Принимает на вход список идентификаторов городов, а
 * возвращает планировщик маршрута по данным городам.
 *
 * @param {number[]} geoids Список идентификаторов городов
 * @returns {TripBuilder} Объект планировщика маршрута
 * @see https://yandex.ru/dev/xml/doc/dg/reference/regions-docpage/
 */
function

planTrip(geoids) {
  return new TripBuilder(geoids);
}

module
  .exports = {
  planTrip
};
