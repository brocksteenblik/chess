package ui;



import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final String EMPTY = "  ";

    public static void drawWhitePlayerBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawWhiteBorder(out);
        drawBlackBackline(out, "WHITE");
        drawBlackPawnLine(out, "WHITE");
        drawMiddleSquares(out, "WHITE");
        drawWhitePawnLine(out, "WHITE");
        drawWhiteBackline(out, "WHITE");
        drawWhiteBorder(out);
    }

    public static void drawBlackPlayerBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBlackBorder(out);
        drawWhiteBackline(out, "BLACK");
        drawWhitePawnLine(out, "BLACK");
        drawMiddleSquares(out, "BLACK");
        drawBlackPawnLine(out, "BLACK");
        drawBlackBackline(out, "BLACK");
        drawBlackBorder(out);
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

    private static void drawBlackBackline(PrintStream out, String color){
        // add backwards parser for board flip
        String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        setBorderColors(out);
        out.print(" 8 ");
        drawBlBLWithPlayerPOV(out, pieces, color);
        setBorderColors(out);
        out.print(" 8 ");
        resetColors(out);
        out.println();
    }

    private static void drawBlBLWithPlayerPOV(PrintStream out, String[] pieces, String color) {
        if (color.equals("BLACK")) {
            for (int i = 0; i <= 7; i++) {
                if (i % 2 != 0) {
                    setWhiteSpaceBlackPiece(out);
                } else {
                    setBlackSpaceBlackPiece(out);
                }
                out.print(pieces[i]);
            }
        }
        else{
            for (int i = 7; i >= 0; i--) {
                if (i % 2 != 0) {
                    setWhiteSpaceBlackPiece(out);
                } else {
                    setBlackSpaceBlackPiece(out);
                }
                out.print(pieces[i]);
            }
        }
    }

    private static void drawBlackPawnLine(PrintStream out, String color){
        // Add backwards parser for board flip
        setBorderColors(out);
        out.print(" 7 ");
        drawBlPLWithPlayerPOV(out, color);
        setBorderColors(out);
        out.print(" 7 ");
        resetColors(out);
        out.println();
    }

    private static void drawBlPLWithPlayerPOV(PrintStream out, String color) {
        if (color.equals("BLACK")) {
            for (int i = 0; i <= 7; i++) {
                if (i % 2 != 0) {
                    setBlackSpaceBlackPiece(out);
                } else {
                    setWhiteSpaceBlackPiece(out);
                }
                out.print(" P ");
            }
        }
        else{
            for (int i = 7; i >= 0; i--) {
                if (i % 2 != 0) {
                    setBlackSpaceBlackPiece(out);
                } else {
                    setWhiteSpaceBlackPiece(out);
                }
                out.print(" P ");
            }
        }
    }

    private static void drawMiddleSquares(PrintStream out, String color){
        // From the perspective of white player
        if (color.equals("WHITE")) {
            for (int i = 6; i >= 3; i--) {
                drawSingleBlankLine(i, out, color);
            }
        }
        else{
            for (int i = 3; i <= 6; i++) {
                drawSingleBlankLine(i, out, color);
            }
        }
    }

    private static void drawSingleBlankLine(int row, PrintStream out, String color){
        setBorderColors(out);
        out.print(" " + row + " ");
        for (int i = 0; i <= 7; i++){
            determineRowPattern(out, i, row, color);
            out.print("   ");
        }
        setBorderColors(out);
        out.print(" " + row + " ");
        resetColors(out);
        out.println();
    }

    private static void determineRowPattern(PrintStream out, int i, int row, String color) {
        if (color.equals("BLACK")) {
            if (row % 2 == 0) {
                if (i % 2 != 0) {
                    setWhiteSpaceBlackPiece(out);
                } else {
                    setBlackSpaceBlackPiece(out);
                }
            } else {
                if (i % 2 != 0) {
                    setBlackSpaceBlackPiece(out);
                } else {
                    setWhiteSpaceBlackPiece(out);
                }
            }
        }
        else {
            if (row % 2 == 1) {
                if (i % 2 != 0) {
                    setWhiteSpaceBlackPiece(out);
                } else {
                    setBlackSpaceBlackPiece(out);
                }
            } else {
                if (i % 2 != 0) {
                    setBlackSpaceBlackPiece(out);
                } else {
                    setWhiteSpaceBlackPiece(out);
                }
            }
        }
    }

    private static void drawWhiteBackline(PrintStream out, String color){
        // add backwards parser for board flip
        String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        setBorderColors(out);
        out.print(" 1 ");
        drawWhBLWithPlayerPOV(out, pieces, color);
        setBorderColors(out);
        out.print(" 1 ");
        resetColors(out);
        out.println();
    }

    private static void drawWhBLWithPlayerPOV(PrintStream out, String[] pieces, String color) {
        if (color.equals("BLACK")) {
            for (int i = 0; i <= 7; i++) {
                if (i % 2 != 0) {
                    setBlackSpaceWhitePiece(out);
                } else {
                    setWhiteSpaceWhitePiece(out);
                }
                out.print(pieces[i]);
            }
        }
        else {
            for (int i = 7; i >= 0; i--) {
                if (i % 2 != 0) {
                    setBlackSpaceWhitePiece(out);
                } else {
                    setWhiteSpaceWhitePiece(out);
                }
                out.print(pieces[i]);
            }
        }
    }

    private static void drawWhitePawnLine(PrintStream out, String color){
        // Add backwards parser for board flip
        setBorderColors(out);
        out.print(" 2 ");
        drawWhPLWithPlayerPOV(out, color);
        setBorderColors(out);
        out.print(" 2 ");
        resetColors(out);
        out.println();
    }

    private static void drawWhPLWithPlayerPOV(PrintStream out, String color) {
        if (color.equals("BLACK")) {
            for (int i = 0; i <= 7; i++) {
                if (i % 2 != 0) {
                    setWhiteSpaceWhitePiece(out);
                } else {
                    setBlackSpaceWhitePiece(out);
                }
                out.print(" P ");
            }
        }
        else{
            for (int i = 7; i >= 0; i--) {
                if (i % 2 != 0) {
                    setWhiteSpaceWhitePiece(out);
                } else {
                    setBlackSpaceWhitePiece(out);
                }
                out.print(" P ");
            }
        }
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
