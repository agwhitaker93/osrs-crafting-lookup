(ns scratch
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup [config :refer [db]]]
            [osrs-crafting-lookup.components.items :as items]))

(def ge-api-alphas (seq "abcdefghijklmnopqrstuvwxyz0123456789"))

(defn write-edn [item-char page contents]
  (spit (format "resources/scraped/osrs-%s-%s.edn" item-char page)
        (pr-str contents)))

(defn scrape-all-pages [item-char]
  (loop [page 1]
    (let [result (items/get-page item-char page)]
      (if (not (or (nil? result) (empty? result)))
        (do (write-edn item-char page result)
            (recur (inc page)))))))

(defn scrape []
  (map scrape-all-pages ge-api-alphas))
