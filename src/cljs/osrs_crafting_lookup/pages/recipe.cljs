(ns osrs-crafting-lookup.pages.recipe
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(defonce recipe-results (atom {}))

(defn update-recipe-results [new]
  (swap! recipe-results #(identity new)))

(rum/defc fetch-recipes [id]
  (GET "/api/recipe" {:params          {:id id}
                      :response-format :json
                      :keywords?       true
                      :handler         #(update-recipe-results %1)})
  [:div {:class "results"} (str "Fetching recipes for id: " id)])

(rum/defc recipe < rum/reactive [id]
  (if (empty? (rum/react recipe-results))
    (fetch-recipes id)
    [:div {} (str (rum/react recipe-results))]))
