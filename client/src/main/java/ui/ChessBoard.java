package ui;



import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessBoard.*;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final String EMPTY = "  ";

    public static void main(){
        // Add stuff with player color checking later
        /*
        Order of operations:
        - Draw white or black border
        - Draw black or white backline
        - Draw corresponding line of pawns
        - Draw middle lines
        - Draw pawn line and backline for other color
        - Draw another white or black border
         */
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawWhiteBorder(out);
        drawBlackBackline(out);
        drawBlackPawnLine(out);
        drawMiddleSquares(out);
        drawWhitePawnLine(out);
        drawWhiteBackline(out);
        drawWhiteBorder(out);
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
                out.print(EMPTY.repeat(3));
            }
        }
        out.println();
    }

    private static void drawBlackBackline(PrintStream out){
        // add backwards parser for board flip
        String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        setBorderColors(out);
        out.print(" 8 ");
        out.print(EMPTY);
        for (int i = 0; i <= 7; i++){
            if (i % 2 != 0){
                setWhiteSpaceBlackPiece(out);
            }
            else{
                setBlackSpaceBlackPiece(out);
            }
            out.print(pieces[i]);
            }
        setBorderColors(out);
        out.print(EMPTY);
        out.print(" 8 ");

        out.println();
    }

    private static void drawBlackPawnLine(PrintStream out){
        // Add backwards parser for board flip
        setBorderColors(out);
        out.print(" 7 ");
        out.print(EMPTY);
        for (int i = 0; i <= 7; i++){
            if (i % 2 != 0){
                setBlackSpaceBlackPiece(out);
            }
            else{
                setWhiteSpaceBlackPiece(out);
            }
            out.print(" P ");
        }
        setBorderColors(out);
        out.print(EMPTY);
        out.print(" 7 ");
        out.println();
    }

    private static void drawMiddleSquares(PrintStream out){
        // From the perspective of white player
        for (int i = 6; i >= 3; i--){
            drawSingleBlankLine(i, out);
        }
    }

    private static void drawSingleBlankLine(int row, PrintStream out){
        setBorderColors(out);
        out.print(" " + row + " ");
        out.print(EMPTY);
        for (int i = 0; i <= 7; i++){
            determineRowPattern(out, i, row);
            out.print("   ");
        }
        setBorderColors(out);
        out.print(EMPTY);
        out.print(" " + row + " ");
        out.println();
    }

    private static void determineRowPattern(PrintStream out, int i, int row) {
        if (row % 2 == 0){
            if (i % 2 != 0){
                setWhiteSpaceBlackPiece(out);
            }
            else{
                setBlackSpaceBlackPiece(out);
            }
        }
        else {
            if (i % 2 != 0){
                setBlackSpaceBlackPiece(out);
            }
            else{
                setWhiteSpaceBlackPiece(out);
            }
        }
    }

    private static void drawWhiteBackline(PrintStream out){
        // add backwards parser for board flip
        String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        setBorderColors(out);
        out.print(" 1 ");
        out.print(EMPTY);
        for (int i = 0; i <= 7; i++){
            if (i % 2 != 0){
                setBlackSpaceWhitePiece(out);
            }
            else{
                setWhiteSpaceWhitePiece(out);
            }
            out.print(pieces[i]);
        }
        setBorderColors(out);
        out.print(EMPTY);
        out.print(" 1 ");

        out.println();
    }

    private static void drawWhitePawnLine(PrintStream out){
        // Add backwards parser for board flip
        setBorderColors(out);
        out.print(" 2 ");
        out.print(EMPTY);
        for (int i = 0; i <= 7; i++){
            if (i % 2 != 0){
                setWhiteSpaceWhitePiece(out);
            }
            else{
                setBlackSpaceWhitePiece(out);
            }
            out.print(" P ");
        }
        setBorderColors(out);
        out.print(EMPTY);
        out.print(" 2 ");
        out.println();
    }

    private static void drawBlackBorder(PrintStream out){}

    private static void setBorderColors(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setWhiteSpaceBlackPiece(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setBlackSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setWhiteSpaceWhitePiece(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_RED);
    }
}
