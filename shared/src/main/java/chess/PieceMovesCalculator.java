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
        if(row > 8 | col > 8 | row < 1 | col < 1){
            return false;
        }
        ChessPiece maybeSpot = board.getPiece(new ChessPosition(row, col));
        if (maybeSpot == null){
            return false;
        }
        else if (maybeSpot.getTeamColor() != piece.getTeamColor()){
            return true;
        }
        return false;
    }
    public boolean CheckSpotOpen(ChessBoard board, int row, int col){
        if (board.getPiece(new ChessPosition(row, col)) == null){
            return true;
        }
        return false;
    }

    public boolean CheckSpotOccupiedByOtherColor(ChessBoard board, ChessPiece originalPiece, int row, int col){
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (originalPiece.getTeamColor() != piece.getTeamColor()){
            return true;
        }
        return false;
    }

    public ArrayList<ChessMove> CheckAndAddNewSpace(ChessBoard board, ArrayList<ChessMove> pieceMoves, ChessPiece piece, ChessPosition myPosition, int row, int col){
        if (CheckSpotOpen(board, row, col)) {
            pieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
        } else {
            if (CheckSpotOccupiedByOtherColor(board, piece, row, col)){
                pieceMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(row, col), null));
            }
        }
        return pieceMoves;
    }
}
