(ns osrs-crafting-lookup.pages.recipes
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]
            [osrs-crafting-lookup.components.item-card :refer [card]]))

(defn narrow-selection [cb id]
  (js/console.log "Fetching results for: " id)
  (cb "/api/recipe" {:id id} ["recipe" id]))

(rum/defc recipes [recipes contents cb]
  (->> (:results contents)
       (map (fn [result]
              {:header {:img       (:icon result)
                        :title     (:name result)
                        :on-click  #(narrow-selection cb (:id result))
                        :wiki-link (:wiki result)}
               :body   (str (:examine result))
               :footer {:left  (if (:members_only result)
                                 "Members-only"
                                 "Free to Play")
                        :right (:value result)}}))
       (map card)
       (conj [:div {:class "results flex-container"}])))
