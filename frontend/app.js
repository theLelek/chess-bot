const pieceNames = new Map([

["p", "black_pawn"],
["r", "black_rook"],
["n", "black_knight"],
["b", "black_bishop"],
["q", "black_queen"],
["k", "black_king"],

["P", "white_pawn"],
["R", "white_rook"],
["N", "white_knight"],
["B", "white_bishop"],
["Q", "white_queen"],
["K", "white_king"]

]);

let moves = [];

let lastFen = null;
let movesSinceLastFen = [];

//TODO: implement promotion
//TODO: implement Checkmate and draw


let field;

let startCoordinates = null;
let endCoordinates = null;


const boardSize = 8;
let isBotWhite = false;
let isWhiteToMove = true;



initializeField();

createBoard();

renderBoard();






function initializeField(){
    field = [
        ['r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'],
        ['p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        ['P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'],
        ['R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'],
    ];

    movesSinceLastFen = [];

    lastFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    sendMessage("position startpos").then((position) => {
        if(position.startsWith("ERROR")){
            alertNoInternetMessage();
            //window.location.reload();
        }
    });

    moves = [];

    if(isBotWhite){
        turnField();
    }
    renderBoard();
}

function alertNoInternetMessage(){
    window.alert("Something went wrong, please check your internet connection.");
}

function turnField(){
    field = field.reverse();
    for(let i = 0; i < field.length; i++){
        field[i] = field[i].reverse();
    }
}

function createBoard(){
    let board = document.getElementById("board");
    board.innerHTML = "";

    const windowHeight = window.innerHeight;
    const windowWidth = window.innerWidth;

    if(windowHeight < windowWidth){
        board.style.height = windowHeight*0.8 + "px";
        board.style.width = windowHeight*0.8 + "px";
        board.style.margin = "5vh";
    } else {
        board.style.height = windowWidth + "px";
        board.style.width = windowWidth + "px";
        board.style.margin = "0px";
    }

    for(let i = 0; i < boardSize; i++){
        let white = i%2;

        for(let j = 0; j < boardSize; j++){
            const col = !isBotWhite ? i : Math.abs(i - 7);
            const row = !isBotWhite ? j : Math.abs(j - 7);

            let buttonElement = document.createElement("button");

            buttonElement.dataset.number = col * boardSize + row;
            buttonElement.dataset.coordinates = getNotationFromCoordinates(j,i);
            buttonElement.dataset.col = i;
            buttonElement.dataset.row = j;

            white = !white;


            buttonElement.classList.add("field");

            buttonElement.addEventListener("click", function(){
                fieldClicked(buttonElement);
            })


            if(white){
                buttonElement.classList.add("whiteField");
            } else{
                buttonElement.classList.add("blackField");
            }

            


            board.appendChild(buttonElement);
        }

        
    }
}

function getNotationFromCoordinates(x, y){
    const char = String.fromCharCode(x + 97);
    return char + (y - 8)*-1;
}


async function makeBotMove() {
    let response = await sendMessage("go");
    if(response.startsWith("ERROR")){
        console.log("an error occurred ", response);
    } else if(response.startsWith("bestmove ")){
        response = response.slice(9);
        movesSinceLastFen.push(response);
        makeMove(response);
    }
}

async function fieldClicked(field){

    let coordinates = field.dataset.coordinates;

    if(getFieldValueByNotation(coordinates) === null && startCoordinates === null){
        return;
    }

    if(startCoordinates === null && isCharLowerCase(getFieldValueByNotation(coordinates)) === isWhiteToMove) return;

    if(startCoordinates === null){
        startCoordinates = coordinates;
    } else if(startCoordinates === coordinates){
        startCoordinates = null;
    } else{
        endCoordinates = coordinates;

        let madeMove = await move();
        startCoordinates = null;
        endCoordinates = null;
        if(!madeMove){
            return;
        }
        await makeBotMove();
        await sendPosition();

    }
}


function isCharLowerCase(testCharacter){
    return testCharacter.toLowerCase() === testCharacter;
}

function turnBoard(){
    isBotWhite = !isBotWhite;
    console.log(isBotWhite);
    turnField();
    createBoard();
    renderBoard();
}

function getFieldValueByNotation(notation) {
    const col = !isBotWhite ? notation.charCodeAt(0) - 'a'.charCodeAt(0) : Math.abs(notation.charCodeAt(0) - 'a'.charCodeAt(0) - 7);
    const row = !isBotWhite ? 8 - parseInt(notation[1]) : notation[1] -1;

    return field[row][col];
}

function setFieldValueByNotation(notation, toSet) {
    const col = !isBotWhite ? notation.charCodeAt(0) - 'a'.charCodeAt(0) : Math.abs(notation.charCodeAt(0) - 'a'.charCodeAt(0) - 7);
    const row = !isBotWhite ? 8 - parseInt(notation[1]) : notation[1] -1;
    field[row][col] = toSet;
}


async function move(){
    let message = await sendMessage("legal-moves");
    while(message.startsWith("ERROR")){
        alertNoInternetMessage();
        message = await sendMessage("get possible moves");
    }
    console.log("got legal moves");
    let possibleMoves = JSON.parse(message);


    let possibleStrings = possibleMoves.map(value => {
        return getNotationFromCoordinates(value.from.x, value.from.y) + getNotationFromCoordinates(value.to.x, value.to.y);
    });


    let moveString = startCoordinates + endCoordinates;

    if(!possibleStrings.includes(moveString)){
        console.log("invalidMove!");
        return false;
    }


    let result;
    if(isPromotion()) {
        result = await sendMove(startCoordinates, endCoordinates, isWhiteToMove ? "Q":"q");
    } else {
        result = await sendMove(startCoordinates, endCoordinates);
    }

    let toReturn = false;

    if(result === "ERROR invalid move"){
        console.log("invalid move");
    } else if(result.startsWith("ERROR")){
        console.log("an error occurred: " + result);
    } else{
        makeMove();
        renderMove(startCoordinates, endCoordinates);
        moves.push(moveString);
        toReturn = true;
    }

    console.log("move finished");
    return toReturn;
}

function makeMove(move){
    if(move !== undefined){
        startCoordinates = move.slice(0,2);
        endCoordinates = move.slice(2);
        console.log(startCoordinates);
        console.log(endCoordinates);
    }

    let piece = getFieldValueByNotation(startCoordinates);
    let castle = false;
    if(piece.toLowerCase() === "k"){
        castle = true;
        if (startCoordinates === "e1" && endCoordinates === "g1") {
            makeSmallRochade("w");
        } else if (startCoordinates === "e1" && endCoordinates === "c1") {
            makeBigRochade("w");
        } else if (startCoordinates === "e8" && endCoordinates === "g8") {
            makeSmallRochade("b");
        } else if (startCoordinates === "e8" && endCoordinates === "c8") {
            makeBigRochade("b");
        } else{
            castle = false;
        }
    }
    if(!castle){
        if(isPromotion()){
            piece = isWhiteToMove ? "Q" : "q";
        }
        setFieldValueByNotation(endCoordinates, piece);
        setFieldValueByNotation(startCoordinates, null);
    }
    renderMove(startCoordinates, endCoordinates);

    isWhiteToMove = !isWhiteToMove;

    if(move !== undefined){
        startCoordinates = null;
        endCoordinates = null;
    }
}

function isPromotion(){
    const piece = getFieldValueByNotation(startCoordinates);
    if(piece === "p" && endCoordinates.endsWith("1")){
        return true;
    } else if(piece === "P" && endCoordinates.endsWith("8")){
        return true;
    }
    return false;
}

function makeSmallRochade(color){
    if (color === "w") {
        setFieldValueByNotation("e1", null);
        setFieldValueByNotation("h1", null);
        setFieldValueByNotation("g1", "K");
        setFieldValueByNotation("f1", "R");
        renderMove(startCoordinates, endCoordinates, ["h1", "f1"]);
    } else {
        setFieldValueByNotation("e8", null);
        setFieldValueByNotation("h8", null);
        setFieldValueByNotation("g8", "k");
        setFieldValueByNotation("f8", "r");
        renderMove(startCoordinates, endCoordinates, ["h8", "f8"]);
    }
}

function makeBigRochade(color){
    if (color === "w") {
        setFieldValueByNotation("e1", null);
        setFieldValueByNotation("a1", null);
        setFieldValueByNotation("c1", "K");
        setFieldValueByNotation("d1", "R");
        renderMove(startCoordinates, endCoordinates, ["a1", "d1"]);
    } else {
        setFieldValueByNotation("e8", null);
        setFieldValueByNotation("a8", null);
        setFieldValueByNotation("c8", "k");
        setFieldValueByNotation("d8", "r");
        renderMove(startCoordinates, endCoordinates, ["a8", "d8"]);
    }
}

function renderMove(startCoordinates, endCoordinates, additionalCoordinates = []){
    let buttons = board.querySelectorAll(".field");

    buttons.forEach(button => {
        if(button.dataset.coordinates === startCoordinates || button.dataset.coordinates === endCoordinates || additionalCoordinates.includes(button.dataset.coordinates)){
            let row = button.dataset.row;
            let col = button.dataset.col;

            let piece = field[col][row];

            const pieceName = pieceNames.get(piece);


            button.innerHTML = "";

            if (piece !== null) {

                let img = document.createElement("img");

                
                img.src = "img/" + pieceName + ".png";
                

                button.appendChild(img);
            } 
        }
    });
}

function renderBoard(checkmated) {
    let buttons = document.querySelectorAll(".field");

    buttons.forEach(button => {

        let row = button.dataset.row;
        let col = button.dataset.col;

        let piece = field[col][row];

        const pieceName = pieceNames.get(piece);


        button.innerHTML = "";

        if (piece !== null) {

            let img = document.createElement("img");

            
            img.src = "img/" + pieceName + ".png";


            if(checkmated === piece){
                img.style.transform = "rotate(-90deg)"
            }

            button.appendChild(img);
        }
    });
}

function reset() {
    console.log("reseted");

    initializeField();

    renderBoard();


    startCoordinates = null;
    endCoordinates = null;
}




async function sendMove(from, to, ...promotion) {
    let stringMove = from + to;
    if (promotion !== undefined) stringMove += promotion;

    movesSinceLastFen.push(stringMove);

    return await sendPosition();
}

async function sendPosition(){
    const message = "position fen " +  lastFen + " moves " + movesSinceLastFen.join(" ");

    console.log(message);

    let responseGotten;
    try{
        responseGotten = await sendMessage(message);
    } catch (error){
        responseGotten = "ERROR " + error.message;
    }

    return responseGotten;
}

async function sendMessage(message) {
    return fetch("http://127.0.0.1:8081/chess", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: message
    })
        .then(async function (response) {
            if (!response.ok) {
                return "ERROR " + response.statusText;
            }
            return response.text();
        })
        .catch(function (error) {
            return "ERROR " + error.message;
        });
}


