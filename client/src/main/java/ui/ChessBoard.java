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
                out.print(" ");
            }
        }
        out.println();
    }

    private static void drawBlackBackline(PrintStream out){
        String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        for (int i = 0; i <= 9; i++){
            if (i > 0 && i < 9){
                if (i % 2 != 0){
                    setWhiteSpaceBlackPiece(out);
                }
                else{
                    setBlackSpaceBlackPiece(out);
                }
                out.print(pieces[i - 1]);
            }
            else if (i == 0){
                setBorderColors(out);
                out.print(" 8");
                out.print(EMPTY);
            }
            else{
                setBorderColors(out);
                out.print(EMPTY);
                out.print("8");
            }
        }
        out.println();
    }

    private static void drawBlackPawnLine(PrintStream out){
        for (int i = 0; i <= 9; i++){
            if (i > 0 && i < 9){
                if (i % 2 != 0){
                    setBlackSpaceBlackPiece(out);
                }
                else{
                    setWhiteSpaceBlackPiece(out);
                }
                out.print(" P ");
            }
            else if (i == 0){
                setBorderColors(out);
                out.print(" 7");
                out.print(EMPTY);
            }
            else{
                setBorderColors(out);
                out.print(EMPTY);
                out.print("7");
            }
        }
        out.println();
    }

    private static void drawWhiteBackline(PrintStream out){}

    private static void drawWhitePawnLine(PrintStream out){}

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
}
