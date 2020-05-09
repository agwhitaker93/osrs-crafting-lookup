(ns osrs-crafting-lookup.components.search
  (:require [ring.util.response :refer [response]]
            [clojure.string :refer [lower-case starts-with?]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]))

(def base "http://services.runescape.com/m=itemdb_oldschool/api/catalogue")

(def category-id 1)                                         ; only 1 category in osrs

(def pages (range 1 13))                                    ; allegedly exactly 12 pages per "alpha" value

(defn detail-url [item-id]
  (str base "/detail.json?item=" item-id))

(defn category-url []
  (str base "/category.json?category=" category-id))

(defn items-url [item-char page]
  (str base "/items.json?category=" category-id "&alpha=" item-char "&page=" page))

(defn get-page [item-char page]
  (-> (items-url item-char page)
      (client/get)
      (:body)
      (parse-string true)
      (:items)))

(defn get-all-pages [item-char]
  (loop [page (first pages)
         rest-pages (rest pages)
         page-results '()]
    (if (nil? page)
      page-results
      (let [result (get-page item-char page)]
        (if (or (nil? result) (empty? result))
          (recur nil nil page-results)
          (recur (first rest-pages) (rest rest-pages) (flatten (conj page-results result))))))))

(def memoized-get-all-pages (memoize get-all-pages))

(defn narrow-selection [item-name results]
  (loop [result (first results)
         rest-results (rest results)
         narrowed-results '()]
    (if (or (nil? result) (empty? result))
      narrowed-results
      (let [lower-result-name (lower-case (:name result))]
        (if (starts-with? lower-result-name item-name)
          (recur (first rest-results) (rest rest-results) (flatten (conj narrowed-results result)))
          (recur (first rest-results) (rest rest-results) narrowed-results))))))

(defn rs-lookup [query]
  (let [lower-cased (lower-case query)
        first-char (first (seq lower-cased))
        query-pages (memoized-get-all-pages first-char)]
    (narrow-selection lower-cased query-pages)))

(defn handle [query]
  (-> (rs-lookup query)
      response
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
