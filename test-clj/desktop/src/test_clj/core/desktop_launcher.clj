(ns test-clj.core.desktop-launcher
  (:require [test-clj.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. test-clj "test-clj" 800 600)
  (Keyboard/enableRepeatEvents true))
