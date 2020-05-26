(ns ge-api
  (:require [osrs-crafting-lookup.config :refer [db]]
            [osrs-crafting-lookup.components.items :as items]
            [clojure.java.io :as io])
  (:import (java.io File)))

(def ge-api-alphas (seq "abcdefghijklmnopqrstuvwxyz0123456789"))

(defn write-edn [item-char page contents]
  (if (not (.exists (io/file "resources/scraped")))
    (.mkdirs (new File "resources/scraped")))
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
