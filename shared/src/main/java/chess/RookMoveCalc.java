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
        return rookMoves;
    }
}

