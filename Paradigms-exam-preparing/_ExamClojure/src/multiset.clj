(ns multiset)
(definterface IMultiSet
  (add [x])
  (add [x k])
  (getKV [k])
  (contains [x])
  (remove [x])
  (getm [])
  (intersect [x])
  (union [x])
  (getall [])
  )

(deftype JMultiSet [m]
  Object
  (equals [this that] (= this that))
  IMultiSet
  (add [_ x k] (let [e (find m x)]
                 (if (= e nil)
                   (JMultiSet. (assoc m x k))
                   (JMultiSet. (assoc m x (+ (second e) k)))
                   )))
  (add [_ x] (.add _ x 1))
  (getKV [_ k] (find m k))
  (contains [_ x] (boolean (find m x)))
  (remove [_ x] (let [e (find m x)]
                  (if (= (second e) 1)
                    (JMultiSet. (dissoc m x))
                    (JMultiSet. (assoc m x (- (second e) 1)))
                    )))

  (getm [this] m)
  (intersect [this x] (let [t (filter #(not (= % nil)) (map #(.getKV x (first %)) m))]
                        (JMultiSet.
                          (into (sorted-map)
                                (map #(if (< (second %) (get m (first %))) %
                                        (.getKV this (first %)) ) t) ))))
  (union [this x] (let [mx (.getm x)
                        o1 (map #(if (boolean (get m (first %)))
                                   ))]
                    (JMultiSet. tmp)))
  (getall [_] (print m))
  )

(deftype MutableMultiSet [^{:unsynchronized-mutable true} m]
  IMultiSet
  (add [_ x k] (set! m (.add m x k)))
  (add [_ x] (set! m (.add m x)))
  (getKV [_ k] (.getKV m k))
  (contains [_ x] (.contains m x))
  (remove [_ x] (set! m (.remove m x)))
  (getm [this] (.m this))
  (intersect [_ x] (set! m (.intersect m (.getm x))))
  (union [this x] (doseq [[k v] (.m (.getm x))] (.add this k v)))
  (getall [_] (.getall m))
  )

(def mms (JMultiSet. (sorted-map 1 2 3 3)))
(def mms2  (JMultiSet. (sorted-map 1 2, 3 1, 5 2, 6 1)))
(.getall mms)
(println)
(.getall mms2)
(println)
(.getall (.union mms2 mms))