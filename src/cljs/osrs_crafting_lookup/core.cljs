(ns osrs-crafting-lookup.core
  (:require [rum.core :as rum]
            [osrs-crafting-lookup.components.nav :refer [nav]]
            [osrs-crafting-lookup.components.results :refer [results]]))

(defonce search-result (atom ""))

(defn update-search-result [new]
  (swap! search-result #(str new)))

(enable-console-print!)

(rum/defc app []
  [:div {} [(nav update-search-result)
            (results search-result)]])

(defn render []
  (rum/mount (app) (. js/document (getElementById "app"))))
