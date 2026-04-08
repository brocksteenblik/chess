package ui;



import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final String EMPTY = "  ";

    public static void drawWhitePlayerBoard(ChessGame game) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chess.ChessBoard board = game.getBoard();
        drawWhiteBorder(out);
        drawBlackBackline(out, "WHITE", board);
        drawBlackPawnLine(out, "WHITE", board);
        drawMiddleSquares(out, "WHITE", board);
        drawWhitePawnLine(out, "WHITE", board);
        drawWhiteBackline(out, "WHITE", board);
        drawWhiteBorder(out);
    }

    public static void drawBlackPlayerBoard(ChessGame game){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chess.ChessBoard board = game.getBoard();
        drawBlackBorder(out);
        drawWhiteBackline(out, "BLACK", board);
        drawWhitePawnLine(out, "BLACK", board);
        drawMiddleSquares(out, "BLACK", board);
        drawBlackPawnLine(out, "BLACK", board);
        drawBlackBackline(out, "BLACK", board);
        drawBlackBorder(out);
    }

    private static void printRow(PrintStream out, int row, chess.ChessBoard board, String boardColor){
        for (int i = 8; i > 0; i--){
            ChessPiece piece = board.getPiece(new ChessPosition(row, i));
            if (piece == null) {
                determineSquareColor(out, row, i, boardColor, null);
                out.print("   ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" R ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" N ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" B ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.KING){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" K ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" Q ");}
            else if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                determineSquareColor(out, row, i, boardColor, piece.getTeamColor());
                out.print(" P ");}
        }
    }

    private static void determineSquareColor(PrintStream out, int row, int col, String boardColor, ChessGame.TeamColor pieceColor){
        if (boardColor.equals("BLACK")){
            if (row % 2 == 1){
                if (col % 2 == 1){
                    if (pieceColor == null){
                        setBlackSpaceBlackPiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setBlackSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setBlackSpaceBlackPiece(out);
                    }
                }
                else{
                    if (pieceColor == null){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setWhiteSpaceBlackPiece(out);
                    }
                }
            }
            else if (row % 2 == 0){
                if (col % 2 == 1){
                    if (pieceColor == null){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setWhiteSpaceBlackPiece(out);
                    }
                }
                else{
                    if (pieceColor == null){
                        setBlackSpaceBlackPiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setBlackSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setBlackSpaceBlackPiece(out);
                    }
                }
            }
        }
        else if (boardColor.equals("WHITE")){
            if (row % 2 == 1){
                if (col % 2 == 1){
                    if (pieceColor == null){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setWhiteSpaceBlackPiece(out);
                    }
                }
                else{
                    if (pieceColor == null){
                        setBlackSpaceBlackPiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setBlackSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setBlackSpaceBlackPiece(out);
                    }
                }
            }
            else if (row % 2 == 0){
                if (col % 2 == 1){
                    if (pieceColor == null){
                        setBlackSpaceBlackPiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setBlackSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setBlackSpaceBlackPiece(out);
                    }
                }
                else{
                    if (pieceColor == null){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.WHITE){
                        setWhiteSpaceWhitePiece(out);
                    }
                    else if (pieceColor == ChessGame.TeamColor.BLACK){
                        setWhiteSpaceBlackPiece(out);
                    }
                }
            }
        }
    }

    private static void drawWhiteBorder(PrintStream out){
        setBorderColors(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 0; i <= 9; i++){
            if (i > 0 && i < 9){
                out.print(headers[i - 1]);
                out.print(EMPTY);
            }
            else if (i == 0){
                out.print(EMPTY.repeat(2));
            }
        }
        out.print(EMPTY);
        resetColors(out);
        out.println();
    }

    private static void drawBlackBackline(PrintStream out, String color, chess.ChessBoard board){
        drawGivenRow(out, " 8 ", 8, board, color);
    }

    private static void drawGivenRow(PrintStream out, String s, int row, chess.ChessBoard board, String color) {
        setBorderColors(out);
        out.print(s);
        printRow(out, row, board, color);
        setBorderColors(out);
        out.print(s);
        resetColors(out);
        out.println();
    }

    private static void drawBlackPawnLine(PrintStream out, String color, chess.ChessBoard board){
        drawGivenRow(out, " 7 ", 7, board, color);
    }

    private static void drawMiddleSquares(PrintStream out, String color, chess.ChessBoard board){
        if (color.equals("WHITE")) {
            for (int i = 6; i >= 3; i--) {
                String s = String.format(" %d ", i);
                drawGivenRow(out, s, i, board, color);
            }
        }
        else{
            for (int i = 3; i <= 6; i++) {
                String s = String.format(" %d ", i);
                drawGivenRow(out, s, i, board, color);
            }
        }
    }

    private static void drawWhiteBackline(PrintStream out, String color, chess.ChessBoard board){
        drawGivenRow(out, " 1 ", 1, board, color);
    }

    private static void drawWhitePawnLine(PrintStream out, String color, chess.ChessBoard board){
        drawGivenRow(out, " 2 ", 2, board, color);
    }
    
    private static void drawBlackBorder(PrintStream out){
        setBorderColors(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 9; i >= 0; i--){
            if (i > 0 && i < 9){
                out.print(headers[i - 1]);
                out.print(EMPTY);
            }
            else if (i == 9){
                out.print(EMPTY.repeat(2));
            }
        }
        out.print(EMPTY);
        resetColors(out);
        out.println();
    }

    private static void setBorderColors(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setBlackSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setWhiteSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setWhiteSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void resetColors(PrintStream out){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}
