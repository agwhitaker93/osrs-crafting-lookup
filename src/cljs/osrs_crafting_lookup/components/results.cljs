(ns osrs-crafting-lookup.components.results
  (:require [rum.core :as rum]))

(def truthy? #{"true"})

(rum/defc item < {:key-fn #(:id %1)} [contents]
  [:div {:class "results-item" :id (:id contents)}
   [:div {:class "results-item-header"}
    [:img {:class "results-item-icon"
           :src   (:icon contents)}]
    [:div {:class "results-item-name"} (:name contents)]]
   [:div {:class "results-item-description"} (:examine contents)]
   [:div {:class "results-item-footer"}
    [:div {:class "results-item-members"} (if (:members_only contents)
                                            "Members-only"
                                            "Free to Play")]
    [:div {:class "results-item-price"} (:value contents)]]])

(rum/defc results < rum/reactive [result]
  (if (empty? (rum/react result))
    [:div {:class "results"} "Enter a search above"]
    (if (string? (rum/react result))
      [:div {:class "results"} (rum/react result)]
      [:div {:class "results flex-container"} (map item (rum/react result))])))
