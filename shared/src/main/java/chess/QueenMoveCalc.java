package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalc extends PieceMovesCalculator {
    ArrayList<ChessMove> queenMoves = new ArrayList<ChessMove>() {};

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece queen = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (row > 1) {
            row += -1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null) {
                if (spotOccupiedByEnemy(board, queen, row, col)) {
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));
            } else {
                break;
            }
        }

        /*
        rookMoves = (ArrayList<ChessMove>) moveInALine(board, myPosition, queen, row, col, rookMoves, row, -1);
         */
        row = myPosition.getRow();
        while (row < 8) {
            row += 1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null) {
                if (spotOccupiedByEnemy(board, queen, row, col)) {
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));
            } else {
                break;
            }
        }
        row = myPosition.getRow();
        while (col > 1) {
            col += -1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null) {
                if (spotOccupiedByEnemy(board, queen, row, col)) {
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));
            } else {
                break;
            }
        }
        col = myPosition.getColumn();
        while (col < 8) {
            col += 1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null) {
                if (spotOccupiedByEnemy(board, queen, row, col)) {
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));
            } else {
                break;
            }
        }
        col = myPosition.getColumn();
        while(row > 1 && col > 1){
            row += -1;
            col += -1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, queen, row, col)){
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(row < 8 && col < 8){
            row += 1;
            col += 1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, queen, row, col)){
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(col > 1 && row < 8){
            col += -1;
            row += 1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, queen, row, col)){
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while(col < 8 && row > 1){
            col += 1;
            row += -1;
            ChessMove move = possibleMove(board, myPosition, queen, row, col);
            if (move != null){
                if(spotOccupiedByEnemy(board, queen, row, col)){
                    queenMoves.add(possibleMove(board, myPosition, queen, row, col));
                    break;
                }
                queenMoves.add(possibleMove(board, myPosition, queen, row, col));}
            else {break;}
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        return queenMoves;
    }
}
