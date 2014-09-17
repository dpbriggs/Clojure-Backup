(ns squid-neural-networks.core
  (:gen-class)
  (:use clojure.core.matrix))

(def input-neurons [1 0])
(def input-hidden-strengths [[0.12 0.2 0.13]
                             [0.01 0.02 0.03]])

(def hidden-neurons [0 0 0])
(def hidden-output-strengths [[0.15 0.16]
                              [0.02 0.03]
                              [0.01 0.02]])

(def activation-fn (fn [x] (Math/tanh x)))
(def deactivation-fn (fn [y] (- 1.0 (* y y))))

(defn layer-activation [inputs strengths]
  "forward propagate the input of a layer"
  (mapv activation-fn
      (mapv #(reduce + %)
       (* inputs (transpose strengths)))))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
