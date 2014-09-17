(ns matrix-play.core
  (:gen-class)
  (:use clojure.core.matrix)
  (:require [clatrix.core :as cl]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn square-mat
  "Creates a square matrix of size n x n  who's
   elements are all e with optional arg for type"
  [n e & {:keys [implementation]
          :or {implementation :persistent-vector}}]
  (let [repeater #(repeat n %)]
    (matrix implementation (-> e repeater repeater))))

(defn id-mat
  "Creates an identity matrix of n x n size"
  [n]
  (let [init (square-mat n 0 :implementation :clatrix)
        identity-f (fn [i j n]
                     (if (= i j) 1 n))]
    (cl/map-indexed identity-f init)))

(defn rand-square-clmat
  "makes n x n matrix of random ints"
  [n]
  (cl/map rand-int (square-mat n 100 :implementation :clatrix)))
