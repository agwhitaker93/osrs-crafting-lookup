(ns osrs-crafting-lookup.database
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :refer [lower-case]]
            [osrs-crafting-lookup.config :refer [db read-dir craftables-dir]]
            [osrs-crafting-lookup.util :refer [parse-int]])
  (:import (java.util Calendar)
           (java.sql Timestamp)))

(defn count-pages-matching-name [name limit]
  (Math/ceil (/ (:count (first (jdbc/query db ["SELECT count(1) FROM osrs.items WHERE lower(name) LIKE ?" (lower-case (str "%" name "%"))]))) limit)))

(def memoized-page-count (memoize count-pages-matching-name))

(defn get-item-details-matching-name [name limit offset]
  (jdbc/query db ["SELECT * FROM osrs.items WHERE lower(name) LIKE ? ORDER BY name LIMIT ? OFFSET ?" (lower-case (str "%" name "%")) (parse-int limit) (parse-int offset)]))

(defn get-item-details-by-name [name]
  (jdbc/query db ["SELECT * FROM osrs.items WHERE name = ?" name]))

(defn get-item-details [id]
  (jdbc/query db ["SELECT * FROM osrs.items WHERE id = ?" (parse-int id)]))

(defn get-recipe-details-for-recipe [id recipe-id]
  (jdbc/query db ["SELECT * FROM osrs.recipes WHERE id = ? AND recipe_id = ?" (parse-int id) (parse-int recipe-id)]))

(defn get-recipe-details [id]
  (jdbc/query db ["SELECT * FROM osrs.recipes WHERE id = ?" (parse-int id)]))

(defn get-recipe-skills-by-name [name]
  (jdbc/query db ["SELECT * FROM osrs.skills WHERE name = ?" name]))

(defn get-recipe-skills [item-id recipe-id]
  (jdbc/query db ["SELECT * FROM osrs.skills WHERE id = ? AND recipe_id = ?" (parse-int item-id) (parse-int recipe-id)]))

(defn get-recipe-materials-by-name [name]
  (jdbc/query db ["SELECT * FROM osrs.materials WHERE name = ?" name]))

(defn get-recipe-products [name]
  (jdbc/query db ["SELECT m.id, m.recipe_id, i.name, m.quantity, m.last_updated
FROM osrs.items AS i
LEFT JOIN osrs.materials m ON m.id = i.id
WHERE m.name = ?" name]))

(defn get-recipe-materials [item-id recipe-id]
  (jdbc/query db ["SELECT * FROM osrs.materials WHERE id = ? AND recipe_id = ?" (parse-int item-id) (parse-int recipe-id)]))

(defn timestamp-now []
  (-> (Calendar/getInstance)
      (.getTimeInMillis)
      (Timestamp.)))

(defn insert-or-update [conn table to-insert duplicates-string]
  (if (empty? (jdbc/query conn [(str "SELECT id FROM " table " WHERE " duplicates-string)]))
    (jdbc/insert! conn table to-insert)
    (jdbc/update! conn table to-insert [duplicates-string])))

(defn insert-recipe-skills [conn item-id recipe-id skills]
  (doall
   (map
    #(insert-or-update conn "osrs.skills"
                       (assoc %1 :id item-id
                              :recipe_id recipe-id
                              :last_updated (timestamp-now))
                       (str "id = " item-id
                            " AND recipe_id = " recipe-id
                            " AND name = '" (:name %1) "'")) skills)))

(defn insert-recipe-materials [conn item-id recipe-id materials]
  (doall
   (map
    #(insert-or-update conn "osrs.materials"
                       (assoc %1 :id item-id
                              :recipe_id recipe-id
                              :last_updated (timestamp-now))
                       (str "id = " item-id
                            " AND recipe_id = " recipe-id
                            " AND name = '" (clojure.string/escape (:name %1) {\' "''"}) "'")) materials)))

(defn insert-recipes [conn item-id recipes]
  (doall
   (map-indexed #(let [recipe-id (inc %1)
                       recipe (assoc (dissoc %2 :materials :skills)
                                     :last_updated (timestamp-now)
                                     :id item-id
                                     :recipe_id recipe-id)]
                   (insert-or-update conn "osrs.recipes" recipe (str "id = " item-id " AND recipe_id = " recipe-id))
                   (insert-recipe-materials conn item-id recipe-id (:materials %2))
                   (insert-recipe-skills conn item-id recipe-id (:skills %2))) recipes)))

(defn insert-meta [conn meta]
  (insert-or-update conn "osrs.items" (assoc meta :last_updated (timestamp-now)) (str "id = " (:id meta))))

(defn insert-or-update-craftables [craftables]
  (jdbc/with-db-transaction [t-conn db]
    (doall (map #(do
                   (insert-meta t-conn (assoc (:meta %1) :last_updated (timestamp-now)))
                   (insert-recipes t-conn (get-in %1 [:meta :id]) (:recipes %1))
                   nil) craftables))))

(defn init []
  (println "Initializing database")
  (insert-or-update-craftables (read-dir craftables-dir))
  (println "Successfully initialized database"))
