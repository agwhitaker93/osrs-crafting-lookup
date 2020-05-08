(ns osrs-crafting-lookup.core
  (:require [rum.core :as rum]
            [osrs-crafting-lookup.components.nav :refer [nav]]
            [osrs-crafting-lookup.components.results :refer [results]]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(rum/defc greeting < rum/reactive []
   [:h1 (:text (rum/react app-state))])

(rum/defc app []
  [:div {} [(nav)
            (results)]])

(defn render []
  (rum/mount (app) (. js/document (getElementById "app"))))
