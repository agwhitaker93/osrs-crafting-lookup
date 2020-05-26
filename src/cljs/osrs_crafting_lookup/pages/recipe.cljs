(ns osrs-crafting-lookup.pages.recipe
  (:require [rum.core :as rum]
            [ajax.core :refer [GET]]))

(defonce previous-id (atom ""))
(defonce recipe-results (atom {}))

(defn update-recipe-results [new]
  (swap! recipe-results #(identity new)))

(rum/defc materials [materials]
  [:div {} [:h1 {} "Materials"]
   (map (fn [material]
          (identity [:div
                     [:h2 (str (:id (first material)) "-" (:recipe_id (first material)))]
                     (map (fn [inner] [:p (str inner)]) material)])) materials)])

(rum/defc target [target]
  [:div {} [:h1 {} "Target"] [:p {} (str target)]])

(rum/defc products [products]
  [:div {} [:h1 {} "Products"] (map #(identity [:div [:h2 (:id %1)] [:p (str %1)]]) products)])

(rum/defc fetch-recipes [id]
  (GET "/api/recipe" {:params          {:id id}
                      :response-format :json
                      :keywords?       true
                      :handler         #(update-recipe-results %1)})
  [:div {:class "results"} (str "Fetching recipes for id: " id)])

(rum/defc recipe < rum/reactive [id]
  (if (not (= (deref previous-id) id))
    (do
      (update-recipe-results {})
      (swap! previous-id #(identity id))))
  (if (empty? (rum/react recipe-results))
    (fetch-recipes id)
    (let [results (rum/react recipe-results)]
      [:div {}
       (materials (map #(get %1 :materials) (get-in results [:target :recipes])))
       (target (:target results))
       (products (:products results))])))
