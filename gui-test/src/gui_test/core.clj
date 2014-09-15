(ns gui-test.core
  (:gen-class)
  (:use [seesaw.core]))

(defn -main
  [& args]
  (native!)
  (-> (frame :title "test",
             :size [300 :by 300],
             :on-close :exit,
             :content "Some content")
      pack!
      show!))
