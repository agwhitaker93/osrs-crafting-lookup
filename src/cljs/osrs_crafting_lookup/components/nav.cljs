(ns osrs-crafting-lookup.components.nav
  (:require [rum.core :as rum]))

(def search-term (atom "Placeholder"))

(defn submit-search [event]
  ((.-log js/console) "Hello " event search-term))

(defn update-search-term [event]
  (swap! search-term #(str (-> event
                               .-target
                               .-value))))

(rum/defc home []
  [:button {:class "nav-inner nav-home"} "Home"])

(rum/defc search []
  [:div {} [:input {:class "nav-inner nav-search"
                    :type "text"
                    :placeholder "Search..."
                    :on-change update-search-term}]
   [:button {:class "nav-inner nav-search-button"
             :on-click submit-search} "Search"]])

(rum/defc nav []
  [:div {:class "nav-bar"} [(home)
                            (search)]])
