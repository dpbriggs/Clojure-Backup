(ns bubble-sort.core
  (:gen-class))

(defn displayPathtoPrincess [m grid] (println grid))

(defn -main []
  (let [m (read-line)
        grid (dorun (take m (map #(seq (.toCharArray %)) (repeatedly #(read-line)))))]
    (displayPathtoPrincess m grid)))
