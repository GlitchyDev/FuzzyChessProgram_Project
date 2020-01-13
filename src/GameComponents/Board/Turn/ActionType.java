package GameComponents.Board.Turn;

public enum ActionType {
    MOVEMENT_STANDARD, // 1 square movement in any direction

    MOVEMENT_PAWN_ADVANCE, // Pawn can move 2 spaces forward for its first movement
    ATTACK_PAWN,
    ATTACK_PAWN_EN_PASSANT, // ASK TEACHER ABOUT EN PASSANT https://en.wikipedia.org/wiki/En_passant
    MOVEMENT_KNIGHT, // L Shape movement
    ATTACK_KNIGHT, // L shape attack
    MOVEMENT_BISHOP, // 4 squares any direction
    ATTACK_BISHOP, // 4 squares any direction
    MOVEMENT_ROOK, // 3 Squares any Direction
    MOVEMENT_CASTLE_ROOK, // Castle movement for Rook
    ATTACK_ROOK, // 3 Squares any Direction
    MOVEMENT_QUEEN, // 5 Squares any Direction
    ATTACK_QUEEN, // 5 Squares any Direction
    MOVEMENT_CASTLE_KING, // Castle movement for King
    ATTACK_KING,

}
