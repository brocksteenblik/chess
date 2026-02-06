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
        return bishopMoves;
    }
}
