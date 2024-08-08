package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import static chess.ChessPiece.PieceType.*;
import static ui.EscapeSequences.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class GamePlayUI {

    private static ChessGame currGame;

    private static final int GAME_BOARD_DIMENSIONS = 8;
    private static final String[] TOP_HEADERS = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] SIDE_HEADERS = {"1", "2", "3", "4", "5", "6", "7", "8"};

    public GamePlayUI(ChessGame game){
        currGame = game;
    }

    public String help(){
        return """
                \texit - exit the game""";
    }

    public void draw() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawBoard(out);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void drawBoard(PrintStream out){
        //draw the initial board
        drawHeaders(out);
        drawSquares(out, currGame);
        drawHeaders(out);
        drawHorizontalLine(out);
        drawHeadersUpsideDown(out);
        drawUpsideDown(out, currGame);
        drawHeadersUpsideDown(out);
    }

    private static void drawHeadersUpsideDown(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (int boardCol = 8; boardCol > 0; boardCol--) {
            drawHeader(out, TOP_HEADERS[boardCol - 1]);
        }
        out.println();
    }

    private static boolean drawSquare(boolean boardColor, PrintStream out, ChessGame game, int squareRow, int boardCol){
        boolean result = false;
        if (boardColor){
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
        else{
            out.print(SET_BG_COLOR_TAN);
            result = true;
        }
        printCharacter(out, game, squareRow, boardCol);
        return result;
    }

    private static void drawUpsideDown(PrintStream out, ChessGame game){
        boolean boardColor;
        for (int squareRow = 1; squareRow <= GAME_BOARD_DIMENSIONS; squareRow++) {
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" ");
            out.print(SIDE_HEADERS[squareRow - 1]);
            out.print(" ");
            boardColor = squareRow % 2 == 0;
            for (int boardCol = 8; boardCol > 0; boardCol--) {
                boardColor = drawSquare(boardColor, out, game, squareRow, boardCol);
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" ");
            out.print(SIDE_HEADERS[squareRow - 1]);
            out.print(" ");
            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY.repeat(GAME_BOARD_DIMENSIONS + 2));
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.println();
    }

    private static void drawSquares(PrintStream out, ChessGame game){
        boolean boardColor;
        for (int squareRow = 8; squareRow > 0; squareRow--) {
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" ");
            out.print(SIDE_HEADERS[squareRow - 1]);
            out.print(" ");
            boardColor = squareRow % 2 == 1;
            for (int boardCol = 1; boardCol <= GAME_BOARD_DIMENSIONS; boardCol++) {
                boardColor = drawSquare(boardColor, out, game, squareRow, boardCol);
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(" ");
            out.print(SIDE_HEADERS[squareRow - 1]);
            out.print(" ");
            out.println();
        }
    }

    private static void printCharacter(PrintStream out, ChessGame game, int row, int col){
        ChessBoard currBoard = game.getBoard();
        ChessPosition pos = new ChessPosition(row, col);
        if (currBoard.getPiece(pos) == null){
            out.print(EMPTY);
        }
        else{
            if (currBoard.getPiece(pos).getPieceType().equals(KING)){
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_KING);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_KING);
                }
            }
            else if (currBoard.getPiece(pos).getPieceType().equals(QUEEN)){
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_QUEEN);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_QUEEN);
                }
            }
            else if (currBoard.getPiece(pos).getPieceType().equals(BISHOP)){
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_BISHOP);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_BISHOP);
                }
            }
            else if (currBoard.getPiece(pos).getPieceType().equals(KNIGHT)){
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_KNIGHT);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_KNIGHT);
                }
            }
            else if (currBoard.getPiece(pos).getPieceType().equals(ROOK)){
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_ROOK);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_ROOK);
                }
            }
            else {
                if (currBoard.getPiece(pos).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                    out.print(SET_TEXT_COLOR_BLACK);
                    out.print(BLACK_PAWN);
                }
                else{
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(WHITE_PAWN);
                }
            }
        }
    }

    private static void drawHeaders(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
        for (int boardCol = 0; boardCol < GAME_BOARD_DIMENSIONS; ++boardCol) {
            drawHeader(out, TOP_HEADERS[boardCol]);
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String text){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" ");
        out.print(text);
        out.print(" ");
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
