package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		currentPlayer = Color.WHITE;
		turn = 1;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer(){
		return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}

		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);

		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();

		validateSourcePosition(source);
		validateTargetPosition(source, target);

		Piece capturedPiece = makeMove(source, target);
		nextTurn();
		
		return (ChessPiece) capturedPiece;
	}

	private void validateSourcePosition(Position source) {
		if (!board.thereIsAPiece(source)) {
			throw new ChessException("There is no piece on source position");
		}
		
		ChessPiece piece = (ChessPiece) board.piece(source);
		if (piece.getColor() != currentPlayer) {
			throw new ChessException("The chosen piece is not yours");
		}
		
		if (!board.piece(source).isThereAnyPossibleMove()) {
			throw new ChessException("There isn't any possible move for this piece");
		}
		
		
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}

	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		return capturedPiece;
	}

	public void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	public void initialSetup() {
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('g', 4, new Rook(board, Color.BLACK));
		placeNewPiece('c', 5, new King(board, Color.WHITE));
	}
}
