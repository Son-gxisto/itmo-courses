let m_const = 5;
let n_const = 5;
let k_const = 5;
//m=n=k game
function randomInt(min, max) {
    return min + Math.floor((max - min) * Math.random());
}
function BoardConfiguration() {
    this.m = m_const;
    this.n = n_const;
    this.k = k_const;
    this.board = new Array(this.m);
    for(let i = 0; i < this.m; i++) {
        this.board[i] = new Array(this.n).fill('.');
    }
}
BoardConfiguration.prototype.getCell = function(x, y) {
    return this.board[x][y];
}
BoardConfiguration.prototype.getM = function() {
    return this.m;
}
BoardConfiguration.prototype.getN = function() {
    return this.n;
}
BoardConfiguration.prototype.getK = function() {
    return this.k;
}
BoardConfiguration.prototype.setCell = function(x, y, c) {
    this.board[x][y] = c;
}
BoardConfiguration.prototype.isCorrect = function(x, y) {
    return this.board[x][y] === '.';
}
BoardConfiguration.prototype.toString = function() {
    return this.board.join("\n");
}
function Player(sym) {
    this.sym = sym;
    this.getSym = function() {
        return this.sym;
    }
}
function ComputerPlayerSeq(sym) {
    Player.call(this, sym);
};
ComputerPlayerSeq.prototype.makeMove = function(boardConf) {
    for(let i = 0; i < boardConf.getM(); i++) {
        for (let j = 0; j < boardConf.getN(); j++) {
            if (boardConf.isCorrect(i, j)) {
                boardConf.setCell(i, j, this.getSym());
                return;
            }
        }
    }
}
function ComputerPlayerRandom(sym) {
    Player.call(this, sym);
    this.planB = new ComputerPlayerSeq(sym);
}
ComputerPlayerRandom.prototype.makeMove = function(boardConf) {
    let x = randomInt(0, boardConf.getM());
    let y = randomInt(0, boardConf.getN());
    let count = 0;
    let m = boardConf.getM();
    while (!boardConf.isCorrect(x, y) && count < m*m*m*m) {
        let x = randomInt(0, boardConf.getM());
        let y = randomInt(0, boardConf.getN());
        count++;
    }
    if (count === m*m*m*m) {
        this.planB.makeMove(boardConf);
    } else {
        boardConf.setCell(x, y, this.getSym());
    }
}

function HumanPlayer(sym, steps) {
    Player.call(this, sym);
    this.steps = steps.split(" ").reverse();
    this.planB = new ComputerPlayerRandom(sym);
    this.makeMove = function(boardConf) {
        let y = this.steps.pop();
        let x = this.steps.pop();
        if (boardConf.isCorrect(x, y)) {
            boardConf.setCell(x, y, this.getSym());
        } else {
            this.planB.makeMove(boardConf);
        }
    }
}

function GameServer(pl1, pl2, board) {
    this.pl1 = pl1;
    this.pl2 = pl2;
    this.board = board;
}

/**
 * @return {number}
 */
