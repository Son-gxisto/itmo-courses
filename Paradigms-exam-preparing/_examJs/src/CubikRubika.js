const edgeSize = 3;
const EdgeNumbers = [0, 1, 2, 3, 4, 5];
function getSides(a, b) {
    return EdgeNumbers.filter(x => (x !== a) && (x !== b));
}
function rotateR(arr, num) {
    let length = arr.length;
    let res = new Array(length);
    for (let i = 0; i < length; i++) {
        res[i] = new Array(length);
    }
    for (let i = 0; i < length; i++) {
        for (let j = length - 1; j >= 0; j--) {
            switch(num) {
                case 0:
                    res[i][length - 1 - j] = arr[0][j][i];
                    break;
                case 1:
                    res[i][length - 1 - j] = arr[j][edgeSize - 1][i];
                    break;
                case 5:
                    res[i][length - 1 - j] = arr[edgeSize - 1][j][i];
                    break;
            }
        }
    }
    return res;
}
function rotateL(arr) {
    let length = arr.length;
    let res = new Array(length);
    for (let i = 0; i < length; i++) {
        res[i] = new Array(length);
    }
    for (let i = 0; i < length; i++) {
        for (let j = length - 1; j >= 0; j--) {
            res[j][i] = arr[i][length - 1 - j];
        }
    }
    return res;
}

function Edge(sym, indFr, indB, sideCoord) {
    this.ed = cdeJlaTb_rpaHb(sym);
    this.sideEdge = getSides(indFr, indB);
    this.sideCoord = sideCoord;
}

function Cube() {
    this.col = ['W', 'B', 'O', 'G', 'R', 'Y'];
}
Cube.prototype.ind = ['U', 'F', 'R', 'B', 'L', 'D'];
Cube.prototype.rotateL = function() {
    let t = this.col[1];
    this.col.copyWithin(1, 2, 5);
    this.col[4] = t;
}
Cube.prototype.rotateR = function() {
    let t = this.col[4];
    this.col.copyWithin(2, 1, 4);
    this.col[1] = t;
}
/*
 W
BOGR
 Y
// W B O G R Y ->
*/
Cube.prototype.rotateU = function() {

}
let cube = new Cube();
console.log(cube.col);
cube.rotateR();
console.log(cube.col);
cube.rotateL();
console.log(cube.col);
function Rubik() {
    this.cube = new Array(edgeSize);
    for (let i = 0; i < edgeSize; i++) {
        this.cube[i] = new Array(edgeSize);
        for (let j = 0; j < edgeSize; j++) {
            this.cube[i][j] = new Array(edgeSize);
            for (let k = 0; k < edgeSize; k++) {
                this.cube[i][j][k] = new Cube();
            }
        }
    }
}
