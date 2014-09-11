(ns genetic-algorithm.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defstruct city :x :y)

(defn gen-random-city-scape
  "Generates several random cities"
  [num-cities dist-x dist-y]
  {:num num-cities
   :map (repeatedly num-cities #(struct city (rand-int dist-x) (rand-int dist-y)))})

(defn city-dist
  "Finds distance between two cities"
  [city-1 city-2]
  (let [xdist (Math/abs (- (:x city-1) (:x city-2)))
        ydist (Math/abs (- (:y city-1) (:y city-2)))]
    (Math/pow (+ (* xdist xdist) (* ydist ydist)) 0.5)))
