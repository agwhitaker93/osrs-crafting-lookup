(ns osrs-crafting-lookup.database
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.config :refer [db read-dir craftables-dir]])
  (:import (java.util Calendar)
           (java.sql Timestamp)))

(defn timestamp-now []
  (-> (Calendar/getInstance)
      (.getTimeInMillis)
      (Timestamp.)))

(defn insert-or-update [conn table to-insert duplicates-query update-clause]
  (if (empty? (jdbc/query conn duplicates-query))
    (jdbc/insert! conn table to-insert)
    (jdbc/update! conn table to-insert update-clause)))

(defn insert-recipe-skills [conn item-id recipe-id skills]
  (println "skills" skills)
  (map (fn [skill]
         (println "Not implemented")) skills))

(defn insert-recipe-materials [conn item-id recipe-id materials]
  (println "materials" materials)
  (map (fn [material]
         (println "Not implemented")) materials))

(defn insert-recipes [conn item-id recipes]
  (loop [recipe (first recipes)
         recipes (rest recipes)
         counter 1]
    (if (not (nil? recipe))
      (do
        (println "Hey, we're gonna insert some recipes")
        (insert-or-update conn "osrs.recipes" (assoc (dissoc recipe
                                                             :materials
                                                             :skills)
                                                :last_updated (timestamp-now)
                                                :id item-id
                                                :recipe_id counter)
                          [(str "SELECT id FROM osrs.recipes WHERE id = " item-id
                                " AND recipe_id = " counter)]
                          [(str "id = " item-id " AND recipe_id = " counter)])
        (println "Time to insert materials")
        (insert-recipe-materials conn item-id counter (:materials recipe))
        (println "Time to insert skills")
        (insert-recipe-skills conn item-id counter (:skills recipe))
        (recur (first recipes) (rest recipes) (inc counter))))))

(defn insert-meta [conn meta]
  (insert-or-update conn "osrs.items" (assoc meta :last_updated (timestamp-now))
                    [(str "SELECT id FROM osrs.items WHERE id = " (:id meta))]
                    [(str "id = " (:id meta))]))

(defn insert-and-update-all-craftables []
  (let [craftables (take 2 (read-dir craftables-dir))]
    (map #(do
            (println %1)
            (insert-meta db (:meta %1))
            (insert-recipes db (get-in %1 [:meta :id]) (:recipes %1))) craftables)))
