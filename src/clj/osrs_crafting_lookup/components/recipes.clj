(ns osrs-crafting-lookup.components.recipes
  (:require [osrs-crafting-lookup.database :refer [get-item-details-matching-name get-item-details get-recipe-details get-recipe-skills get-recipe-materials]]))

(defn get-skills-for-recipe [{id :id recipe-id :recipe_id :as recipe}]
  (get-recipe-skills id recipe-id))

(defn get-materials-for-recipe [{id :id recipe-id :recipe_id}]
  (get-recipe-materials id recipe-id))

(defn get-recipes [{name :name}]
  (get-item-details-matching-name name))

(defn get-recipe [{id :id}]
  (let [item-details (first (get-item-details id))
        recipe-details (map #(assoc %1
                               :skills (get-skills-for-recipe %1)
                               :materials (get-materials-for-recipe %1))
                            (get-recipe-details id))]
    {:body (assoc item-details :recipes recipe-details)}))
