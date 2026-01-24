package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalc extends PieceMovesCalculator{
    ArrayList<ChessMove> pawnMoves = new ArrayList<>();

    private void promotePawn(ArrayList<ChessMove> newPawnMoves, ChessPosition myPosition, int row, int col){
        List<ChessPiece.PieceType> promotionPieces = List.of(ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP);
        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            newPawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), promotionPiece));
        }
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece pawn = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            if (myPosition.getRow() < 8){
                ChessMove move = possibleMove(board, myPosition, pawn, row + 1, col);
                if (board.getPiece(new ChessPosition(row + 1, col)) == null){
                    if (row == 7){
                        promotePawn(pawnMoves, myPosition, row + 1, col);
                    } else {pawnMoves.add(move);}
                    if (row == 2){
                        ChessMove move_two_spaces = possibleMove(board, myPosition, pawn, row + 2, col);
                        if (board.getPiece(new ChessPosition(row + 2, col)) == null){
                            pawnMoves.add(move_two_spaces);
                        }
                    }
                }
                if (spotOccupiedByEnemy(board, pawn, row + 1, col - 1)) {
                    if (row == 7) {
                        promotePawn(pawnMoves, myPosition, row + 1, col - 1);
                    } else {
                        pawnMoves.add(possibleMove(board, myPosition, pawn, row + 1, col - 1));
                    }
                }
                if (spotOccupiedByEnemy(board, pawn, row + 1, col + 1)){
                    if (row == 7) {
                        promotePawn(pawnMoves, myPosition, row + 1, col - 1);
                    } else {
                        pawnMoves.add(possibleMove(board, myPosition, pawn, row + 1, col + 1));
                    }                }
            }
        }
        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK){
            if (myPosition.getRow() > 1){
                ChessMove move = possibleMove(board, myPosition, pawn, row - 1, col);
                if (board.getPiece(new ChessPosition(row - 1, col)) == null){
                    if (row == 2){
                        promotePawn(pawnMoves, myPosition, row - 1, col);
                    } else {pawnMoves.add(move);}
                    if (row == 7){
                        ChessMove move_two_spaces = possibleMove(board, myPosition, pawn, row - 2, col);
                        if (board.getPiece(new ChessPosition(row - 2, col)) == null){
                            pawnMoves.add(move_two_spaces);
                        }
                    }
                }
                if (spotOccupiedByEnemy(board, pawn, row - 1, col - 1)) {
                    if (row == 2) {
                        promotePawn(pawnMoves, myPosition, row - 1, col - 1);
                    } else {
                        pawnMoves.add(possibleMove(board, myPosition, pawn, row - 1, col - 1));
                    }
                }
                if (spotOccupiedByEnemy(board, pawn, row - 1, col + 1)) {
                    if (row == 2) {
                        promotePawn(pawnMoves, myPosition, row - 1, col + 1);
                    } else {
                        pawnMoves.add(possibleMove(board, myPosition, pawn, row - 1, col + 1));
                    }
                }
            }
        }
        return pawnMoves;
    }
}
