(ns osrs-crafting-lookup.pages.recipes
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(defonce previous-search (atom ""))
(defonce recipe-results (atom {}))

(defn update-recipe-results [new]
  (swap! recipe-results #(identity new)))

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

(rum/defc item < {:key-fn #(:id %2)} [cb contents]
  [:div {:class "results-item"
         :id    (:id contents)}
   [:div {:class    "results-item-header"
          :on-click #(narrow-selection cb (:id contents))}
    [:img {:class "results-item-icon"
           :src   (:icon contents)}]
    [:div {:class "results-item-name"} (:name contents)]]
   [:div {:class "results-item-description"} (:examine contents)]
   [:div {:class "results-item-footer"}
    [:div {:class "results-item-members"} (if (:members_only contents)
                                            "Members-only"
                                            "Free to Play")]
    [:div {:class "results-item-price"} (:value contents)]]])

(rum/defc recipes < rum/reactive [narrow-selection-cb name]
  (if (not (= (deref previous-search) name))
    (do
      (update-recipe-results {})
      (swap! previous-search #(identity name))))
  (if (empty? (rum/react recipe-results))
    (fetch-recipes name)
    [:div {:class "results flex-container"} (map #(item narrow-selection-cb %1) (rum/react recipe-results))]))
