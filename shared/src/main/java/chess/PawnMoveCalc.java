package chess;

import java.util.ArrayList;

public class PawnMoveCalc extends PieceMovesCalculator{
    private ArrayList<ChessMove> pawnMoves = new ArrayList<>();

    private void promote(ChessPosition myPosition, int row, int col){
        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.ROOK));
        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.BISHOP));
        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT));
        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), ChessPiece.PieceType.QUEEN));
    }
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pawn = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            if (row <= 7) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    if (row == 7){
                        promote(myPosition, row + 1, col);
                    }
                    else {
                        CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col);
                    }
                    if (row == 2 && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                        CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 2, col);
                    }
                }
                if (col > 1) {
                    if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                        if (row == 7){
                            promote(myPosition, row + 1, col - 1);
                        }
                        else{
                            CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col - 1);
                        }
                    }
                }
                if (col < 8) {
                    if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                        if (row == 7){
                            promote(myPosition, row + 1, col + 1);
                        }
                        else{
                            CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col + 1);
                        }                    }
                }
            }
        }
        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK){
            if (row >= 2) {
                if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    if (row == 2){
                        promote(myPosition, row - 1, col);
                    }
                    else {
                        CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col);
                    }
                    if (row == 7 && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                        CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 2, col);
                    }
                }
                if (col > 1) {
                    if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                        if (row == 2){
                            promote(myPosition, row - 1, col - 1);
                        }
                        else{
                            CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col - 1);
                        }
                    }
                }
                if (col < 8) {
                    if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                        if (row == 7){
                            promote(myPosition, row - 1, col + 1);
                        }
                        else{
                            CheckAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col + 1);
                        }                    }
                }
            }
        }
        return pawnMoves;
    }
}
