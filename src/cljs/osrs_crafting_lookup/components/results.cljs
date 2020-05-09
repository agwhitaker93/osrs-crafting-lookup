(ns osrs-crafting-lookup.components.results
  (:require [rum.core :as rum]))

(def truthy? #{"true"})

(rum/defc item < { :key-fn #(:id %1) }
  [contents]
  (println (:id contents) ": " contents)
  [:div {:class "results-item"} [:img {:src (:icon_large contents)}]
   [:div {} (:name contents)]
   [:div {} (:description contents)]
   [:div {} (:price (:current contents))]
   [:div {} (if (truthy? (:members contents)) "Members-only" "Free to Play")]])

(rum/defc results < rum/reactive [result]
  (if (empty? (rum/react result))
    [:div {:class "results"} "Enter a search above"]
    [:div {:class "results"} (map item (rum/react result))]))
