(ns osrs-crafting-lookup.components.recipes
  (:require [osrs-crafting-lookup.database :as db]
            [osrs-crafting-lookup.util :refer [parse-int]]))

(defn get-skills-for-recipe [{id :id recipe-id :recipe_id}]
  (db/get-recipe-skills id recipe-id))

(defn get-materials-for-recipe [{id :id recipe-id :recipe_id}]
  (db/get-recipe-materials id recipe-id))

(defn get-products-for-recipe [recipe-name]
  (db/get-recipe-products recipe-name))

(defn get-product-details [{id :id recipe-id :recipe_id :as product} depth]
  product)

(defn get-material-details [{id :id recipe-id :recipe_id :as material} depth]
  material)

(defn get-recipe [{id             :id
                   name           :name
                   material-depth :materialDepth
                   product-depth  :productDepth
                   :or            {material-depth 0
                                   product-depth  0}}]
  (let [material-depth (parse-int material-depth)
        product-depth (parse-int product-depth)
        get-item-fn (if (not (nil? id))
                      #(db/get-item-details id)
                      #(db/get-item-details-by-name name))
        item-details (first (get-item-fn))
        recipe-details (map #(assoc %1
                                    :skills (get-skills-for-recipe %1)
                                    :materials (get-materials-for-recipe %1))
                            (db/get-recipe-details (:id item-details)))
        products (get-products-for-recipe (:name item-details))]
    (as-> {:target (assoc item-details :recipes recipe-details :products products)} $
      (if (not (= product-depth 0))
        (assoc $ :products (map #(get-product-details %1 (dec product-depth)) products))
        $)
      (if (not (= material-depth 0))
        (assoc $ :materials (map #(get-material-details %1 (dec material-depth)) (reduce #(conj %1 %2) [] recipe-details)))
        $)
      {:body $})))

(defn get-recipes [{name :name limit :limit page :page :or {limit 15 page 1} :as input}]
  (let [limit (parse-int limit)
        page (parse-int page)
        offset (* (dec page) limit)]
    (->> (db/get-item-details-matching-name name limit offset)
         (map #(assoc %1 :more_details (format "/api/recipe?id=%s" (:id %1))))
         (assoc-in {:body {:pages (db/memoized-page-count name limit)}} [:body :results]))))
