package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    /*
    If there is repeated logic between pieces I should include it here.
     */
    public ChessMove possibleMove(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col){
        ChessPiece maybeSpot = board.getPiece(new ChessPosition(row, col));
        if (maybeSpot == null){
            return (new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
        } else if (maybeSpot.getTeamColor() != piece.getTeamColor()) {
            return (new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
        }
        return null;
    }

    public boolean spotOccupiedByEnemy(ChessBoard board, ChessPiece piece, int row, int col){
        ChessPiece maybeSpot = board.getPiece(new ChessPosition(row, col));
        if (maybeSpot == null){
            return false;
        }
        else if (maybeSpot.getTeamColor() != piece.getTeamColor()){
            return true;
        }
        return false;
    }
    /* I tried to turn the repeated code into one function, but I couldn't figure it out.
    I figured it would be faster to code if I had less elegant code
    public Collection<ChessMove> moveInALine(ChessBoard board, ChessPosition myPosition, ChessPiece piece, int row, int col, ArrayList<ChessMove> pieceMoves, int iterator, int direction){
        if(iterator == row && direction > 0) {
            while (iterator > 1) {
                iterator += direction;
                ChessMove move = possibleMove(board, myPosition, piece, iterator, col);
                if (move != null) {
                    if (spotOccupiedByEnemy(board, piece, row, col)) {
                        pieceMoves.add(possibleMove(board, myPosition, piece, iterator, col));
                        break;
                    }
                    pieceMoves.add(possibleMove(board, myPosition, piece, iterator, col));
                } else {
                    break;
                }
            }
            return pieceMoves;
        }

        return null;
    }

     */
}
