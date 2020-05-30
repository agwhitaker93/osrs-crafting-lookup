(ns osrs-crafting-lookup.components.recipes
  (:require [osrs-crafting-lookup.database :as db]
            [osrs-crafting-lookup.util :refer [parse-int]]))

(defn get-skills-for-recipe [{id :id recipe-id :recipe_id}]
  (db/get-recipe-skills id recipe-id))

(defn get-materials-for-recipe [{id :id recipe-id :recipe_id}]
  (db/get-recipe-materials id recipe-id))

(defn get-products-for-recipe [recipe-name]
  (db/get-recipe-products recipe-name))

(defn get-product-details [product depth])

(defn get-material-details [material depth])

(defn get-recipe [{id :id
                   name :name
                   material-depth :material-depth
                   product-depth :product-depth
                   :or {material-depth 0
                        product-depth 0}}]
  (let [material-depth (parse-int material-depth)
        product-depth (parse-int product-depth)
        get-item-fn (if (not (nil? id))
                      #(db/get-item-details id)
                      #(db/get-item-details-by-name name))
        item-details (first (get-item-fn))
        recipes (db/get-recipe-details (:id item-details))
        skills (map #(get-skills-for-recipe %1) recipes)
        materials (map #(get-materials-for-recipe %1) recipes)
        recipes (map #(assoc %1
                             :skills skills
                             :materials materials) recipes)
        products (get-products-for-recipe (:name item-details))]
    (as-> {:body {:target   (assoc item-details :recipes recipes :products products)}} $
      (if (> product-depth 0)
        (assoc $ :products (map #(get-product-details %1 (dec product-depth)) products))
        $)
      (if (> material-depth 0)
        (assoc $ :materials (map #(get-material-details %1 (dec material-depth)) materials))
        $))))

(defn get-recipes [{name :name limit :limit page :page :or {limit 15 page 1} :as input}]
  (let [limit (parse-int limit)
        page (parse-int page)
        offset (* (dec page) limit)]
    (->> (db/get-item-details-matching-name name limit offset)
         (map #(assoc %1 :more_details (format "/api/recipe?id=%s" (:id %1))))
         (assoc-in {:body {:pages (db/memoized-page-count name limit)}} [:body :results]))))
