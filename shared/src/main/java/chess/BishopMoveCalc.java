package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalc extends PieceMovesCalculator{
    ArrayList<ChessMove> bishopMoves = new ArrayList<ChessMove>() {};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece bishop = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while(row > 1 && col > 1){
            row += -1;
            col += -1;
            ChessMove move = possibleMove(board, myPosition, bishop, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, bishop, row, col)){
                    bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));
                    break;
                }
                bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(row < 8 && col < 8){
            row += 1;
            col += 1;
            ChessMove move = possibleMove(board, myPosition, bishop, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, bishop, row, col)){
                    bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));
                    break;
                }
                bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(col > 1 && row < 8){
            col += -1;
            row += 1;
            ChessMove move = possibleMove(board, myPosition, bishop, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, bishop, row, col)){
                    bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));
                    break;
                }
                bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(col < 8 && row > 1){
            col += 1;
            row += -1;
            ChessMove move = possibleMove(board, myPosition, bishop, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, bishop, row, col)){
                    bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));
                    break;
                }
                bishopMoves.add(possibleMove(board, myPosition, bishop, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
                /*
        for (int row = myPosition.getRow()-1; row <= myPosition.getRow()+1; row++){
            if (row >= 1 && row <= 8) {
                for (int col = myPosition.getColumn() - 1; col <= myPosition.getColumn() + 1; col++) {
                    if (col >= 1 && col <= 8){
                        ChessPiece maybeSpot = board.getPiece(new ChessPosition(row, col));
                        if (maybeSpot == null){
                            rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
                        } else if (maybeSpot.getTeamColor() != bishop.getTeamColor()) {
                            rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(),myPosition.getColumn()), new ChessPosition(row, col), null));
                        }
                    }
                }
            }
        }

         */
        return bishopMoves;
    }
}
