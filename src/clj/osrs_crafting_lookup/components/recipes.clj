(ns osrs-crafting-lookup.components.recipes
  (:require [osrs-crafting-lookup.database :as db]
            [osrs-crafting-lookup.util :refer [parse-int]]))

(declare get-materials-for-recipe)

(defn get-skills-for-recipe [{id :id recipe-id :recipe_id}]
  (db/get-recipe-skills id recipe-id))

(defn look-back [name]
  (let [item-details (first (db/get-item-details-by-name name))
        recipe-details (map #(assoc %1
                               :skills (get-skills-for-recipe %1)
                               :materials (get-materials-for-recipe %1 true))
                            (if (nil? (:id item-details))
                              []
                              (db/get-recipe-details (:id item-details))))]
    recipe-details))

(defn look-forward [name]
  (let [materials (db/get-recipe-materials-by-name name)
        recipes (map #(first (db/get-recipe-details-for-recipe (:id %1) (:recipe_id %1))) materials)
        recipes (map #(assoc %1 :skills (db/get-recipe-skills (:id %1) (:recipe_id %1))) recipes)]
    recipes)
  )

(defn get-recipe [{id :id}]
  (let [item-details (first (db/get-item-details id))
        recipe-details (map #(assoc %1
                               :skills (get-skills-for-recipe %1)
                               :materials (get-materials-for-recipe %1 true))
                            (db/get-recipe-details id))]
    {:body {:target   (assoc item-details :recipes recipe-details)
            :products (look-forward (:name item-details))}}))

(defn get-materials-for-recipe
  ([lookup-data look-back?]
   (if look-back?
     (map #(conj %1 (look-back (:name %1))) (get-materials-for-recipe lookup-data))
     (get-materials-for-recipe lookup-data)))
  ([{id :id recipe-id :recipe_id}]
   (db/get-recipe-materials id recipe-id)))

(defn get-recipes [{name :name limit :limit page :page}]
  (let [limit (if (nil? limit) 15 (parse-int limit))
        page (if (nil? page) 1 (parse-int page))
        offset (* (dec page) limit)]
    (->> (db/get-item-details-matching-name name limit offset)
         (map #(assoc %1 :more_details (format "/api/recipe?id=%s" (:id %1))))
         (assoc-in {:body {:pages (db/memoized-page-count name limit)}} [:body :results]))))
