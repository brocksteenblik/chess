package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalc extends  PieceMovesCalculator{
    ArrayList<ChessMove> knightMoves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece knight = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row + 2 <= 8){
            if (col - 1 >= 1){
                ChessMove move =  possibleMove(board, myPosition, knight, row + 2, col - 1);
                if (move != null){knightMoves.add(move);}
            }
            if (col + 1 <= 8){
                ChessMove move =  possibleMove(board, myPosition, knight, row + 2, col + 1);
                if (move != null){knightMoves.add(move);}
            }
        }
        if (row - 2 >= 1){
            if (col - 1 >= 1){
                ChessMove move =  possibleMove(board, myPosition, knight, row - 2, col - 1);
                if (move != null){knightMoves.add(move);}
            }
            if (col + 1 <= 8){
                ChessMove move =  possibleMove(board, myPosition, knight, row - 2, col + 1);
                if (move != null){knightMoves.add(move);}
            }
        }
        if (col + 2 <= 8){
            if (row - 1 >= 1){
                ChessMove move =  possibleMove(board, myPosition, knight, row - 1, col + 2);
                if (move != null){knightMoves.add(move);}
            }
            if (row + 1 <= 8){
                ChessMove move =  possibleMove(board, myPosition, knight, row + 1, col + 2);
                if (move != null){knightMoves.add(move);}
            }
        }
        if (col - 2 >= 1){
            if (row - 1 >= 1){
                ChessMove move =  possibleMove(board, myPosition, knight, row - 1, col - 2);
                if (move != null){knightMoves.add(move);}
            }
            if (row + 1 <= 8){
                ChessMove move =  possibleMove(board, myPosition, knight, row + 1, col - 2);
                if (move != null){knightMoves.add(move);}
            }
        }

        return knightMoves;
    }

    /*
    private Collection<ChessMove> knightMoveOneDirection (ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int moveByTwo, int moveByOne){

    }

     */
}
