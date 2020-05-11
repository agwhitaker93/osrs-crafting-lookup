(ns scratch
  (:require [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.components.items :as items]))

(def scraped-dir "resources/scraped")

(def craftables-dir "resources/craftable")

(defn list-files [dir]
  "file-seq returns the dir given as the first entry"
  (rest (file-seq (clojure.java.io/file dir))))

(defn read-file [file]
  (read-string (slurp file)))

; go through all of the scraped files
; check if we have a file in craftable for each item
; merge the data for both
; put in a new place
