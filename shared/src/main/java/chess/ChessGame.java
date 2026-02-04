package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
        ArrayList<ChessMove> validMovesList = new ArrayList<>();
        ChessBoard board = getBoard();
        for (ChessMove move : moves){
            ChessBoard boardCopy = new ChessBoard(board);
            boardCopy.addPiece(move.getEndPosition(), piece);
            boardCopy.addPiece(move.getStartPosition(), null);
            setBoard(boardCopy);
            if (!isInCheck(piece.getTeamColor())){
                validMovesList.add(move);
            }
            setBoard(board);
        }
        return validMovesList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if (piece == null){
            throw new InvalidMoveException();
        }
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }
        if (!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException();
        }
        if (move.getPromotionPiece() == null){
            getBoard().addPiece(move.getEndPosition(), piece);
            getBoard().addPiece(move.getStartPosition(), null);
        }
        else{
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            getBoard().addPiece(move.getEndPosition(), piece);
            getBoard().addPiece(move.getStartPosition(), null);
        }
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++) {
                ChessPiece piece = getBoard().getPiece(new ChessPosition(row, col));
                if (piece != null){
                    if (piece.getTeamColor() != teamColor){
                        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(board, new ChessPosition(row, col));
                        for (ChessMove move : moves) {
                            ChessPiece targetPiece = getBoard().getPiece(move.getEndPosition());
                            if (targetPiece != null) {
                                if (targetPiece.getPieceType() == ChessPiece.PieceType.KING && targetPiece.getTeamColor() == teamColor) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(teamTurn);
    }
}
