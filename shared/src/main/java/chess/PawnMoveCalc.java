package chess;

import java.util.ArrayList;

public class PawnMoveCalc extends PieceMovesCalculator{
    private final ArrayList<ChessMove> pawnMoves = new ArrayList<>();

    private void promotionMoves(ChessPosition myPosition, int row, int col){
        ChessPosition startPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessPosition endPosition = new ChessPosition(row, col);
        pawnMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        pawnMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        pawnMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        pawnMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
    }
    public ArrayList<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pawn = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE){
            if (row <= 7) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    if (row == 7){
                        promotionMoves(myPosition, row + 1, col);
                    }
                    else {
                        checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col);
                    }
                    if (row == 2 && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                        checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 2, col);
                    }
                }
                promotionWhite(board, myPosition, col, row, pawn);
            }
        }
        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK){
            if (row >= 2) {
                if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    if (row == 2){
                        promotionMoves(myPosition, row - 1, col);
                    }
                    else {
                        checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col);
                    }
                    if (row == 7 && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                        checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 2, col);
                    }
                }
                promotionBlack(board, myPosition, col, row, pawn);
            }
        }
        return pawnMoves;
    }

    private void promotionBlack(ChessBoard board, ChessPosition myPosition, int col, int row, ChessPiece pawn) {
        if (col > 1) {
            if (board.getPiece(new ChessPosition(row - 1, col - 1)) != null) {
                if (row == 2){
                    promotionMoves(myPosition, row - 1, col - 1);
                }
                else{
                    checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col - 1);
                }
            }
        }
        if (col < 8) {
            if (board.getPiece(new ChessPosition(row - 1, col + 1)) != null) {
                if (row == 2){
                    promotionMoves(myPosition, row - 1, col + 1);
                }
                else{
                    checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row - 1, col + 1);
                }                    }
        }
    }

    private void promotionWhite(ChessBoard board, ChessPosition myPosition, int col, int row, ChessPiece pawn) {
        if (col > 1) {
            if (board.getPiece(new ChessPosition(row + 1, col - 1)) != null) {
                if (row == 7){
                    promotionMoves(myPosition, row + 1, col - 1);
                }
                else{
                    checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col - 1);
                }
            }
        }
        if (col < 8) {
            if (board.getPiece(new ChessPosition(row + 1, col + 1)) != null) {
                if (row == 7){
                    promotionMoves(myPosition, row + 1, col + 1);
                }
                else{
                    checkAndAddNewSpace(board, pawnMoves, pawn, myPosition, row + 1, col + 1);
                }                    }
        }
    }
}
