(ns osrs-crafting-lookup.components.recipes
  (:require [osrs-crafting-lookup.database :refer [get-item-details-matching-name memoized-page-count get-item-details get-recipe-details get-recipe-skills get-recipe-materials]]
            [osrs-crafting-lookup.util :refer [parse-int]]))

(defn get-skills-for-recipe [{id :id recipe-id :recipe_id}]
  (get-recipe-skills id recipe-id))

(defn get-materials-for-recipe [{id :id recipe-id :recipe_id}]
  (get-recipe-materials id recipe-id))

(defn get-recipe [{id :id}]
  (let [item-details (first (get-item-details id))
        recipe-details (map #(assoc %1
                               :skills (get-skills-for-recipe %1)
                               :materials (get-materials-for-recipe %1))
                            (get-recipe-details id))]
    {:body (assoc item-details :recipes recipe-details)}))

(defn get-recipes [{name :name limit :limit page :page}]
  (let [limit (if (nil? limit) 15 (parse-int limit))
        page (if (nil? page) 1 (parse-int page))]
    {:body {:results (get-item-details-matching-name name limit (* (dec page) limit)) :pages (memoized-page-count name limit)}}))
