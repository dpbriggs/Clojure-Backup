(ns basic-cypto.core
  (:gen-class))

;; Map of values with letters --> value
(def letters (let [letters (clojure.string/split
                            "abcdefghijklmnopqrstuvwxyz" #"")
                   values (range 26)]
               (zipmap (map keyword letters) values)))


;; for decrypting, reverse set
(def reverse-letters (clojure.set/map-invert letters))

(defn get-letter-value
  "Returns values of string letter from letters"
  [letter]
  ((keyword letter) letters))


(defn strip-text
  [text]
  (.toLowerCase (.replaceAll text "[^a-zA-z]" "")))

(defn text-to-values
  [text]
  (let [stripped-text (strip-text text)
        text-seq (clojure.string/split stripped-text #"")]
    (mapv get-letter-value text-seq)))

(defn gen-key
  [text]
  (mapv #(* (rand-int 1000000) %) (repeat (count (strip-text text)) 1)))

(defn encrypt
  [text encrypt-key]
  (loop [text-body (text-to-values text)
         encrypt-key-body encrypt-key
         message []]
    (if (empty? text-body)
      message
      (recur (rest text-body)
             (rest encrypt-key-body)
             (conj message
                   (mod (+ (first text-body)
                        (first encrypt-key-body)) 26))))))

(defn value-to-letter
  [value]
  (name (get reverse-letters value)))

(defn decrypt
  [text encrypt-key]
  (loop [text-body text
         encrypt-key-body encrypt-key
         message []]
    (if (empty? text-body)
      (->> message
           (map value-to-letter)
           (apply str))
      (recur (rest text-body)
             (rest encrypt-key-body)
             (conj message
                   (mod (- (first text-body)
                        (first encrypt-key-body)) 26))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
