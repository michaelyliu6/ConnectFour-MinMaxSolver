import numpy as np
import random
import sys
import math

NUM_ROWS = 6
NUM_COLUMNS = 7


def initialize_board():
    board = np.zeros((NUM_ROWS, NUM_COLUMNS))
    return board


def drop_piece(board, row, col, piece):
    board[row][col] = piece


def is_valid_move(board, col):
    return board[NUM_ROWS - 1][col] == 0


def get_open_row(board, col):
    for row in range(NUM_ROWS):
        if board[row][col] == 0:
            return row


def print_board(board):
    print(np.flipud(board))


def is_game_over(board, piece):
    # Check Horizontal Locations
    for r in range(NUM_ROWS):
        for c in range(NUM_COLUMNS-3):
            if board[r][c] == piece and board[r][c+1] == piece and \
                    board[r][c+2] == [piece] and board[r][c+3] == piece:
                return True

    # Check Vertical Locations
    for r in range(NUM_ROWS-3):
        for c in range(NUM_COLUMNS):
            if board[r][c] == piece and board[r+1][c] == piece and \
                    board[r+2][c] == piece and board[r+3][c] == piece:
                return True

    # Check Positive Slope Diagonals
    for r in range(NUM_ROWS-3):
        for c in range(NUM_COLUMNS-3):
            if board[r][c] == piece and board[r+1][c+1] == piece and \
                    board[r+2][c+2] == piece and board[r+3][c+3] == piece:
                return True

    # Check Negative Slop Diagonals
    for r in range(3, NUM_ROWS):
        for c in range(NUM_COLUMNS-3):
            if board[r][c] == piece and board[r-1][c+1] == piece and \
                    board[r-2][c+2] == piece and board[r-3][c+3] == piece:
                return True
    return False


def score_window(window, piece):
    score = 0
    opponent_piece = 1
    if piece == 1:
        opponent_piece = 2

    if window.count(piece) == 4:
        score += 100
    elif window.count(piece) == 3 and window.count(0) == 1:
        score += 5
    elif window.count(piece) == 2 and window.count(0) == 2:
        score += 2

    if window.count(opponent_piece) == 3 and window.count(0) == 1:
        score -= 4

    return score


def evaluation_function(board, piece):
    score = 0

    # Score Center Column
    center_arr = [int(i) for i in list(board[:, 4])]
    center_count = center_arr.count(piece)
    score += center_count * 3

    # Score Rows
    for r in range(NUM_ROWS):
        row_arr = [int(i) for i in list(board[r, :])]
        for c in range(NUM_COLUMNS-3):
            window = row_arr[c:c+4]
            score += score_window(window, piece)

    # Score Columns
    for c in range(NUM_COLUMNS):
        col_arr = [int(i) for i in list(board[:, c])]
        for r in range(NUM_ROWS-3):
            window = col_arr[r:r+4]
            score += score_window(window, piece)

    # Score positive sloped diagonals
    for r in range(NUM_ROWS-3):
        for c in range(NUM_COLUMNS-3):
            window = [board[r+i][c+i] for i in range(4)]
            score += score_window(window, piece)

    # Score negative sloped digaonals
    for r in range(NUM_ROWS-3):
        for c in range(NUM_COLUMNS-3):
            window = [board[r+3-i][c+i] for i in range(4)]
            score += score_window(window, piece)

    return score


def get_valid_moves(board):
    valid_moves = []
    for col in range(NUM_COLUMNS):
        if is_valid_move(board, col):
            valid_moves.append(col)
    return valid_moves


def is_terminal_node(board):
    return is_game_over(board, 1) or is_game_over(board, 2) \
        or len(get_valid_moves(board)) == 0


def minimax(board, depth, alpha, beta, player):
    valid_moves = get_valid_moves(board)
    is_terminal = is_terminal_node(board)
    if is_terminal:
        if is_game_over(board, 2):
            return (None, 100000000)
        elif is_game_over(board, 1):
            return (None, -100000000)
    if depth == 0:
        return (None, evaluation_function(board, 2))
    if player:
        best_score = -math.inf
        column = random.choice(valid_moves)
        for col in valid_moves:
            row = get_open_row(board, col)
            board_copy = board.copy()
            drop_piece(board_copy, row, col, 2)
            score = minimax(board_copy, depth-1, alpha, beta, False)[1]
            if score > best_score:
                best_score = score
                column = col
            alpha = max(alpha, score)
            if alpha >= beta:
                break
        return column, best_score
    else:
        best_score = math.inf
        column = random.choice(valid_moves)
        for col in valid_moves:
            row = get_open_row(board, col)
            board_copy = board.copy()
            drop_piece(board_copy, row, col, 1)
            score = minimax(board_copy, depth-1, alpha, beta, True)[1]
            if score < best_score:
                best_score = score
                column = col
            beta = min(beta, score)
            if alpha >= beta:
                break

        return column, best_score


# board = initialize_board()
# drop_piece(board, 0, 0, 1)
# print(is_game_over(board, 0))
# print_board(board)


def play():
    board = initialize_board()
    while True:
        move = int(input("Enter a move: ")) - 1
        if is_valid_move(board, move):
            row = get_open_row(board, move)
            drop_piece(board, row, move, 1)

            ai_move, score = minimax(board, 5, -math.inf, math.inf, True)
            print("The AI dropped a piece in column " + str(ai_move + 1))
            row = get_open_row(board, ai_move)
            drop_piece(board, row, ai_move, 2)
            print_board(board)


play()
