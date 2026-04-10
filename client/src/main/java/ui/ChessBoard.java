package ui;



import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final String EMPTY = "  ";


    public static void drawWhitePlayerBoard(ChessGame game, ChessPosition chessPosition) {
        ArrayList<ChessMove> validMoves = null;
        if (chessPosition != null) {
            if (game.getBoard().getPiece(chessPosition) == null){
                System.out.print("Provided position does not have a Piece");
                return;
            }
            validMoves = (ArrayList<ChessMove>) game.validMoves(chessPosition);
            if (validMoves.isEmpty()){
                System.out.print("Selected Piece cannot move.");
                return;
            }
        }
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chess.ChessBoard board = game.getBoard();
        drawWhiteBorder(out);
        drawBlackBackline(out, "WHITE", board, validMoves);
        drawBlackPawnLine(out, "WHITE", board, validMoves);
        drawMiddleSquares(out, "WHITE", board, validMoves);
        drawWhitePawnLine(out, "WHITE", board, validMoves);
        drawWhiteBackline(out, "WHITE", board, validMoves);
        drawWhiteBorder(out);
    }

    public static void drawBlackPlayerBoard(ChessGame game, ChessPosition chessPosition) {
        ArrayList<ChessMove> validMoves = null;
        if (chessPosition != null){
            validMoves = (ArrayList<ChessMove>) game.validMoves(chessPosition);
            if (validMoves.isEmpty()){
                System.out.print("Selected Piece cannot move.");
                return;
            }
        }
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        chess.ChessBoard board = game.getBoard();
        drawBlackBorder(out);
        drawWhiteBackline(out, "BLACK", board, validMoves);
        drawWhitePawnLine(out, "BLACK", board, validMoves);
        drawMiddleSquares(out, "BLACK", board, validMoves);
        drawBlackPawnLine(out, "BLACK", board, validMoves);
        drawBlackBackline(out, "BLACK", board, validMoves);
        drawBlackBorder(out);
    }

    private static void printRow(PrintStream out, int row, chess.ChessBoard board, String boardColor, ArrayList<ChessMove> validMoves){
        if (boardColor.equals("BLACK")){
            for (int i = 8; i > 0; i--){
                drawSquare(out, row, board, boardColor, validMoves, i);
            }
        }
        else if (boardColor.equals("WHITE")){
            for (int i = 1; i < 9; i++){
                drawSquare(out, row, board, boardColor, validMoves, i);
            }
        }

    }

    private static void drawSquare(PrintStream out, int row, chess.ChessBoard board, String boardColor, ArrayList<ChessMove> validMoves, int i) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, i));
        if (piece == null) {
            determineSquareColor(out, row, i, boardColor, null, validMoves);
            out.print("   ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" R ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" N ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" B ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.KING){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" K ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" Q ");}
        else if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            determineSquareColor(out, row, i, boardColor, piece.getTeamColor(), validMoves);
            out.print(" P ");}
    }

    private static void determineSquareColor(PrintStream out, int row, int col,
                                             String boardColor, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves){
        if (row % 2 == 1){
            blackThenWhiteSpace(out, row, col, pieceColor, validMoves);
        }
        else if (row % 2 == 0){
            whiteThenBlackSpace(out, row, col, pieceColor, validMoves);
        }
    }

    private static void blackThenWhiteSpace(PrintStream out, int row, int col, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves) {
        if (col % 2 == 1){
            if (checkHighlight(row, col, validMoves)){
                highlightBlackSpace(out, pieceColor, validMoves);
            } else {
                assignBlackSpace(out, pieceColor, validMoves);
            }
        }
        else{
            if (checkHighlight(row, col, validMoves)){
                highlightWhiteSpace(out, pieceColor, validMoves);
            } else {
                assignWhiteSpace(out, pieceColor, validMoves);
            }
        }
    }

    private static void whiteThenBlackSpace(PrintStream out, int row, int col, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves) {
        if (col % 2 == 1){
            if (checkHighlight(row, col, validMoves)){
                highlightWhiteSpace(out, pieceColor, validMoves);
            } else {
                assignWhiteSpace(out, pieceColor, validMoves);
            }
        }
        else{
            if (checkHighlight(row, col, validMoves)){
                highlightBlackSpace(out, pieceColor, validMoves);
            } else {
                assignBlackSpace(out, pieceColor, validMoves);
            }
        }
    }

    private static boolean checkHighlight(int row, int col, ArrayList<ChessMove> validMoves) {
        if (validMoves == null){
            return false;
        }
        ChessPosition start = validMoves.getFirst().getStartPosition();
        ChessMove move = new ChessMove(start, new ChessPosition(row, col), null);
        if (move.getEndPosition().equals(start)){
            return true;
        }
        for (ChessMove m : validMoves){
            if (m.equals(move)){
                return true;
            }
        }
        for (ChessMove m : validMoves){
            if (m.equals(new ChessMove(start, new ChessPosition(row, col), ChessPiece.PieceType.ROOK))){
                return true;
            }
        }
        return false;
    }

    private static void assignWhiteSpace(PrintStream out, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves) {
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

    private static void highlightWhiteSpace(PrintStream out, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves){
        if (pieceColor == null){
            highlightWhiteSpaceWhitePiece(out);
        }
        else if (pieceColor == ChessGame.TeamColor.WHITE){
            highlightWhiteSpaceWhitePiece(out);
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK){
            highlightWhiteSpaceBlackPiece(out);
        }
    }

    private static void assignBlackSpace(PrintStream out, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves) {
        if (pieceColor == null) {
            setBlackSpaceBlackPiece(out);
        } else if (pieceColor == ChessGame.TeamColor.WHITE) {
            setBlackSpaceWhitePiece(out);
        } else if (pieceColor == ChessGame.TeamColor.BLACK) {
            setBlackSpaceBlackPiece(out);
        }
    }

    private static void highlightBlackSpace(PrintStream out, ChessGame.TeamColor pieceColor, ArrayList<ChessMove> validMoves){
        if (pieceColor == null){
            highlightBlackSpaceWhitePiece(out);
        }
        else if (pieceColor == ChessGame.TeamColor.WHITE){
            highlightBlackSpaceWhitePiece(out);
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK){
            highlightBlackSpaceBlackPiece(out);
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

    private static void drawBlackBackline(PrintStream out, String color, chess.ChessBoard board, ArrayList<ChessMove> validMoves){
        drawGivenRow(out, " 8 ", 8, board, color, validMoves);
    }

    private static void drawGivenRow(PrintStream out, String s, int row, chess.ChessBoard board, String color, ArrayList<ChessMove> validMoves) {
        setBorderColors(out);
        out.print(s);
        printRow(out, row, board, color, validMoves);
        setBorderColors(out);
        out.print(s);
        resetColors(out);
        out.println();
    }

    private static void drawBlackPawnLine(PrintStream out, String color, chess.ChessBoard board, ArrayList<ChessMove> validMoves){
        drawGivenRow(out, " 7 ", 7, board, color, validMoves);
    }

    private static void drawMiddleSquares(PrintStream out, String color, chess.ChessBoard board, ArrayList<ChessMove> validMoves){
        if (color.equals("WHITE")) {
            for (int i = 6; i >= 3; i--) {
                String s = String.format(" %d ", i);
                drawGivenRow(out, s, i, board, color, validMoves);
            }
        }
        else{
            for (int i = 3; i <= 6; i++) {
                String s = String.format(" %d ", i);
                drawGivenRow(out, s, i, board, color, validMoves);
            }
        }
    }

    private static void drawWhiteBackline(PrintStream out, String color, chess.ChessBoard board, ArrayList<ChessMove> validMoves){
        drawGivenRow(out, " 1 ", 1, board, color, validMoves);
    }

    private static void drawWhitePawnLine(PrintStream out, String color, chess.ChessBoard board, ArrayList<ChessMove> validMoves){
        drawGivenRow(out, " 2 ", 2, board, color, validMoves);
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

    private static void highlightBlackSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void highlightWhiteSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void highlightBlackSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void highlightWhiteSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void resetColors(PrintStream out){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}
