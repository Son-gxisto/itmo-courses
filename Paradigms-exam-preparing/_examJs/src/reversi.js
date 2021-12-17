let m_const = 8;
let n_const = 8;

//M x N reversi
function randomInt(min, max) {
    return min + Math.floor((max - min) * Math.random());
}
function BoardConfiguration(m = m_const, n = n_const) {
    this.m = m;
    this.n = n;
    this.board = new Array(this.m);
    for(let i = 0; i < this.m; i++) {
        this.board[i] = new Array(this.n);
        for (let j = 0; j < this.n; j++) {
            this.board[i][j] = '.';
        }
    }
    let m2 = Math.floor(this.m/2 - 1);
    let n2 = Math.floor(this.n/2 - 1);
    for (let i = 0; i < 2; i++) {
        for (let j = 0; j < 2; j++) {
            if (i === j) {
                this.board[m2 + i][n2 + j] = 'B';
            } else {
                this.board[m2 + i][n2 + j] = 'W';
            }

        }
    }
}
BoardConfiguration.prototype.between = function (x, y) {
    return x >= 0 && x < this.getM() && y >= 0 && y < this.getN();
};
BoardConfiguration.prototype.getCell = function(x, y) {
    return this.board[x][y];
};
BoardConfiguration.prototype.getM = function() {
    return this.m;
};
BoardConfiguration.prototype.getN = function() {
    return this.n;
};
BoardConfiguration.prototype.fillLine = function(x, y, vx, vy, sym) {
    while (this.between(x, y) && this.board[x][y] !== sym) {
        this.board[x][y] = sym;
        x += vx;
        y += vy;
    }
};
BoardConfiguration.prototype.setCell = function(x, y, sym) {
    for (let i = -1; i < 2; i++) {
        for (let j = -1; j < 2; j++) {
            let m = x - i;
            let n = y - j;
            if (!(i === 0 && j === 0) && this.between(m, n)) {
                let c = this.board[m][n];
                if (c !== '.' &&
                    c !== sym &&
                    this.haveSym(x, y, -i, -j, sym)) {
                    this.fillLine(x, y, -i, -j, sym);
                }
            }
        }
    }
};
BoardConfiguration.prototype.haveSym = function(x, y, vx, vy, sym) {
    while (this.between(x, y) && this.board[x][y] !== sym) {
        x += vx;
        y += vy;
    }
    return (this.between(x, y) && this.board[x][y] === sym);
};

BoardConfiguration.prototype.isCorrect = function (x, y, sym) {
    if (this.board[x][y] !== '.') {
        return false;
    }
    let haveLine = false;
    for (let i = -1; i < 2 && !haveLine; i++) {
        for (let j = -1; j < 2; j++) {
            let m = x - i;
            let n = y - j;
            if (!(i === 0 && j === 0) && this.between(m, n)) {
                let c = this.board[m][n];
                if (c !== '.' &&
                   c !== sym &&
                    this.haveSym(x, y, -i, -j, sym)) {
                    haveLine = true;
                    break;
                }
            }
        }
    }
    return haveLine;
};
BoardConfiguration.prototype.toString = function() {
    return this.board.join("\n");
};
function Player(sym) {
    this.sym = sym;
    this.getSym = function() {
        return this.sym;
    }
}
function ComputerPlayerSeq(sym) {
    Player.call(this, sym);
}
ComputerPlayerSeq.prototype.makeMove = function(boardConf) {
    for(let i = 0; i < boardConf.getM(); i++) {
        for (let j = 0; j < boardConf.getN(); j++) {
            if (boardConf.isCorrect(i, j, this.getSym())) {
                boardConf.setCell(i, j, this.getSym());
                return;
            }
        }
    }
};
function ComputerPlayerRandom(sym) {
    Player.call(this, sym);
    this.planB = new ComputerPlayerSeq(sym);
}
ComputerPlayerRandom.prototype.makeMove = function(boardConf) {
    let x = randomInt(0, boardConf.getM());
    let y = randomInt(0, boardConf.getN());
    let count = 0;
    let m = boardConf.getM();
    while (!boardConf.isCorrect(x, y, this.getSym()) && count < m*m) {
        let x = randomInt(0, boardConf.getM());
        let y = randomInt(0, boardConf.getN());
        count++;
    }
    if (count === m*m) {
        this.planB.makeMove(boardConf);
    } else {
        boardConf.setCell(x, y, this.getSym());
    }
};

