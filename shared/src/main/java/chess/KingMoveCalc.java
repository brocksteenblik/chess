package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalc extends PieceMovesCalculator{
    ArrayList<ChessMove> kingMoves = new ArrayList<ChessMove>() {};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece king = board.getPiece(myPosition);
        for (int i = myPosition.getRow()-1; i <= myPosition.getRow()+1; i++){
            if (i >= 1 && i <= 8) {
                for (int j = myPosition.getColumn() - 1; j <= myPosition.getColumn() + 1; j++) {
                    if (j >= 1 && j <= 8){
                        ChessMove move = possibleMove(board, myPosition, king, i, j);
                        if (move != null){
                            kingMoves.add(possibleMove(board, myPosition, king, i, j));}
                    }
                }
            }
        }
        return kingMoves;
    }
}
