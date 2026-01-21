package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalc extends PieceMovesCalculator{
    ArrayList<ChessMove> rookMoves = new ArrayList<ChessMove>() {};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece rook = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while(row > 1){
            row += -1;
            ChessMove move = possibleMove(board, myPosition, rook, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, rook, row, col)){
                    rookMoves.add(possibleMove(board, myPosition, rook, row, col));
                    break;
                }
                rookMoves.add(possibleMove(board, myPosition, rook, row, col));}
            else {break;}
        }

        /*
        rookMoves = (ArrayList<ChessMove>) moveInALine(board, myPosition, rook, row, col, rookMoves, row, -1);
         */
        row = myPosition.getRow();
        while(row < 8){
            row += 1;
            ChessMove move = possibleMove(board, myPosition, rook, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, rook, row, col)){
                    rookMoves.add(possibleMove(board, myPosition, rook, row, col));
                    break;
                }
                rookMoves.add(possibleMove(board, myPosition, rook, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        while(col > 1){
            col += -1;
            ChessMove move = possibleMove(board, myPosition, rook, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, rook, row, col)){
                    rookMoves.add(possibleMove(board, myPosition, rook, row, col));
                    break;
                }
                rookMoves.add(possibleMove(board, myPosition, rook, row, col));}
            else {break;}
        }
        col = myPosition.getColumn();
        while(col < 8){
            col += 1;
            ChessMove move = possibleMove(board, myPosition, rook, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, rook, row, col)){
                    rookMoves.add(possibleMove(board, myPosition, rook, row, col));
                    break;
                }
                rookMoves.add(possibleMove(board, myPosition, rook, row, col));}
            else {break;}
        }
        col = myPosition.getColumn();
        /*
        for (int row = myPosition.getRow()-1; row <= myPosition.getRow()+1; row++){
            if (row >= 1 && row <= 8) {
                for (int col = myPosition.getColumn() - 1; col <= myPosition.getColumn() + 1; col++) {
                    if (col >= 1 && col <= 8){
                        ChessPiece maybeSpot = board.getPiece(new ChessPosition(row, col));
                        if (maybeSpot == null){
                            rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
                        } else if (maybeSpot.getTeamColor() != rook.getTeamColor()) {
                            rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
        }

         */
        return rookMoves;
    }
}

