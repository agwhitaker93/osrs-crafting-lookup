(ns osrs-crafting-lookup.util)

(defn parse-int [int?]
  (if (integer? int?)
    int?
    (if (or (empty? int?) (not (string? int?)))
      0
      (Integer/parseInt int?))))
