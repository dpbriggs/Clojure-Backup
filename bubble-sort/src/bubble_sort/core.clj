(ns bubble-sort.core
  (:gen-class))

(defn bubble-sort
  [list-n]
  (let [swap (fn [v i1 i2] (assoc v i1 (v i1) i1 (v i2)))]
    (loop [head (take 2 list-n)
           tail (drop 2 list-n)
           sorted []]
      (if ()))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
