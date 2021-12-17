'use strict';
//parser ne vesgda parsit
const OrS = '|';//'∨';
const AndS = '&'; //'∧';
const NotS = '~';//'¬';
const VarS = 'V';
const ConstS = 'C';

function Operation(...args) {
    if (this.sym === OrS || this.sym === AndS) {
        this.operands = [];
        for (let i = 0; i < args.length; i++) {
            if (args[i].sym === this.sym) {
                this.operands.push(...args[i].operands);
            } else {
                this.operands.push(args[i]);
            }
        }
    } else {
        this.operands = args;
    }
}
Operation.prototype.toString = function() {
    let res = [];
    const sym = this.sym;
    if (this.operands.length > 1) {
        for (let i = 0; i < this.operands.length; i++) {
            res.push(this.operands[i].toString());
        }
        return '(' + res.join(' ' + sym + ' ') + ')';
    } else {
        if (sym === VarS || sym === ConstS) {
            return this.operands[0].toString();
        }
        return this.sym + this.operands[0].toString();
    }
};
Operation.prototype.pushArgs = function(haveN) {
    let res = [];
    for (let i = 0; i < this.operands.length; i++) {
        res.push(this.operands[i].push(haveN));
    }
    return res;
};

function distributOr(v) {
    let andO = [];
    let notAndO = [];
    for (let i = 0; i < v.operands.length; i++) {
        if (v.operands[i].sym === AndS) {
            andO.push(v.operands[i]);
        } else {
            notAndO.push(v.operands[i]);
        }
    }
    switch(andO.length) {
        case 0:
            return v;
        case 1:
            let res = [];
            for (let i = 0; i < andO[0].operands.length; i++) {
                res.push(new Or(andO[0].operands[i], ...notAndO));
            }
            return new And(...res);
        default:
            let res2 = [];
            for (let i = 1; i < andO.length; i++) {
                notAndO.push(andO[i]);
            }
            for (let i = 0; i < andO[0].operands.length; i++) {
                res2.push(distributOr(new Or(andO[0].operands[i], ...notAndO)));
            }
            return new And(...res2);
    }
}
function makeOperation(sym, push) {
    let res = function(...args) {
        Operation.call(this, ...args);
    };
    res.prototype = Object.create(Operation.prototype);
    res.prototype.sym = sym;
    res.prototype.push = push;
    return res;
}
let Not = makeOperation(
    NotS,
    function(haveN = false) {
        return this.pushArgs(!haveN)[0];
    }
);
let Or = makeOperation(
    OrS,
    function(haveN = false) {
        let res = this.pushArgs(haveN);
        if (haveN) {
            return new And(...res);
        }
        return distributOr(new Or(...res));
    }
);
let And = makeOperation(
    AndS,
    function(haveN = false) {
        let res = this.pushArgs(haveN);
        if (haveN) {
            return distributOr(new Or(...res));
        }
        return new And(...res);
    }
);
let Variable = makeOperation(
    VarS,
    function(haveN = false) {
        if (haveN) {
            return new Not(this);
        }
        return this;
    }
);
let Constant = makeOperation(
    ConstS,
    function(haveN = false) {
        return new Constant(+(+haveN !== this.operands[0]));
    }
);

const MyError = function (message, name) {
    const err = function (...args) {
        this.message = message(...args);
    };
    err.prototype = new Error;
    err.prototype.name = name;
    return err;
};

const MissingBracketError = MyError(
    (index) => "Missing bracket on position " + index,
    "MissingBracketError"
);

const MissingOperationError = MyError(
    (index) => "Missing operation on position " + index,
    "MissingOperationError"
);

const TooMuchArgumentsError = MyError(
    (symbol) => "Too much arguments beginning from " + symbol,
    "TooMuchArgumentsError"
);

const MissingArgumentError = MyError(
    (index) => "Missing argument on position " + index,
    "MissingArgumentError"
);

const IllegalSymbolError = MyError(
    (symbol) => "Illegal symbol " + symbol,
    "IllegalSymbolError"
);

const OPERATIONS = {
    '|': Or,
    '∨': Or,
     '&': And,
    '∧': And,
    '~': Not,
    '¬': Not
};
const UNARY_OPERATIONS = {
    '~': Not,
    '¬': Not
};

