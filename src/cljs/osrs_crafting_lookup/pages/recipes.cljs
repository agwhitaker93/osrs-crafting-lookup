(ns osrs-crafting-lookup.pages.recipes
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]
            [osrs-crafting-lookup.components.item-card :refer [card]]))

(defonce previous-search (atom ""))
(defonce recipe-results (atom {}))

(defn update-recipe-results [new]
  (js/console.log "We have these results: " (str new))
  (if (not (or (nil? new) (empty? new)))
    (swap! recipe-results #(identity new))))

(defn narrow-selection [cb id]
  (let [page-title (. js/document -title)]
    (cb "recipe" id)
    (js/history.pushState {"recipe" id} page-title (str "?recipe=" id))))

(rum/defc fetch-recipes [recipe-name]
  (GET "/api/recipes" {:params          {:name recipe-name}
                       :response-format :json
                       :keywords?       true
                       :handler         #(update-recipe-results (:results %1))})
  [:div {:class "results"} (str "Fetching recipes for \"" recipe-name "\"")])

(rum/defc recipes < rum/reactive [narrow-selection-cb name]
  (if (not (= (deref previous-search) name))
    (do
      (update-recipe-results {})
      (swap! previous-search #(identity name))))
  (if (empty? (rum/react recipe-results))
    (fetch-recipes name)
    (->> (rum/react recipe-results)
         (map (fn [result]
                {:header {:img       (:icon result)
                          :title     (:name result)
                          :on-click  #(narrow-selection narrow-selection-cb (:id result))
                          :wiki-link (:wiki result)}
                 :body   (str (:examine result))
                 :footer {:left  (if (:members_only result)
                                   "Members-only"
                                   "Free to Play")
                          :right "100gp"}}))
         (map card)
         (conj [:div {:class "results flex-container"}]))))
