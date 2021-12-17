(ns cnf)
(def ^:const OrS "∨")
(def ^:const AndS "∧")
(def ^:const NotS "¬")
(def ^:const VarS "V")
(def ^:const ConstS "C")

(definterface Expression
  (getChar [])
  (ToString []))

(deftype AbstractOperation [c args]
  Expression
  (getChar [_] (str c))
  (ToString [this] (str "(" (clojure.string/join (str " " c " ")  (map #(.ToString %) (.args this))) ")")))

(deftype AbstractUnaryOperation [c arg]
  Expression
  (getChar [_] (str c))
  (ToString [_] (str c (.ToString arg))))

(deftype VarConstPrototype [s value]
  Expression
  (getChar [_] (str s))
  (ToString [_] (str value))
  )

(defn toString [obj] (.ToString obj))
(defn getChar [value] (.getChar value))
(defn Constant [value] (VarConstPrototype. ConstS value))
(defn Variable [name] (VarConstPrototype. VarS name))
(defn Or [& args] (AbstractOperation. OrS args))

(defn And [& args] (AbstractOperation. AndS args))

(defn Not [v] (AbstractUnaryOperation. NotS v))

(defn deleteOperation [t] (fn [v] (if (= (getChar v) t)
                              (.args v)
                              (seq [v]))))

(defn simplifyOperation [op t] (fn [v] (if (= (getChar v) t)
                                 (apply op (mapcat (deleteOperation t) (.args v)))
                                 v)))

(def simplifyOr (simplifyOperation Or OrS))
(def simplifyAnd (simplifyOperation And AndS))

(defn isAnd? [v] (= (getChar v) AndS))
(defn distributOr [v] (let [AndArgs (filter isAnd? (.args v))
                          SomeArgs (filter #(not (isAnd? %)) (.args v))
                          ArgsWithoutOneAnd (concat SomeArgs (rest AndArgs))
                            AndCount (count AndArgs)]
                        (cond
                          (= AndCount 0) v
                          (= AndCount 1) (apply And (map #(simplifyOr (apply Or (conj ArgsWithoutOneAnd %))) (.args (first AndArgs))))
                          (= AndCount 2) (apply And (map distributOr (map #(simplifyOr (apply Or (conj ArgsWithoutOneAnd %))) (.args (first AndArgs))) ) )
                          )
                       ))

(declare push-1)

(defn push-0 [v] (let [symbol (getChar v)]
                   (cond
                   (= symbol AndS) (simplifyAnd (apply And (map #(push-0 %) (.args v))))
                   (= symbol OrS)  (simplifyAnd (distributOr (simplifyOr (apply Or (map #(push-0 %)  (.args v))))))
                   (= symbol NotS) (push-1 (.arg v))
                   (= symbol VarS) v
                   (= symbol ConstS) v
                   )))

(defn push-1 [v] (let [symbol (getChar v)]
                      ( cond
                      (= symbol AndS) (simplifyAnd (distributOr (simplifyOr (apply Or (map #(push-1 %) (.args v))))))
                      (= symbol OrS) (simplifyAnd (apply And (map #(push-1 %) (.args v)) ))
                      (= symbol NotS) (push-0 (.arg v))
                      (= symbol VarS) (Not v)
                      (= symbol ConstS) (Not v)
                      )))
(defn push [v] (push-0 v))
(def x (Not
         (And
           (Variable "x")  (Not (Or (Constant 0) (And (Variable "y") (Variable "z") (Variable "m")))) (And (Not (Variable "e")) (Variable "z"))
           ) ) )

(println (str "expr: " (toString x)))
(println (str "cnf: " (toString (push-0 x))))

(def operations {'| Or '& And '- Not})

(defn parse [expr]
  (cond
    (list? expr) (apply (operations (first expr)) (map parse (rest expr)))
    (number? expr) (Constant expr)
    :else (Variable (str expr))))

(defn parseObject [expression] (parse (read-string expression)))
(def y (parseObject "(| x 1)"))
;(println (apply #() "(a~ | ~b)"))
(println (toString (push y)))

;остался парсер и парсер ~, по желанию сделать упрощение формулы