const COUNTARGS = {
    '|': 2000000,
    '∨': 2000000,
    '&': 2000000,
    '∧': 2000000,
    '~': 1,
    '¬': 1
};

const isNumber = function (number) {
    let i = 0;
    if (number[i] === "-") {
        i++;
    }
    if (i === number.length) {
        return false;
    }
    while (i < number.length) {
        if (number[i] < "0" || number[i] > "9") {
            return false;
        }
        i++;
    }
    return true;
};
function makeBrackets(source) {
    let res = "";
    while (source.curInd < source.expression.length) {
        if (source.expression[source.curInd] in UNARY_OPERATIONS) {
            res+= '(' + source.expression[source.curInd] + ' ';
            source.skipWhitespace();
            if (source.expression[source.curInd + 1] === '(') {
                res += '(';
                let balance = 1;
                source.curInd += 2;
                while (balance !== 0 && source.expression.length > source.curInd) {
                    if (source.expression[source.curInd] in UNARY_OPERATIONS) {
                        res += source.makeBrackets();
                    }
                    if (source.curInd >= source.expression.length) {
                        res += ')';
                        return res;
                    }
                    res += source.expression[source.curInd];
                    balance = balance - (+(source.expression[source.curInd] === ')')) + (+(source.expression[source.curInd] === '('));
                    source.curInd++;
                }
                if (balance === 0) {
                    res += ')';
                    return res;
                }
            } else {
                source.curInd++;
                source.skipWhitespace();
                while (source.expression[source.curInd] !== ' ' && source.curInd < source.expression.length) {
                    res += source.expression[source.curInd];
                    source.curInd++;
                }
                res += ')';
                //return res;
            }
        } else {
            res += source.expression[source.curInd];
            source.curInd++;
        }
    }
    source.curInd = 0;
    return res;
}
const Source = function(expression) {
    this.expression = expression;
    this.curEl;
    this.curInd = 0;
};

Source.prototype.nextElement = function() {
    this.skipWhitespace();
    if (this.expression[this.curInd] === "(" || this.expression[this.curInd] === ")") {
        this.curEl = this.expression[this.curInd];
        this.curInd++;
    } else {
        let endIndex = this.curInd;
        while (endIndex < this.expression.length && this.expression[endIndex] !== "(" && this.expression[endIndex] !== ")" && this.expression[endIndex] !== " ") {
            endIndex++;
        }
        this.curEl = this.expression.slice(this.curInd, endIndex);
        this.curInd = endIndex;
    }
};

Source.prototype.skipWhitespace = function () {
    while (this.curInd < this.expression.length && this.expression[this.curInd] === " ") {
        this.curInd++;
    }
};

Source.prototype.isEnd = function() {
    this.skipWhitespace();
    return this.curInd === this.expression.length;
};

Source.prototype.makeBrackets = function() {
    let res = "";
    while (this.curInd < this.expression.length) {
        if (this.expression[this.curInd] in UNARY_OPERATIONS) {
            res+= '(' + this.expression[this.curInd] + ' ';
            this.skipWhitespace();
            if (this.expression[this.curInd + 1] === '(') {
                res += '(';
                let balance = 1;
                this.curInd += 2;
                while (balance !== 0 && this.expression.length > this.curInd) {
                    if (this.expression[this.curInd] in UNARY_OPERATIONS) {
                        res += this.makeBrackets();
                    }
                    if (this.curInd < this.expression.length) {
                        res += this.expression[this.curInd];
                    }
                    balance = balance - (+(this.expression[this.curInd] === ')')) + (+(this.expression[this.curInd] === '('));
                    this.curInd++;
                }
                if (balance === 0) {
                    res += ')';
                    return res;
                }
            } else {
                this.curInd++;
                this.skipWhitespace();
                while (this.expression[this.curInd] !== ' ' && this.curInd < this.expression.length) {
                    res += this.expression[this.curInd];
                    this.curInd++;
                }
                res += ')';
                return res;
            }
        } else {
            res += this.expression[this.curInd];
            this.curInd++;
        }
    }
    return res;
};

