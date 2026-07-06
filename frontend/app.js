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


let field;

let startCoordinates = null;
let endCoordinates = null;


const boardSize = 8;
const maxThinkTimeForEngineInMilliseconds = 4000;
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

    if(isBotWhite){
        turnField();
    }
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

            let field = document.createElement("button");

            const squareNumber = col*boardSize + row;
            const char = String.fromCharCode(row + 97);


            field.dataset.number = squareNumber;
            field.dataset.coordinates = char + (col - 8)*-1;
            field.dataset.col = i;
            field.dataset.row = j;

            white = !white;


            field.classList.add("field");

            field.addEventListener("click", function(){
                fieldClicked(field);
            })


            if(white){
                field.classList.add("whiteField");
            } else{
                field.classList.add("blackField");
            }

            


            board.appendChild(field);
        }

        
    }
}


function fieldClicked(field){
    //if(isBotWhite === isWhiteToMove) return;
    
    let coordinates = field.dataset.coordinates;

    if(getFieldValueByNotation(coordinates) === null && startCoordinates === null){
        return;
    }

    if(startCoordinates === null && isCharLowerCase(getFieldValueByNotation(coordinates)) === isWhiteToMove) return;

    if(startCoordinates === null){
        startCoordinates = coordinates;
    } else if(startCoordinates == coordinates){
        startCoordinates = null;
    } else{
        endCoordinates = coordinates;

        move(null);
        isWhiteToMove = !isWhiteToMove;
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


function move(possibleMoves){
    console.log("move: " + startCoordinates + " to " + endCoordinates);

    let moveString = startCoordinates + ";" + endCoordinates;

    if(possibleMoves != null && !possibleMoves.contains(moveString)){
        console.log("invalidMove!");
    }

    if (startCoordinates === "e1" && endCoordinates === "g1") {
        makeSmallRochade("w");
    } else if (startCoordinates === "e1" && endCoordinates === "c1") {
        makeBigRochade("w");
    } else if (startCoordinates === "e8" && endCoordinates === "g8") {
        makeSmallRochade("b");
    } else if (startCoordinates === "e8" && endCoordinates === "c8") {
        makeBigRochade("b");
    } else{
        makeMove();
        renderMove(startCoordinates, endCoordinates);
    }
    sendMove(startCoordinates, endCoordinates);

    startCoordinates = null;
    endCoordinates = null;
}

function makeMove(){
    piece = getFieldValueByNotation(startCoordinates);
    setFieldValueByNotation(endCoordinates, piece);
    setFieldValueByNotation(startCoordinates, null);   
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



function renderBoard() {
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
            

            button.appendChild(img);
        }
    });
}

function reset() {
    console.log("reseted");

    initializeField();

    renderBoard();

    sendMessage("position startpos").then(function(response){
        if(response.startsWith("ERROR")){
            window.alert("Connection failed!");
        }

    })


    startCoordinates = null;
    endCoordinates = null;
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
            window.alert("Connection refused"); //TODO do not now right now if every error starts with ERROR
        }


    });


    createBoard();
    renderBoard();
}

async function sendMove(from, to, ...promotion) {
    let stringMove = from + " " + to;
    if (promotion !== undefined) stringMove += promotion;

    let responseGotten;
    try{
        responseGotten = await sendMessage(stringMove);
    } catch (error){
        responseGotten = error.message;
    }

    console.log(typeof responseGotten);

    console.log(responseGotten);

    return responseGotten;
}

async function sendMessage(message = "isready") {
    return fetch("http://127.0.0.1:8081/chess", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: message
    })
        .then(async function (response) {
            if (!response.ok) {
                return "HTTP Error: " + response.statusText;
            }
            return response.text();
        })
        .catch(function (error) {
            return error.message;
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