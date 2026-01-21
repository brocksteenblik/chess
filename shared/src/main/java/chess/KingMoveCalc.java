package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMoveCalc extends PieceMovesCalculator{
    ArrayList<ChessMove> kingMoves = new ArrayList<ChessMove>() {};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece king = board.getPiece(myPosition);
        for (int i = myPosition.getRow()-1; i <= myPosition.getRow()+1; i++){
            if (i >= 1 && i <= 8) {
                for (int j = myPosition.getColumn() - 1; j <= myPosition.getColumn() + 1; j++) {
                    if (j >= 1 && j <= 8){
                        ChessPiece maybeSpot = board.getPiece(new ChessPosition(i, j));
                        if (maybeSpot == null){
                            kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(i, j), null));
                        } else if (maybeSpot.getTeamColor() != king.getTeamColor()) {
                            kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(i, j), null));
                        }
                    }
                }
            }
        }
        return kingMoves;
    }
}
