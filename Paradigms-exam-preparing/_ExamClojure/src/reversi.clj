(ns reversi)
(def m_const 8)
(def n_const 8)
(definterface IBoardConfiguration
  (between [x y])
  (getCell [x y])
  (setCell [x y v])
  (haveSym [x y vx vy sym])
  (isCorrect [x y sym])
  (fillLine [x y vx vy sym])
  (getM [])
  (getN [])
  (getBoard [])
  )
(deftype BoardConfiguration [^{:unsynchronized-mutable true} board]
  IBoardConfiguration
  (getM [_] m_const)
  (getN [_] n_const)
  (getBoard [_] board)
  (getCell [_ x y] (nth (nth board y) x))
  (setCell [_ x y v] (set! board (assoc board x (assoc (nth board x) y v))))
  (between [_ x y])
  (haveSym [_ x y vx vy sym])
  (isCorrect [_ x y sym])
  (fillLine [_ x y vx vy sym])
  )
(def startPuzzle [
                  [".", ".", ".", ".", ".", ".", ".", "."],
                  [".", ".", ".", ".", ".", ".", ".", "."],
                  [".", ".", ".", ".", ".", ".", ".", "."],
                  [".", ".", ".", "B", "W", ".", ".", "."],
                  [".", ".", ".", "W", "B", ".", ".", "."],
                  [".", ".", ".", ".", ".", ".", ".", "."],
                  [".", ".", ".", ".", ".", ".", ".", "."],
                  [".", ".", ".", ".", ".", ".", ".", "."]
                 ])

(def board (BoardConfiguration. startPuzzle))

(.setCell board 2 2 "W")
(println (.getBoard board))