This repo contains a chess engine written fully in Java.
It is written from scratch without any chess libraries.

# Features

- bitboards
- dynamic make and unmake move logic
- FEN support
- UCI support
- Iterative deepening
- alpha-beta pruning
- Move ordering
- Transposition table
- ~~Quiescence Search~~


# APIs

There are 3 APIs.

## UCI API

This is the standard chess engine API. It exists so that chess GUIs can communicate with the engine via std-in/out

The UCI protocol has surprisingly poor documentation. The available resources are scattered across different websites, often incomplete, and sometimes contradict each other. This made implementing the API quite challenging.

The main reference I used for the implementation was:

- https://backscattering.de/chess/uci/

## Play API

This is a custom cli API intended for human players.

## Web API

This API is used by the front end. It communicates over HTTP and is essentially an extension of the UCI API with a few additional commands.

## Starting an API

All commands sent via stdin must end with a newline (either Unix `\n` or Windows `\r\n`).

When starting the engine, enter one of the following commands:

- `play` to start the Play API
- `uci` to start the UCI API

To start the Web API, launch the engine with either the `-w` or `--web` flag.