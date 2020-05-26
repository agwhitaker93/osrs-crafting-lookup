(ns osrs-crafting-lookup.util)

(defn parse-int [int?]
  (if (integer? int?) int? (Integer/parseInt int?)))
