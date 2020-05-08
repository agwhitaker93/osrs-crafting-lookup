(ns osrs-crafting-lookup.components.results
  (:require [rum.core :as rum]))

(rum/defc results < rum/reactive [result]
  [:div {:class "results"} (str "You searched for " (rum/react result))])
