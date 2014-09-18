(ns sherlock-and-squares.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def certainty 5)

(defn prime? [n]
      (.isProbablePrime (BigInteger/valueOf n) certainty))

(defn prime-list [n]
  (->> Integer/MAX_VALUE
       (range 1)
       (take-nth 2)
       (filter prime?)
       (take n)))

(defn sieve [n]
  "Returns a BitSet with bits set for each prime up to n"
  (let [bs (new java.util.BitSet n)]
    (.flip bs 2 n)
    (doseq [i (range 4 n 2)] (.clear bs i))
    (doseq [p (range 3 (Math/sqrt n))]
      (if (.get bs p)
	(doseq [q (range (* p p) n (* 2 p))] (.clear bs q))))
    bs))

(defn sq
  [n]
  (->> n
       range
       (filter even?)
       (map #(Math/pow % 2))))
