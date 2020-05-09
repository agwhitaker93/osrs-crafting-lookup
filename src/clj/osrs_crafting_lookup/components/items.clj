(ns osrs-crafting-lookup.components.items
  (:require [ring.util.response :refer [response]]
            [clojure.string :refer [lower-case starts-with?]]
            [clj-http.client :as client]
            [osrs-crafting-lookup.config :refer [base-url]]
            [cheshire.core :refer [parse-string]]))

(def category-id 1)                                         ; only 1 category in osrs

(defn items-url [item-char page]
  (str base-url "/items.json?category=" category-id "&alpha=" item-char "&page=" page))

(defn get-page [item-char page]
  (-> (items-url item-char page)
      (client/get)
      (:body)
      (parse-string true)
      (:items)))

(defn get-all-pages [item-char]
  (loop [page 1
         page-results '()]
    (let [result (get-page item-char page)]
      (if (or (nil? result) (empty? result))
        page-results
        (recur (inc page) (flatten (conj page-results result)))))))

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
        query-pages (get-all-pages first-char)]
    (narrow-selection lower-cased query-pages)))

(defn handle [query]
  (-> (rs-lookup query)
      response
      (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))