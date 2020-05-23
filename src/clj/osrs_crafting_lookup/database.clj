(ns osrs-crafting-lookup.database
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.config :refer [db read-dir craftables-dir]])
  (:import (java.util Calendar)
           (java.sql Timestamp)))

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
  (insert-or-update-craftables (read-dir craftables-dir)))