GameServer.prototype.CheckWin = function(sym) {
    let cm = 1;
    let cp = 1;
    let count = 0;
    for (let i = 0; i < this.board.getM() - 1; i++) {
        if (this.board.getCell(i, i) === this.board.getCell(i + 1, i + 1) && this.board.getCell(i, i) === sym) {
            cm++;
        }
        if (this.board.getCell(i, this.board.getM() - i - 1) === this.board.getCell(i + 1, this.board.getM() - i - 2) && this.board.getCell(i, this.board.getM() - i - 1) === sym) {
            cp++;
        }
        if (this.board.getCell(i, this.board.getN() - 1) === '.') {
            count++;
        }
    }

    for (let i = 0; i < this.board.getM(); i++) {
        let cw = 1;
        let ch = 1;
        for (let j = 0; j < this.board.getN() - 1; j++) {
            if (this.board.getCell(i, j) === '.') {
                count++;
            }
            if (this.board.getCell(i, j) === sym) {
                if (this.board.getCell(i, j) === this.board.getCell(i, j + 1)) {
                    cw++;
                }
                if (this.board.getCell(j, i) === this.board.getCell(j + 1, i)) {
                    ch++;
                }
            }
        }
        if (cw === this.board.getM() || ch === this.board.getN()) {
            return 2;
        }
    }
    if (count > 0) {
        if (cm === this.board.getM() || cp === this.board.getM()) {
            return 2;
        } else {
            return 0;
        }
    } else {
        return 1;
    }
}
GameServer.prototype.start = function() {
    let checkPl1 = 0;
    while (!(checkPl1 === 2) && !(checkPl1 === 1) && !(this.CheckWin(this.pl2.getSym()) === 2)) {
        this.pl1.makeMove(this.board);
        checkPl1 = this.CheckWin(this.pl1.getSym());
        if (checkPl1 === 0) {
            this.pl2.makeMove(this.board);
        }
    }
    if (checkPl1 === 2) {
        return 1;
    } else if (checkPl1 === 1 && this.CheckWin(this.pl2.getSym()) === 1) {
        return 0;
    } else {
        return 2;
    }
}
let countpl1win = 0;

function Tournament(CircleCount, players) {
    this.count = CircleCount;
    this.players = players;
    this.fullStats = [];
    this.endStats = [];
}
Tournament.prototype.start = function() {
    let plc = this.players.length;
    this.fullStats = new Array(this.count);
    for (let i = 0; i < this.count; i++) {
        this.fullStats[i] = new Array(plc);
        for (let j = 0; j < plc; j++) {
            this.fullStats[i][j] = new Array(plc);
            for (let k = 0; k < plc; k++) {
                this.fullStats[i][j][k] = 0;
            }
        }
    }
    for (let i = 0; i < this.count; i++) {
        for (let j = 0; j < plc; j++) {
            for (let k = j + 1; k < plc; k++) {
                let game = new GameServer(this.players[j], this.players[k], new BoardConfiguration());
                res = game.start();
                switch(res) {
                    case 1:
                        this.fullStats[i][j][k] = 3;
                        this.fullStats[i][k][j] = 0;
                        break;
                    case 2:
                        this.fullStats[i][j][k] = 0;
                        this.fullStats[i][k][j] = 3;
                        break;
                    case 0:
                        this.fullStats[i][j][k] = 1;
                        this.fullStats[i][k][j] = 1;
                        break;
                }
            }
        }
        //console.log(this.fullStats[i]);
    }
    this.endStats = new Array(plc);
    for (let i = 0; i < plc; i++) {
        this.endStats[i] = new Array(plc);
        for (let j = 0; j < plc; j++) {
            this.endStats[i][j] = 0;
        }
    }
    for(let i = 0; i < this.count; i++) {
        for (let j = 0; j < plc; j++) {
            for (let k = 0; k < plc; k++) {
                this.endStats[j][k] += this.fullStats[i][j][k];
            }
        }
    }
}
Tournament.prototype.printStats = function() {
    let plc = this.players.length;
    let playersRes = new Array(plc);
    for (let j = 0; j < plc; j++) {
        playersRes[j] = 0;
        for (let k = 0; k < plc; k++) {
            playersRes[j]+=this.endStats[j][k];
        }
    }
    console.log("a[i][j] = points, which player i won vs player j");
    for (let i = 0; i < this.endStats.length; i++) {
        console.log((i + 1) + ": " + this.endStats[i].join(' '));
    }
    for (let i = 0; i < this.endStats.length; i++) {
        console.log((i + 1) + " sum: " + playersRes[i]);
    }
}

let tour = new Tournament(10, [
    new ComputerPlayerRandom('20'),
    new ComputerPlayerSeq('P'),
    new ComputerPlayerSeq('L'),
    new ComputerPlayerSeq('E'),
    new ComputerPlayerSeq('A'),
    new ComputerPlayerSeq('S'),
    new ComputerPlayerSeq('E')]
);
tour.start();
tour.printStats();