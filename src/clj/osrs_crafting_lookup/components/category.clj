(ns osrs-crafting-lookup.components.category
  (:require [osrs-crafting-lookup.config :refer [base-url pages]]))
; This is here just in case more categories are added

(def category-id 1)                                         ; only 1 category in osrs

(defn category-url []
  (str base-url "/category.json?category=" category-id))

(defn handle [& args]
  (println "Not implemented yet"))
