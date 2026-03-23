package ui;



import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

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

    }

    private static void drawWhiteBorder(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 0; i <= 9; i++){
            if (i > 0 && i < 9){
                //out.print(EMPTY);
                out.print(headers[i - 1]);
                out.print(EMPTY);
            }
            else if (i == 0){
                out.print(EMPTY.repeat(1));
            }
        }
        out.println();
    }

    private static void drawWhiteBackline(PrintStream out){}

    private static void drawWhitePawnLine(PrintStream out){}

    private static void drawBlackBorder(PrintStream out){}

    private static void drawBlackBackline(PrintStream out){}

    private static void drawBlackPawnLine(PrintStream out){}
}
