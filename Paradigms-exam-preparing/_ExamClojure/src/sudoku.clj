(ns sudoku)

(def testPuzzle [
                        [ 0, 0, 0, 0, 6, 0, 7, 0, 0 ],
                        [ 0, 5, 9, 0, 0, 0, 0, 0, 0 ],
                        [ 0, 1, 0, 2, 0, 0, 0, 0, 0 ],
                        [ 0, 0, 0, 1, 0, 0, 0, 0, 0 ],
                        [ 6, 0, 0, 5, 0, 0, 0, 0, 0 ],
                        [ 3, 0, 0, 0, 0, 0, 4, 6, 0 ],
                        [ 0, 0, 0, 0, 0, 0, 0, 0, 0 ],
                        [ 0, 0, 0, 0, 0, 0, 0, 9, 1 ],
                        [ 8, 0, 0, 7, 4, 0, 0, 0, 0 ]
                        ])

(defn getColV [puzzle, colI]
  (apply hash-set (filter pos? (nth puzzle colI))))


(defn getRowV [puzzle, rowI] (apply hash-set (filter pos? (map #(nth % rowI) puzzle) )))

(defn getSquareV [puzzle, rowI, colI] (let [sqrl (* 3 (quot rowI 3))
                                           sqrr (* 3 (inc (quot rowI 3)))
                                           sqcl (* 3 (quot colI 3))
                                           sqcr (* 3 (inc (quot colI 3)))]
                                       (apply hash-set (filter pos? (mapcat #(subvec % sqcl sqcr) (subvec puzzle sqrl sqrr)))) ))
(println (getSquareV testPuzzle 1 1))