Source.prototype.parseExpression = function() {
    this.skipWhitespace();
    let res = this.parse();
    if (!this.isEnd()) {
        throw new IllegalSymbolError(this.curEl);
    }
    return res;
};

Source.prototype.getExpression = function() {
    if (this.curEl === "(") {
        let startIndex = this.curInd - 1;
        this.nextElement();
        while (this.curInd < this.expression.length && this.curEl !== ")") {
            if (this.curEl === "(") {
                this.getExpression();
            }
            this.nextElement();
        }
        if (this.curEl !== ")") {
            throw new MissingBracketError(this.curInd);
        }
        let endIndex = this.curInd;
        return this.expression.slice(startIndex, endIndex);
    } else {
        return this.curEl;
    }
};

Source.prototype.parse = function() {
    this.nextElement();
    if (this.curEl in UNARY_OPERATIONS) {
        this.nextElement();
        return new OPERATIONS[this.curEl](parseExpr(this.getExpression()));
    }
    if (this.curEl === "(") {
        this.nextElement();
        let elementsStack = [];
        if (!(this.curEl in OPERATIONS)) {
            elementsStack.push(parseExpr(this.getExpression()));
            this.nextElement();
        }
        if (this.curEl in OPERATIONS) {
            let oper = this.curEl;

            this.nextElement();
            for (let i = 0; i < this.expression.length && this.curEl !== ")"; i++) {
                elementsStack.push(parseExpr(this.getExpression()));
                this.nextElement();
                if (this.curEl in OPERATIONS) {
                    this.nextElement();
                }
            }

            if (this.curEl !== ")") {
                throw new MissingBracketError(this.curInd);
            }

            if (elementsStack.length > COUNTARGS[oper]) {
                throw new TooMuchArgumentsError(this.curEl);
            } else if (elementsStack.length < 1) {
                throw new MissingArgumentError(this.curInd);
            }

            return new OPERATIONS[oper](...elementsStack);
        } else {
            throw new MissingOperationError(this.curInd);
        }
    } else if (isNumber(this.curEl)) {
        return new Constant(parseInt(this.curEl));
    } else {
        return new Variable(this.curEl);
    }
};

const parseExpr = function(expression) {
    let source = new Source(expression);
    return source.parseExpression();
};
function parse(expression) {
    return parseExpr(makeBrackets(new Source(expression)));
}

let x = new Not(
    new And(
        new Variable('x'), new Not(
            new Or(new Constant(0), new And(new Variable('a'), new Variable('b')), new And(new Not(
                new And(
                    new Variable('x'), new Not(
                        new Or(new Constant(0), new And(new Variable('a'), new Variable('b')), new And(new Variable('y'), new Variable('z'), new Variable('m')))
                    ), new And(new Not(new Variable('e')), new Variable('z'), new Or(new Constant(1), new Constant(0)))
                )
            ),new Variable('y'), new Variable('z'), new Variable('m')))
        ), new And(new Not(new Variable('e')), new Variable('z'), new Or(new Constant(1), new Constant(0)))
    )
);
let x2 = parse('¬(x ∧ ¬(0 | (a ∧ b) | (y ∧ z ∧ m & ¬(x ∧ ¬(0 | (a ∧ b) | (y ∧ z ∧ m)) ∧ ¬e ∧ z))) ∧ ¬e ∧ z)');
let x3 = parse('(~x | ¬(a ∧ ¬b))');
let x4 = parse('~(z | (b & c & ~e))');
//(~(~a & ~b) | c)
//( (~ ((~ a) & ~b)) | c)
console.log('use | ∨ for Or, ∧ & for and, - ~ for Not');
console.log('для проверки корректности можно использовать \nhttps://programforyou.ru/calculators/postroenie-tablitci-istinnosti-sknf-sdnf');
console.log('вставляя выражение и кнф');
console.log('x: '  + x2.toString());
console.log('cnf of x: ' + x2.push().toString());
console.log('x2: ' + x.toString());
console.log('cnf x2: ' + x.push().toString());
console.log('x3:" ' + x3.toString());
console.log('cnf x3: ' + x3.push().toString());
console.log('x4: ' + x4.toString());
console.log('cnf x4: ' + x4.push().toString());