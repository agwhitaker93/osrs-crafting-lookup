(ns osrs-crafting-lookup.components.results
  (:require [rum.core :as rum]))

(rum/defc results []
  [:div {:class "results"} (str "You searched for " "Hello World")])