function updateFieldFromFen(fen) {
    const boardString = fen.split(" ")[0];

    isWhiteToMove = boardString.split(" ")[1] === "w";

    const splitBoardString = boardString.split("/");

    field = [];

    for(let i = 0; i< boardSize; i++){
        const string = splitBoardString[i];

        let arr = [];

        for(let j = 0; j<string.length; j++){
            let num = parseInt(string.charAt(j));
            if(isNaN(num)){
                arr.push(string.charAt(j));
            } else{
                for(let k = 0; k < num; k++){
                    arr.push(null);
                }
            }
        }

        field.push(arr);
    }
}


function fieldToFen(field) {
    const rows = [];

    for (let i = 0; i < field.length; i++) {
        const row = field[i];
        let rowString = "";
        let emptyCount = 0;

        for (let j = 0; j < row.length; j++) {
            const cell = row[j];

            if (cell === null) {
                emptyCount++;
            } else {
                if (emptyCount > 0) {
                    rowString += emptyCount;
                    emptyCount = 0;
                }
                rowString += cell;
            }
        }

        if (emptyCount > 0) {
            rowString += emptyCount;
        }

        rows.push(rowString);
    }

    return rows.join("/");
}

function fieldToFullFen(field, activeColor = "w", castling = "KQkq", enPassant = "-", halfmove = 0, fullmove = 1) {
    const boardString = fieldToFen(field);
    return `${boardString} ${activeColor} ${castling} ${enPassant} ${halfmove} ${fullmove}`;
}

function saveGame() {
    localStorage.setItem("chessBoard", fieldToFullFen(field, isBotWhite ? "w":"b"));
    console.log("game saved");
}


function loadGame() {
    const saved = localStorage.getItem("chessBoard");
    if (!saved) return;
    if(saved === "") return;

    updateFieldFromFen(saved);
    console.log("game loaded");

    sendMessage("position " + localStorage.getItem("chessBoard")).then(function(response){
        if(response.startsWith("ERROR")) {
            initializeField();
            window.alert("Connection refused");
        }


    });


    createBoard();
    renderBoard();
}
