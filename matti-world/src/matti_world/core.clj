(ns matti-world.core
  (:gen-class))

(defn fib
  [n]
  (if (<= n 2)
    1
    (+ (fib (- n 1)) (fib (- n 2)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
