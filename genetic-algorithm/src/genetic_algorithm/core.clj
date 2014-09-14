(ns genetic-algorithm.core
  (:gen-class))

(defstruct city :name :x :y :pos)

;; Ascii map for city name gen
(def ascii-map #(get (vec (map char (range 65 91))) %))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  Generate city information  ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn gen-city-name
  "Adds ascii char to number to identify city"
  [num]
  (if (>= num 26)
    (str (ascii-map (mod num 26)) (gen-city-name (- num 25)))
    (str (ascii-map num))))


(defn gen-random-city-scape
  "Generates num-cities amount of random cities on a map"
  [num-cities dist-x dist-y]
  ;; Create map of randomly generated cities
  (vec (loop [num num-cities
              cities []]
         (if (= num 0)
           cities
           (let [n-num (- num 1)
                 new-city (struct city
                                  (gen-city-name n-num)
                                  (rand-int dist-x)
                                  (rand-int dist-y)
                                  (- num 1))
                 n-cities (conj cities new-city)]
             (recur n-num n-cities))))))

(defn scramble-city-order
  "Returns num amount of randomly permuted city routes"
  [cities]
  (distinct (into [] (repeatedly (* (count cities) 6) #(shuffle cities)))))

(defn distance-between
  "Finds distance between two cities"
  [city-1 city-2]
  (let [xdist (Math/abs (- (:x city-1) (:x city-2)))
        ydist (Math/abs (- (:y city-1) (:y city-2)))]
    (Math/pow (+ (* xdist xdist) (* ydist ydist)) 0.5)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Start of fitness-test ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Routes are a vector of cities in which index[0] is first stop, etc

(defn route-distance
  "Finds total distance taken in route"
  [route]
  (loop [current-city route
         next-city (rest route) ;; ensure
         distance 0]
    (if (= (count next-city) 0)
      {:distance distance :route route}
      ;; n --> new
      (let [n-current-city (rest current-city)
            n-next-city (rest next-city)
            n-distance (+ distance (distance-between
                                   (first current-city)
                                   (first next-city)))]
        (recur n-current-city n-next-city n-distance)))))

(defn fitness-test
  "Grabs top N routes"
  [N routes]
  (let [fitness (vec (take N (sort-by :distance (map route-distance routes))))
        fittest (:distance (first fitness))
        new-routes (vec (map :route fitness))]
    {:top-distance fittest :routes new-routes}))


;;;;;;;;;;;;;;;;;;;;;;;;
;; Start of crossover ;;
;;;;;;;;;;;;;;;;;;;;;;;;


(defn gen-starting-pop
  "Convenience function to grab fit pop"
  [fitness-number num-cities city-dist-x city-dist-y]
  (fitness-test fitness-number
                (scramble-city-order (gen-random-city-scape num-cities
                                                            city-dist-x
                                                            city-dist-y))))
(defn random-swath
  [parent-1]
  (let [size (apply count parent-1)
        pos-1 (rand-int size)
        pos-2 (rand-int size)
        o (println (- pos-1 pos-2) " : " pos-1 " - " pos-2)]
    (if (and (>= pos-1 pos-2) (>= (Math/abs (- pos-1 pos-2)) 3))
      (random-swath parent-1)
      (range pos-1 pos-2))))


(defn pmx-crossover
  [parent-1 parent-2])

(defn rand-int-between
  [upper-bound lower-bound]
  (+ (rand-int (- upper-bound lower-bound)) lower-bound))

(defn swap [pop i1 i2]
  (let [v (vec pop)]
   (vec (assoc v i2 (v i1) i1 (v i2)))))

(defn mutate
  [child]
  (if (>= (rand-int 99) 10) ;; 10% chance
    child
    (loop [i1 (rand-int (count child))
           i2 (rand-int  (count child))] ;; ensure we're not flipping same vals
      (if (not= i1 i2)
        (swap child i1 i2)
        (recur (rand-int (count child))
               (rand-int (count child)))))))

(defn non-swath-values
  [parent-2 swath]
  (filter #(not (contains? swath)) parent-2))

(defn simple-crossover
  "Splices two vectors together to produce two children"
  ;Parent 1         1 2 3 4 5 6 7 8        "
  ;Swath            1 2 3 - - - 7 8        "
  ;Parent 2         8 7 6 5 4 3 2 1        "
  ;                                        "
  ;Result    -->    1 2 3 5 4 3 2 1        "
  ;Parent 2 vals  --->    - - -            "
  ;Repeat with reversed parents            "

  [[parent-1 parent-2]]
  (let [len (count parent-1)
        swath (rand-int-between (- len 2) (quot len 4))
        starting-pos (rand-int (- len swath))

        ;; Parent 1
        swath-part-1 (->> parent-2
                        (take (+ swath starting-pos))
                        (drop starting-pos)
                        (vec))
        non-swath-1 (remove (set swath-part-1) parent-1)
        head-1 (take starting-pos non-swath-1)
        tail-1 (take-last (- len swath starting-pos) non-swath-1)
        child-1 (mutate (concat head-1 swath-part-1 tail-1))

        ;; Parent 2 (same thing except parents are flipped)
        swath-part-2 (->> parent-1
                        (take (+ swath starting-pos))
                        (drop starting-pos)
                        (vec))
        non-swath-2 (remove (set swath-part-2) parent-2)
        head-2 (take starting-pos non-swath-2)
        tail-2 (take-last (- len swath starting-pos) non-swath-2)
        child-2 (mutate (concat head-2 swath-part-2 tail-2))]
    [child-1 child-2]))


(defn apply-crossover
  [routes]
  (let [partners (partition 2 (shuffle routes))
        children-1 (map simple-crossover partners)
        children-2 (map simple-crossover partners) ;; Double the children
        all-children (concat children-1 children-2)]
    (apply concat all-children)))

(defn evolution-cycles
  "applies crossover/fitness cycles num-cycles amount of times to a route"
  [route num-cycles top-fit-num]
  (loop [stop num-cycles
         population (:routes (fitness-test top-fit-num (scramble-city-order route)))
         fitness-dists []]
    (if (= stop 0)
      {:top-distances fitness-dists
       :population population
       :top-route (first population)}
      (let [n-stop (- stop 1)
            fit (fitness-test top-fit-num (apply-crossover population))
            n-population (:routes fit)
            n-fitness-dists (conj fitness-dists (:top-distance fit))]
        (recur n-stop n-population n-fitness-dists)))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
