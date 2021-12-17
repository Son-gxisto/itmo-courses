"use strict"
const BOARD_SIZE = 9;
function union(setA, setB) {
    var _union = new Set(setA);
    for (var elem of setB) {
        _union.add(elem);
    }
    return _union;
}
let sudoku = [
    [ 0, 0, 0, 0, 6, 0, 7, 0, 0 ],
    [ 0, 5, 9, 0, 0, 0, 0, 0, 0 ],
    [ 0, 1, 0, 2, 0, 0, 0, 0, 0 ],
    [ 0, 0, 0, 1, 0, 0, 0, 0, 0 ],
    [ 6, 0, 0, 5, 0, 0, 0, 0, 0 ],
    [ 3, 0, 0, 0, 0, 0, 4, 6, 0 ],
    [ 0, 0, 0, 0, 0, 0, 0, 0, 0 ],
    [ 0, 0, 0, 0, 0, 0, 0, 9, 1 ],
    [ 8, 0, 0, 7, 4, 0, 0, 0, 0 ]
];

function getRowV(rowI, puzzle) {
    let values = new Set();
    for (let i = 0; i < BOARD_SIZE; i++) {
        if (puzzle[rowI][i] !== 0) {
            values.add(puzzle[rowI][i]);
        }
    }
    return values;
}

function getColV(colI, puzzle) {
    let values = new Set();
    for (let i = 0; i < BOARD_SIZE; i++) {
        if (puzzle[i][colI] !== 0) {
            values.add(puzzle[i][colI]);
        }
    }
    return values;
}

function getSquareV(rowI, colI, puzzle) {
    let values = new Set();
    let mnc = 3 * Math.trunc(colI/3);
    let mnr = 3 * Math.trunc(rowI/3);
    for (let i = 0; i < BOARD_SIZE/3; i++) {
        for (let j = 0; j < BOARD_SIZE/3; j++) {
            if (puzzle[i + mnr][j + mnc] !== 0) {
                values.add(puzzle[i + mnr][j + mnc]);
            }
        }
    }
    return values;
}

function findPossibleV(rowI, colI, puzzle) {
    let values = union((getRowV(rowI, puzzle)), getColV(colI, puzzle));
    values = union(values, getSquareV(rowI, colI, puzzle));
    let res = new Set();
    for (let i = 1; i < BOARD_SIZE + 1; i++) {
        if (!(values.has(i))) {
            res.add(i);
        }
    }
    return res;
}

function solve(puzzle) {
    let minRow;
    let minCol = -1;
    let minValues = new Set();
    while (true) {
        minRow = -1;
        for (let rowI = 0; rowI < BOARD_SIZE; rowI++) {
            for (let colI = 0; colI < BOARD_SIZE; colI++) {
                if (puzzle[rowI][colI] !== 0) {
                    continue;
                }
                let possibleV = findPossibleV(rowI, colI, puzzle);
                let possibleVcount = possibleV.size;
                if (possibleVcount === 0) {
                    return false;
                }
                if (possibleVcount === 1) {
                    for (let elem of possibleV) {
                        puzzle[rowI][colI] = elem;
                    }
                }
                if (minRow < 0 || possibleVcount < minValues.size) {
                    minRow = rowI;
                    minCol = colI;
                    minValues = possibleV;
                }
            }
        }
        if (minRow === -1) {
            return true;
        } else if (1 < minValues.size) {
            break;
        } else {
            for (let elem of minValues) {
                puzzle[minRow][minCol] = elem;
            }
        }
    }
    for (let elem of minValues) {
        let puzzleCopy = new Array(BOARD_SIZE);
        for (let i = 0; i < BOARD_SIZE; i++) {
            puzzleCopy[i] = new Array(BOARD_SIZE);
            for (let j = 0; j < BOARD_SIZE; j++) {
                puzzleCopy[i][j] = puzzle[i][j];
            }
        }
        puzzleCopy[minRow][minCol] = elem;
        if (solve(puzzleCopy)) {
            for (let i = 0; i < BOARD_SIZE; i++) {
                for (let j = 0; j < BOARD_SIZE; j++) {
                    puzzle[i][j] = puzzleCopy[i][j];
                }
            }
            return true;
        }
    }
    return false;
}

console.log(solve(sudoku));
console.log(sudoku);