function HumanPlayer(sym, steps) {
    Player.call(this, sym);
    this.steps = steps.split(" ").reverse();
    this.planB = new ComputerPlayerRandom(sym);
    this.makeMove = function(boardConf) {
        let y = this.steps.pop();
        let x = this.steps.pop();
        if (boardConf.isCorrect(x, y, this.getSym())) {
            boardConf.setCell(x, y, this.getSym());
        } else {
            this.planB.makeMove(boardConf);
        }
    }
}
/*
let BotSeq = new ComputerPlayerSeq('B');
let BotSeq2 = new ComputerPlayerSeq('W');
let Human = new HumanPlayer('B', "0 0 4 1 3 2 2 3 1 4 0 4");
let board = new BoardConfiguration();
board.board = [ [ '.', '.', '.', '.', '.', '.', '.', '.' ],
    [ '.', 'B', 'B', 'B', 'B', 'B', 'B', 'B' ],
    [ '.', 'W', 'B', 'W', 'W', 'W', 'B', 'W' ],
    [ '.', 'W', 'W', 'W', 'W', 'W', 'W', '.' ],
    [ '.', 'W', '.', 'W', 'B', '.', '.', '.' ],
    [ '.', '.', '.', '.', '.', '.', '.', '.' ],
    [ '.', '.', '.', '.', '.', '.', '.', '.' ],
    [ '.', '.', '.', '.', '.', '.', '.', '.' ] ];
console.log(board.board);
BotSeq.makeMove(board);
console.log(board.board);
BotSeq2.makeMove(board);
console.log(board.board);
Human.makeMove(board);
*/
function GameServer(pl1, pl2, board) {
    this.pl1 = pl1;
    this.pl2 = pl2;
    this.board = board;
}
/**
 * @return {number}
 */
GameServer.prototype.CheckWin = function(sym) {
    let count = 0;
    for (let i = 0; i < this.board.getM(); i++) {
        for (let j = 0; j < this.board.getN(); j++) {
            if (this.board.getCell(i, j) === '.') {
                return 0;
            }
            if (this.board.getCell(i, j) !== sym) {
                continue;
            }
            count++;
        }
    }
    let num = Math.floor((this.board.getN() * this.board.getM())/ 2);
    if (count > num) {
        return 2;
    } else if (count === num) {
        return 1;
    } else {
        return 0;
    }
};
GameServer.prototype.start = function() {
    let checkPl1 = 0;
    /*
    this.pl1.sym = 'X';
    this.pl2.sym = 'O';
     */
    while (!(checkPl1 === 2) && !(checkPl1 === 1) && !(this.CheckWin(this.pl2.getSym()) === 2)) {
        this.pl1.makeMove(this.board);
        //console.log(this.board.board);
        checkPl1 = this.CheckWin(this.pl1.getSym());
        if (checkPl1 === 0) {
            this.pl2.makeMove(this.board);
        }
        //console.log(this.board.board);
    }
    if (checkPl1 === 2) {
        return 1;
    } else if (checkPl1 === 1 && this.CheckWin(this.pl2.getSym()) === 1) {
        return 0;
    } else {
        return 2;
    }
};
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
};
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
};
//убрать выбор символа в конструкторе
let tour = new Tournament(20, [
    new ComputerPlayerRandom('B'),
    new ComputerPlayerRandom('W')]
);
tour.start();
tour.printStats();