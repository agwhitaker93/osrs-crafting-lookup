(ns database
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.config :refer [db]]
            [craftables :refer [transformed-target read-dir]]))

(defn insert-meta []
  (->> (read-dir transformed-target)
       (map :meta)))

(defn insert-recipes []
  (->> (read-dir transformed-target)
       (map :recipes)))
